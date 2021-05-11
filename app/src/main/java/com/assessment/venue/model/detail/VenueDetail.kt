package com.assessment.venue.model.detail

import com.assessment.venue.model.Location

/**
 * Data class to hold venue detail
 */
data class VenueDetail(
    val id: String,
    val name: String,
    val contact: Contact,
    val location: Location,
    val rating: String?,
    val bestPhoto: BestPhoto?,
    val page: Page?

)
