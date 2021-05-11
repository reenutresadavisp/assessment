package com.assessment.venue.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class to hold venue with 'id' as primarykey
 */
@Entity
data class VenueEntity(
    @PrimaryKey val id: String,
    val searchItem: String?,
    val title: String?,
    val city: String?
)
