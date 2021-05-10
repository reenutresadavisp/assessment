package com.assessment.venue


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.assessment.venue.api.RetrofitClient
import com.assessment.venue.databinding.FragmentVenueDetailBinding
import com.assessment.venue.db.VenueDatabase
import com.assessment.venue.repository.VenueRepository
import com.assessment.venue.viewmodel.VenueViewModel
import com.assessment.venue.viewmodel.VenueViewModelFactory
import com.squareup.picasso.Picasso


class VenueDetailsFragment : Fragment() {

    val args: VenueDetailsFragmentArgs by navArgs()

    private var binding: FragmentVenueDetailBinding? = null
    private lateinit var viewModel: VenueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val venueDao =
            activity?.let { VenueDatabase.getDatabase(it.applicationContext) }?.venueDao()
        val repository = VenueRepository(RetrofitClient.getVenueApiService(), venueDao)
        viewModel = ViewModelProvider(requireActivity(), VenueViewModelFactory(repository)).get(
            VenueViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVenueDetailBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.venueId
        viewModel.fetchVenueDetails(id)
        setObservers()
    }

    private fun setObservers() {
        viewModel.progressBarLiveData.observe(viewLifecycleOwner) { isLoading ->
            binding?.progressCircular?.visibility = if (isLoading == true) {
                View.VISIBLE
            } else View.GONE
        }

        viewModel.generalErrorLiveData.observe(viewLifecycleOwner) {message ->
            if(!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.venueDetailLiveData.observe(viewLifecycleOwner) { details ->
            if (details != null) {
                binding?.apply {
                    venueTitle.text = details.title
                    venueDesc.text = details.description
                    venueAddress.text = details.address
                    venueContact.text = details.contact
                    venueRating.rating = details.rating?.toFloat() ?: 0f
                }
                loadImage(details.imagePrefix + details.imageSuffix)
            }
        }
    }

    private fun loadImage(imageUrl: String) {
        Picasso.get().load(imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.no_image)
            .into(binding?.venueImage)
    }
}