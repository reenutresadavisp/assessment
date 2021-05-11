package com.assessment.venue.repository

import com.assessment.venue.api.VenueApiService
import com.assessment.venue.db.VenueDao
import com.assessment.venue.db.VenueDetailsEntity
import com.assessment.venue.db.VenueEntity
import com.assessment.venue.model.Resource
import com.assessment.venue.model.detail.VenueDetail
import com.assessment.venue.model.search.Venue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/*Repository class of the Venue App. It depends on the api and room database to fetch data
* @param apiService: VenueApiService
* @param venueDao: VenueDao?
*/
class VenueRepository(val apiService: VenueApiService, val venueDao: VenueDao?) {

    /*Fetch venuelist for a given location from the apiService and save the data to database
     *on success else return cached data from the database if available
     * @param location: String?
     * @return Resource<Array<VenueEntity>?>
     */
    suspend fun getVenueSerachList(location: String): Resource<Array<VenueEntity>?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getVenueList(location)
                if (response.isSuccessful) {
                    saveVenueToDatabase(response.body()?.response?.venues, location)
                    val data = venueDao?.fetchVenues(location)
                    Resource.Success(data)
                } else {
                    fetchCachedVenueListIfAvailableFromDb(location)
                }
            } catch (throwable: Throwable) {
                fetchCachedVenueListIfAvailableFromDb(location)
            }
        }
    }

    /*Method to fetch venuelist for a given location from the database if available
    *else returns error message
    * @param location: String
    * @return Resource<Array<VenueEntity>?>
    */
    suspend fun fetchCachedVenueListIfAvailableFromDb(location: String): Resource<Array<VenueEntity>?> {
        val data = venueDao?.fetchVenues(location)
        return if (data.isNullOrEmpty()) {
            Resource.Error("No Data found")
        } else {
            Resource.Success(data)
        }
    }

    /*Method to insert/update venuelist to database
     * @param venues: List<Venue>?
     * @param location: String
     */
    suspend fun saveVenueToDatabase(venues: List<Venue>?, location: String) {
        venues?.let {
            for (venue in it) {
                val venueEntity = VenueEntity(venue.id, location, venue.name, venue.location.city)
                val data = venueDao?.isVenueAvailable(venue.id)
                if (data == null) {
                    venueDao?.insertVenues(venueEntity)
                } else {
                    venueDao?.updateVenue(venueEntity)
                }
            }
        }
    }

    /*Method to insert/update venuedetils to database
    * @param venue: VenueDetail
    */
    suspend fun saveVenueDetailsToDatabase(venue: VenueDetail) {

        val venueEntity = VenueDetailsEntity(
            venue.id,
            venue.name,
            venue.location.address,
            venue.page?.pageInfo?.description,
            venue.contact.formattedPhone,
            venue.bestPhoto?.prefix,
            venue.bestPhoto?.suffix,
            venue.rating
        )

        if (venueDao?.isVenueDetailAvailable(venue.id) == null) {
            venueDao?.insertVenueDetail(venueEntity)
        } else {
            venueDao.updateVenueDetails(venueEntity)
        }
    }

    /*Fetch venue details for a given venueId from the apiService and save the data to database
     *on success else return cached data from the database if available
     * @param venueId: String
     * @return Resource<VenueDetailsEntity?>
     */
    suspend fun getVenueDetails(venueId: String): Resource<VenueDetailsEntity?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getVenueDetails(venueId)
                if (response.isSuccessful) {
                    response.body()?.response?.venue?.let { saveVenueDetailsToDatabase(it) }
                    Resource.Success(venueDao?.fetchVenueDetails(venueId))
                } else {
                    fetchCachedVenueDetailIfAvailableFromDb(venueId)
                }
            } catch (throwable: Throwable) {
                fetchCachedVenueDetailIfAvailableFromDb(venueId)
            }
        }
    }

    /*Method to fetch venue details for a given venueid from the database if available
    *else returns error message
    * @param location: String
    * @return Resource<VenueDetailsEntity?>
    */
    suspend fun fetchCachedVenueDetailIfAvailableFromDb(venueId: String): Resource<VenueDetailsEntity?> {
        val data = venueDao?.fetchVenueDetails(venueId)
        return if (data != null) {
            Resource.Success(data)
        } else {
            Resource.Error("No Data found")
        }
    }

}