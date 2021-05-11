package com.assessment.venue.repository

import com.assessment.venue.api.VenueApiService
import com.assessment.venue.db.VenueDao
import com.assessment.venue.db.VenueDetails
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
                    fetchCachedVenueListFromDb(location)
                } else {
                    fetchCachedVenueListFromDb(location)
                }
            } catch (throwable: Throwable) {
                fetchCachedVenueListFromDb(location)
            }
        }
    }

    /*Method to fetch venuelist for a given location from the database if available
    *else returns error message
    * @param location: String
    * @return Resource<Array<VenueEntity>?>
    */
    suspend fun fetchCachedVenueListFromDb(location: String): Resource<Array<VenueEntity>?> {
        val data = venueDao?.fetchVenues(location)
        return if (data.isNullOrEmpty()) {
            Resource.Error("No Data found")
        } else {
            Resource.Success(data)
        }
    }

    /*Method to delete/insert venuelist to database
     * @param venues: List<Venue>?
     * @param location: String
     */
    suspend fun saveVenueToDatabase(venues: List<Venue>?, location: String) {
        venueDao?.deleteVenues(location)
        venues?.let {
            for (venue in it) {
                val venueEntity = VenueEntity(venue.id, location, venue.name, venue.location.city)
                venueDao?.insertVenues(venueEntity)
            }
        }
    }

    /*Method to update venuedetils to database
    * @param venue: VenueDetail
    */
    suspend fun saveVenueDetailsToDatabase(venue: VenueDetail) {

        val venueDetails = VenueDetails(
            venue.location.address,
            venue.page?.pageInfo?.description,
            venue.contact.formattedPhone,
            venue.bestPhoto?.prefix,
            venue.bestPhoto?.suffix,
            venue.rating
        )

        venueDao?.updateVenueDetails(venueDetails,venue.id)
    }


    /*Fetch venue details for a given venueId from the apiService and save the data to database
     *on success else return cached data from the database if available
     * @param venueId: String
     * @return Resource<VenueEntity?>
     */
    suspend fun getVenueDetails(venueId: String): Resource<VenueEntity?> {
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
    * @return Resource<VenueEntity?>
    */
    suspend fun fetchCachedVenueDetailIfAvailableFromDb(venueId: String): Resource<VenueEntity?> {
        val data = venueDao?.fetchVenueDetails(venueId)
        return if (data != null) {
            Resource.Success(data)
        } else {
            Resource.Error("No Data found")
        }
    }

}