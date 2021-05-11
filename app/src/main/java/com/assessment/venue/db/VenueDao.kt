package com.assessment.venue.db

import androidx.room.*

/**
 * Interface for database access for venue related operations.
 */
@Dao
interface VenueDao {
    @Query("SELECT * FROM VenueEntity WHERE id LIKE :id")
    suspend fun fetchVenueDetails(id: String): VenueEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVenues(venueEntity: VenueEntity)

    @Query("DELETE  from VenueEntity where searchItem LIKE :searchItem")
    suspend fun deleteVenues(searchItem: String)

    @Query("Select * from VenueEntity where searchItem = :search")
    suspend fun fetchVenues(search: String): Array<VenueEntity>

    @Query("UPDATE VenueEntity SET detail = :detail WHERE id = :id")
    suspend fun updateVenueDetails(detail: VenueDetails, id: String)
}