package com.assessment.venue.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Interface for database access for venue related operations.
 */
@Dao
interface VenueDao {
    @Query("SELECT * FROM VenueDetailsEntity WHERE id LIKE :id")
    suspend fun fetchVenueDetails(id: String): VenueDetailsEntity

    @Insert
    fun insertVenues(venueEntity: VenueEntity)

    @Query("SELECT * FROM VenueEntity WHERE id LIKE :id")
    suspend fun isVenueAvailable(id: String): VenueEntity

    @Query("SELECT * FROM VenueDetailsEntity WHERE id LIKE :id")
    suspend fun isVenueDetailAvailable(id: String): VenueDetailsEntity

    @Query("Select * from VenueEntity where searchItem = :search")
    suspend fun fetchVenues(search: String): Array<VenueEntity>

    @Insert
    suspend fun insertVenueDetail(detail: VenueDetailsEntity)

    @Update
    suspend fun updateVenueDetails(venueEntity: VenueDetailsEntity)

    @Update
    suspend fun updateVenue(venueEntity: VenueEntity)
}