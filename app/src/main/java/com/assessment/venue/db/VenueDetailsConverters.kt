package com.assessment.venue.db

import androidx.room.TypeConverter
import com.assessment.venue.db.RoomClient.getGson
import com.google.gson.reflect.TypeToken

/**
 * TypeConverter class which converts VenueDetails to string and vice versa
 * to make custom type suitable for room database
 */
class VenueDetailsConverters {
    @TypeConverter
    fun fromString(value: String): VenueDetails? {
        val type = object: TypeToken<VenueDetails?>() {}.type
        return getGson().fromJson(value, type)
    }

    @TypeConverter
    fun fromVenueDetails(details: VenueDetails?): String {
        val type = object: TypeToken<VenueDetails?>() {}.type
        return getGson().toJson(details, type)
    }
}