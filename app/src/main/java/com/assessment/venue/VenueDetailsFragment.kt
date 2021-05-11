package com.assessment.venue


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.assessment.venue.databinding.FragmentVenueDetailBinding
import com.squareup.picasso.Picasso

/**
 * Fragment to display the details of a venue
 */
class VenueDetailsFragment : BaseFragment() {

    val args: VenueDetailsFragmentArgs by navArgs()

    private var binding: FragmentVenueDetailBinding? = null

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

    //Method to observe detail livedata and populate ui
    private fun setObservers() {
        viewModel.venueDetailLiveData.observe(viewLifecycleOwner) { venue ->
            if (venue != null) {
                binding?.apply {
                    venueTitle.text = venue.title
                    venueDesc.text = venue.detail?.description
                    venueAddress.text = venue.detail?.address
                    venueContact.text = venue.detail?.contact
                    venueRating.rating = venue.detail?.rating?.toFloat() ?: 0f
                }
                loadImage(venue.detail?.imagePrefix + venue.detail?.imageSuffix)
            }
        }
    }

    //Method to load image using Picasso
    private fun loadImage(imageUrl: String) {
        Picasso.get().load(imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.no_image)
            .into(binding?.venueImage)
    }
}