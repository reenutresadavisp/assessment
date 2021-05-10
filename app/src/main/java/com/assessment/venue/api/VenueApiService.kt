package com.assessment.venue.api

import com.assessment.venue.model.detail.VenueDetailResponse
import com.assessment.venue.model.search.VenueSearchListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VenueApiService {

    @GET("search?radius=1000&limit=10")
    suspend fun getVenueList(@Query("near") location: String): Response<VenueSearchListResponse>

    @GET("{venueId}")
    suspend fun getVenueDetails(@Path("venueId") venueId: String): Response<VenueDetailResponse>
}