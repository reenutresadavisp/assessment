package com.assessment.venue

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView

import androidx.navigation.findNavController
import com.assessment.venue.databinding.FragmentVenueListBinding

/**
 * Fragment to search a location and display list of venues near to that location
 */
class VenueListFragment : BaseFragment() {

    private var binding: FragmentVenueListBinding? = null


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
        binding?.venueLocationSearchView?.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
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
        viewModel.venueListLiveData.observe(viewLifecycleOwner) { list ->
            val adapter = VenueListAdapter(list) { item -> navigateToDetailScreen(item) }
            binding?.venueSearchList?.adapter = adapter
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