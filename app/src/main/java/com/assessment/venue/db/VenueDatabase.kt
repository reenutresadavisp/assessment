package com.assessment.venue.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.assessment.venue.BuildConfig.DATABASE_NAME

/**
 * Main database description.
 */
@Database(entities = arrayOf(VenueEntity::class), version = 1)
@TypeConverters(VenueDetailsConverters::class)
abstract class VenueDatabase : RoomDatabase() {
    abstract fun venueDao(): VenueDao

    companion object {
        private var INSTANCE: VenueDatabase? = null
        fun getDatabase(context: Context): VenueDatabase? {
            if (INSTANCE == null) {
                synchronized(VenueDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        VenueDatabase::class.java, DATABASE_NAME
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}
