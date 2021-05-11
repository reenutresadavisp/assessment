package com.assessment.venue.db

/**
 * Data class to hold venue details with 'id' as primary key
 */
data class VenueDetails(
    val address: String?,
    val description: String?,
    val contact: String?,
    val imagePrefix: String?,
    val imageSuffix: String?,
    val rating: String?
)