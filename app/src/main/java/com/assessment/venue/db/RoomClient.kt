package com.assessment.venue.db

import android.content.Context
import com.google.gson.Gson
/**
 * Handles instantiation of dao ang gson
 * required for database operations
 */
object RoomClient {

    fun getGson(): Gson {
        return Gson()
    }

    fun getDao(context: Context): VenueDao? {
        return VenueDatabase.getDatabase(context)?.venueDao()
    }
}