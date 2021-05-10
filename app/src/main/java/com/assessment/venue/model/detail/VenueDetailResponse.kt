package com.assessment.venue.model.detail

import com.assessment.venue.model.BaseResponse
import com.assessment.venue.model.search.ListResponse

data class VenueDetailResponse (
    val meta: BaseResponse,
    val response: DetailResponse
)