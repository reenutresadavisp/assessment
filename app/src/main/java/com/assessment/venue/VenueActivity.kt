package com.assessment.venue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.assessment.venue.databinding.ActivityVenueBinding

/**
 * Main activity for ListFragment and DetailsFragment
 */
class VenueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVenueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVenueBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    // Method to show progressbar
    fun showProgressBar(visibility: Int) {
        binding.progressCircular.visibility = visibility
    }
}