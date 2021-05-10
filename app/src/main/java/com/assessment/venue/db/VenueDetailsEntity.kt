package com.assessment.venue.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VenueDetailsEntity(
    @PrimaryKey val id: String,
    val title: String?,
    val address: String?,
    val description: String?,
    val contact: String?,
    val imagePrefix: String?,
    val imageSuffix: String?,
    val rating: String?
)