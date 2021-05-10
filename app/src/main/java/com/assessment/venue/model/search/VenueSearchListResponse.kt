package com.assessment.venue.model.search

import com.assessment.venue.model.BaseResponse
import com.google.gson.annotations.SerializedName

data class VenueSearchListResponse(
    val meta: BaseResponse,
    val response: ListResponse
)
