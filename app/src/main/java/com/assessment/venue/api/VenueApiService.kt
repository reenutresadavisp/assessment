package com.assessment.venue.api

import com.assessment.venue.BuildConfig.*
import com.assessment.venue.model.detail.VenueDetailResponse
import com.assessment.venue.model.search.VenueSearchListResponse
import com.assessment.venue.util.Constants.API_PARAM_NEAR
import com.assessment.venue.util.Constants.API_PARAM_VENUE_ID
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * REST API access points
 */
interface VenueApiService {

    @GET(SEARCH_API_URL)
    suspend fun getVenueList(@Query(API_PARAM_NEAR) location: String): Response<VenueSearchListResponse>

    @GET(VENUE_DETAIL_API_URL)
    suspend fun getVenueDetails(@Path(API_PARAM_VENUE_ID) venueId: String): Response<VenueDetailResponse>
}