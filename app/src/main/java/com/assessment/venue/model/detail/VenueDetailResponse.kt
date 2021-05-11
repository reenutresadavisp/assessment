package com.assessment.venue.model.detail

import com.assessment.venue.model.BaseResponse

/**
 * Data class to hold venue detail response
 */
data class VenueDetailResponse(
    val meta: BaseResponse,
    val response: DetailResponse
)