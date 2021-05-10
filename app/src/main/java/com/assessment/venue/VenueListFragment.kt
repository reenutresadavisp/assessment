package com.assessment.venue

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.assessment.venue.api.RetrofitClient
import com.assessment.venue.databinding.FragmentVenueDetailBinding
import com.assessment.venue.databinding.FragmentVenueListBinding
import com.assessment.venue.db.VenueDatabase
import com.assessment.venue.repository.VenueRepository
import com.assessment.venue.viewmodel.VenueViewModel
import com.assessment.venue.viewmodel.VenueViewModelFactory

class VenueListFragment : Fragment() {

    private lateinit var viewModel: VenueViewModel
    private var binding: FragmentVenueListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val venueDao =
            activity?.let { VenueDatabase.getDatabase(it.applicationContext) }?.venueDao()
        val repository = VenueRepository(RetrofitClient.getVenueApiService(), venueDao)
        viewModel= ViewModelProvider(requireActivity(),VenueViewModelFactory(repository)).get(VenueViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVenueListBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.clearDetails()
        binding?.venueLocationSearchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.fetchVenueList(query)
                binding?.venueLocationSearchView?.hideKeyboard()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        setObservers()

    }

    private fun setObservers() {
        viewModel.progressBarLiveData.observe(viewLifecycleOwner) { isLoading ->
            binding?.progressCircular?.visibility = if (isLoading == true) {
                View.VISIBLE
            } else View.GONE
        }

        viewModel.venueListLiveData.observe(viewLifecycleOwner) { list ->
            val adapter = VenueListAdapter(list) { item -> navigateToDetailScreen(item) }
            binding?.venueSearchList?.adapter = adapter
        }

        viewModel.generalErrorLiveData.observe(viewLifecycleOwner) {message ->
            if(!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateToDetailScreen(id: String) {
        val action = VenueListFragmentDirections.actionVenueListFragmentToVenueDetailsFragment(id)
        view?.findNavController()?.navigate(action)
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}