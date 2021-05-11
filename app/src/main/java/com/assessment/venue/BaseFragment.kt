package com.assessment.venue

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.assessment.venue.api.RetrofitClient
import com.assessment.venue.db.RoomClient.getDao
import com.assessment.venue.repository.VenueRepository
import com.assessment.venue.viewmodel.VenueViewModel
import com.assessment.venue.viewmodel.VenueViewModelFactory

/**
 * Base fragment for performing common functionalities
 */
open class BaseFragment : Fragment() {

    lateinit var viewModel: VenueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val venueDao = getDao(requireActivity().applicationContext)
        val repository = VenueRepository(RetrofitClient.getVenueApiService(), venueDao)
        viewModel = ViewModelProvider(requireActivity(), VenueViewModelFactory(repository)).get(
            VenueViewModel::class.java
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clearDetails()
        setObservers()
    }

    //Method to observe the progressbar and generalError livedata
    private fun setObservers() {
        viewModel.progressBarLiveData.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading == true) {
                (requireActivity() as VenueActivity).showProgressBar(View.VISIBLE)
            } else (requireActivity() as VenueActivity).showProgressBar(View.GONE)
        }

        viewModel.generalErrorLiveData.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }
    }
}