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

class VenueRepository(val apiService: VenueApiService, val venueDao: VenueDao?) {

    suspend fun getVenueSerachList(location: String): Resource<Array<VenueEntity>?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getVenueList(location)
                if (response.isSuccessful) {
                    saveVenueToDatabase(response.body()?.response?.venues, location)
                    val data = venueDao?.fetchVenues(location)
                    Resource.Success(data)
                } else {
                    fetchOldVenueListIfAvailableFromDb(location)
                }
            } catch (throwable: Throwable) {
                fetchOldVenueListIfAvailableFromDb(location)
            }
        }
    }

    suspend fun fetchOldVenueListIfAvailableFromDb(location: String): Resource<Array<VenueEntity>?> {
        val data = venueDao?.fetchVenues(location)
        return if (data.isNullOrEmpty()) {
            Resource.Error("No Data found")
        } else {
            Resource.Success(data)
        }
    }

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


    suspend fun getVenueDetails(venueId: String): Resource<VenueDetailsEntity?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getVenueDetails(venueId)
                if (response.isSuccessful) {
                    response.body()?.response?.venue?.let { saveVenueDetailsToDatabase(it) }
                    Resource.Success(venueDao?.fetchVenueDetails(venueId))
                } else {
                    fetchOldVenueDetailIfAvailableFromDb(venueId)
                }
            } catch (throwable: Throwable) {
                fetchOldVenueDetailIfAvailableFromDb(venueId)
            }
        }
    }

    suspend fun fetchOldVenueDetailIfAvailableFromDb(venueId: String): Resource<VenueDetailsEntity?> {
        val data = venueDao?.fetchVenueDetails(venueId)
        return if (data != null) {
            Resource.Success(data)
        } else {
            Resource.Error("No Data found")
        }
    }

}