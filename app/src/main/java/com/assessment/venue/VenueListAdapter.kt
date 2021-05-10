package com.assessment.venue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.assessment.venue.db.VenueEntity
import com.assessment.venue.model.search.Venue

class VenueListAdapter(private val venueList: Array<VenueEntity>?, val onClick: (String) -> Unit) :
    RecyclerView.Adapter<VenueListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_item_venue_search_list, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.venueTitle.text = venueList?.get(position)?.title
        viewHolder.venueLocation.text = venueList?.get(position)?.city
        viewHolder.cardView.setOnClickListener({ venueList?.let { onClick(it.get(position).id) } })
    }

    override fun getItemCount() = venueList?.size ?: 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView
        val venueTitle: TextView
        val venueLocation: TextView

        init {
            cardView = view.findViewById(R.id.venue_list_item_card_view)
            venueTitle = view.findViewById(R.id.venue_title)
            venueLocation = view.findViewById(R.id.venue_location)
        }
    }
}