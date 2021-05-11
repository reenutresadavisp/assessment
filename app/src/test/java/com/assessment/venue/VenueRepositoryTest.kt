package com.assessment.venue

import com.assessment.venue.api.VenueApiService
import com.assessment.venue.db.VenueDao
import com.assessment.venue.db.VenueDetails
import com.assessment.venue.db.VenueEntity
import com.assessment.venue.model.detail.VenueDetailResponse
import com.assessment.venue.model.search.VenueSearchListResponse
import com.assessment.venue.repository.VenueRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.kotlin.*

import retrofit2.Response

//Test Class to VenueRepository
class VenueRepositoryTest {

    private lateinit var apiService: VenueApiService
    private lateinit var venueDao: VenueDao

    @Before
    fun setup() {
        apiService = mock(VenueApiService::class.java)
        venueDao = mock(VenueDao::class.java)
    }


    @Test
    fun checkgetVenueListOnApiSuccess() {
        val response = mock(Response::class.java) as? Response<VenueSearchListResponse>
        `when`(response?.isSuccessful())
            .thenReturn(true)


        runBlocking {
            `when`(apiService.getVenueList("Norway"))
                .thenReturn(response)
            val repository = VenueRepository(apiService, venueDao)
            repository.getVenueSerachList("Norway")
            verify(venueDao).fetchVenues(eq("Norway"))
        }
    }

    @Test
    fun fetchOldDataIfAvailableFromDbOnApiError() {
        val result = arrayOf(
            VenueEntity(
                "123",
                "Norway",
                "title",
                "city"
            )
        )
        val response = mock(Response::class.java) as? Response<VenueSearchListResponse>
        `when`(response?.isSuccessful())
            .thenReturn(false)

        runBlocking {
            `when`(apiService.getVenueList("Norway"))
                .thenReturn(response)

            `when`(venueDao.fetchVenues("Norway"))
                .thenReturn(result)

            val repository = VenueRepository(apiService, venueDao)
            val actualResult = repository.getVenueSerachList("Norway").data
            Assert.assertEquals("Norway", actualResult?.first()?.searchItem)
        }
    }

    @Test
    fun checkErrorIfOldDataIfNotAvailableFromDbOnApiError() {
        val response = mock(Response::class.java) as? Response<VenueSearchListResponse>
        `when`(response?.isSuccessful())
            .thenReturn(false)

        runBlocking {
            `when`(apiService.getVenueList("Norway"))
                .thenReturn(response)

            `when`(venueDao.fetchVenues("Norway"))
                .thenReturn(null)

            val repository = VenueRepository(apiService, venueDao)
            val actualResult = repository.getVenueSerachList("Norway").message
            Assert.assertEquals("No Data found", actualResult)
        }
    }

    @Test
    fun checkgetVenueDetailOnApiSuccess() {
        val response = mock(Response::class.java) as? Response<VenueDetailResponse>
        `when`(response?.isSuccessful())
            .thenReturn(true)


        runBlocking {
            `when`(apiService.getVenueDetails("123"))
                .thenReturn(response)
            val repository = VenueRepository(apiService, venueDao)
            repository.getVenueDetails("123")
            verify(venueDao).fetchVenueDetails(eq("123"))
        }
    }

    @Test
    fun fetchOldDetailsIfAvailableFromDbOnApiError() {
        val result = VenueDetails(
            "address",
            "description",
            "contact",
            "imagePrefix",
            "imageSuffix",
            "2.5"
        )
        val response = mock(Response::class.java) as? Response<VenueDetailResponse>
        `when`(response?.isSuccessful())
            .thenReturn(false)

        runBlocking {
            `when`(apiService.getVenueDetails("123"))
                .thenReturn(response)

            `when`(venueDao.fetchVenueDetails("123"))
                .thenReturn(VenueEntity("123","norway","title","city",result))

            val repository = VenueRepository(apiService, venueDao)
            val actualResult = repository.getVenueDetails("123").data
            Assert.assertEquals("2.5", actualResult?.detail?.rating)
        }
    }

    @Test
    fun checkErrorIfOldDetailIfNotAvailableFromDbOnApiError() {
        val response = mock(Response::class.java) as? Response<VenueDetailResponse>
        `when`(response?.isSuccessful())
            .thenReturn(false)

        runBlocking {
            `when`(apiService.getVenueDetails("123"))
                .thenReturn(response)

            `when`(venueDao.fetchVenueDetails("123"))
                .thenReturn(null)

            val repository = VenueRepository(apiService, venueDao)
            val actualResult = repository.getVenueDetails("123").message
            Assert.assertEquals("No Data found", actualResult)
        }
    }


}