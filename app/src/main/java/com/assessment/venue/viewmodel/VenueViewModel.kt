package com.assessment.venue.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assessment.venue.db.VenueDetailsEntity
import com.assessment.venue.db.VenueEntity
import com.assessment.venue.model.Resource
import com.assessment.venue.repository.VenueRepository
import kotlinx.coroutines.launch

class VenueViewModel(val venueRepository: VenueRepository) : ViewModel() {

    private val _progressBarLiveData = MutableLiveData<Boolean>()
    private val _venueListLiveData = MutableLiveData<Array<VenueEntity>>()
    private val _venueDetailLiveData = MutableLiveData<VenueDetailsEntity>()
    private val _generalErrorLiveData = MutableLiveData<String>()

    val progressBarLiveData: LiveData<Boolean> = _progressBarLiveData
    val venueListLiveData: LiveData<Array<VenueEntity>> = _venueListLiveData
    val venueDetailLiveData: LiveData<VenueDetailsEntity> = _venueDetailLiveData
    val generalErrorLiveData: LiveData<String> = _generalErrorLiveData

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

    fun clearDetails() {
        _venueDetailLiveData.value = null
        _generalErrorLiveData.value = null
    }
}