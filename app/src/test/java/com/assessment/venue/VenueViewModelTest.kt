package com.assessment.venue

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.assessment.venue.db.VenueDetailsEntity
import com.assessment.venue.model.Resource
import com.assessment.venue.repository.VenueRepository
import com.assessment.venue.viewmodel.VenueViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


class VenueViewModelTest {

    private lateinit var repository:VenueRepository

    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(VenueRepository::class.java)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }


    @Test
    fun checkfetchVenueListOnSuccessResponse(){
        runBlocking {
            `when`(repository.getVenueSerachList("Norway"))
                .thenReturn(Resource.Success(arrayOf()))
            val viewModel = VenueViewModel(repository)
            viewModel.fetchVenueList("Norway")
            Assert.assertFalse(viewModel.progressBarLiveData.value == true)
            Assert.assertNotNull(viewModel.venueListLiveData.value)
        }
    }

    @Test
    fun checkfetchVenueListOnErrorResponse(){
        runBlocking {
            `when`(repository.getVenueSerachList("Norway"))
                .thenReturn(Resource.Error("Error"))
            val viewModel = VenueViewModel(repository)
            viewModel.fetchVenueList("Norway")
            Assert.assertFalse(viewModel.progressBarLiveData.value == true)
            Assert.assertEquals(viewModel.generalErrorLiveData.value, "Error")
        }
    }

    @Test
    fun checkfetchVenueDetailOnSuccessResponse(){
        runBlocking {
            `when`(repository.getVenueDetails("123"))
                .thenReturn(Resource.Success(mock(VenueDetailsEntity::class.java)))
            val viewModel = VenueViewModel(repository)
            viewModel.fetchVenueDetails("123")
            Assert.assertFalse(viewModel.progressBarLiveData.value == true)
            Assert.assertNotNull(viewModel.venueDetailLiveData.value)
        }
    }

    @Test
    fun checkfetchVenueDetailOnErrorResponse(){
        runBlocking {
            `when`(repository.getVenueDetails("123"))
                .thenReturn(Resource.Error("Error"))
            val viewModel = VenueViewModel(repository)
            viewModel.fetchVenueDetails("123")
            Assert.assertFalse(viewModel.progressBarLiveData.value == true)
            Assert.assertEquals(viewModel.generalErrorLiveData.value, "Error")
        }
    }

}