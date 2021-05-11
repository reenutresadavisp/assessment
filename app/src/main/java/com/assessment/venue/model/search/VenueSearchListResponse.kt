package com.assessment.venue.model.search

import com.assessment.venue.model.BaseResponse

/**
 * Data class to hold venue search list response
 */
data class VenueSearchListResponse(
    val meta: BaseResponse,
    val response: ListResponse
)
