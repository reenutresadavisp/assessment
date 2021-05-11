package com.assessment.venue.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assessment.venue.db.VenueDetails
import com.assessment.venue.db.VenueEntity
import com.assessment.venue.model.Resource
import com.assessment.venue.repository.VenueRepository
import kotlinx.coroutines.launch

/*ViewModel Class to handle repository calls
* @param venueRepository: VenueRepository
*/
class VenueViewModel(val venueRepository: VenueRepository) : ViewModel() {

    private val _progressBarLiveData = MutableLiveData<Boolean>()
    private val _venueListLiveData = MutableLiveData<Array<VenueEntity>>()
    private val _venueDetailLiveData = MutableLiveData<VenueEntity>()
    private val _generalErrorLiveData = MutableLiveData<String>()

    val progressBarLiveData: LiveData<Boolean> = _progressBarLiveData
    val venueListLiveData: LiveData<Array<VenueEntity>> = _venueListLiveData
    val venueDetailLiveData: LiveData<VenueEntity> = _venueDetailLiveData
    val generalErrorLiveData: LiveData<String> = _generalErrorLiveData

    /*Method to fetch venue list for a given location from the repository and
     * save the response to the corresponding livedatas
     * @param location: String?
     */
    fun fetchVenueList(location: String?) {
        _progressBarLiveData.value = true
        viewModelScope.launch {
            val result = venueRepository.getVenueSerachList(location ?: "")
            _progressBarLiveData.value = false
            when (result) {
                is Resource.Success -> {
                    _venueListLiveData.value = result.data
                }
                else -> {
                    _venueListLiveData.value = null
                    _generalErrorLiveData.value = result.message
                }
            }
        }
    }

    /*Method to fetch venue detail for a given venueId from the repository and
    * save the response to the corresponding livedatas
    * @param venueId: String
    */
    fun fetchVenueDetails(venueId: String) {
        _progressBarLiveData.value = true
        viewModelScope.launch {
            val result = venueRepository.getVenueDetails(venueId)
            _progressBarLiveData.value = false
            when (result) {
                is Resource.Success -> {
                    _venueDetailLiveData.value = result.data
                }
                is Resource.Error -> {
                    _generalErrorLiveData.value = result.message
                }
            }
        }
    }

    //Method to clear live data
    fun clearDetails() {
        _venueDetailLiveData.value = null
        _generalErrorLiveData.value = null
    }
}