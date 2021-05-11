package com.assessment.venue.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.assessment.venue.repository.VenueRepository

/*Factory class to instantiate parameterized viewmodels
* @param venueRepository: VenueRepository
*/
class VenueViewModelFactory(val repository: VenueRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VenueViewModel::class.java)) {
            return VenueViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}