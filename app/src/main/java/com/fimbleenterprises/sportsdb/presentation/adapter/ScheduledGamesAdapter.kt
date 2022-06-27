package com.fimbleenterprises.sportsdb.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fimbleenterprises.sportsdb.data.model.ScheduledGame
import com.fimbleenterprises.sportsdb.util.FimTown
import com.fimbleenterprises.sportsdb.databinding.EventListItemBinding
import com.fimbleenterprises.sportsdb.util.Helpers
import org.joda.time.DateTime

class ScheduledGamesAdapter constructor():RecyclerView.Adapter<ScheduledGamesAdapter.EventsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val binding = EventListItemBinding
            .inflate(LayoutInflater.from(parent.context),parent,false)
        return EventsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val team = differ.currentList[position]
        holder.bind(team)
    }

     /**
     * This a required callback object needed to leverage the differ val below.  This setup is
     * designed to replace the old .notifyDatasetChanged method that was deemed overy inefficient.
     */
    private val callback = object : DiffUtil.ItemCallback<ScheduledGame>() {

        /*
        Compares a single item using an arbitrary definition of sameness (in this case we chose
        the item's url property because they are always unique to the item.

        Called to check whether two objects represent the same item.
        For example, if your items have unique ids, this method should check their id equality.

        Note: null items in the list are assumed to be the same as another null item and are
        assumed to not be the same as a non-null item. This callback will not be invoked for
        either of those cases.

        */
        override fun areItemsTheSame(oldItem: ScheduledGame, newItem: ScheduledGame): Boolean {
            return oldItem.strEvent == newItem.strEvent
        }

        /*
        Called to check whether two items have the same data.
        This information is used to detect if the contents of an item have changed.
        */
        override fun areContentsTheSame(oldItem: ScheduledGame, newItem: ScheduledGame): Boolean {
           return oldItem == newItem
        }

    } // callback

    /**
     * This leverages the AsyncListDiffer class from the DiffUtil library.  It is effectively the
     * bucket that holds our listview items.  It's cool but still new and kinda scary to me.
     */
    val differ = AsyncListDiffer(this, callback)

    /* This is a tricksy way of creating an onItemClick listener that makes for an article to
       somehow magically be implied (it == Team) by the caller where it is implemented.
       I do not understand how this works. */
    private var onItemClickListener: ((ScheduledGame)->Unit)? = null

    /* Sets the onItemClickListener that magically returns an Team object as "it".
    Still no understand!  Caller will implement it as such:
        adapter.setOnItemClickListener {
            // it == Team
            it.title
            it.context
            etc.
        }
    */
    fun setOnItemClickListener(listener : (ScheduledGame) -> Unit) {
        onItemClickListener = listener
    }

    // must be implemented.  Return your list's size
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class EventsViewHolder(
        private val binding:EventListItemBinding):
        RecyclerView.ViewHolder(binding.root){
           fun bind(scheduledGame: ScheduledGame){
               Log.i(TAG, "EventsViewHolder|bind(args:[event]")

               if (scheduledGame.dateEvent != null) {
                   val dtEvent = DateTime(scheduledGame.dateEvent).toLocalDateTime();
                   val prettyDate = Helpers.DatesAndTimes.getPrettyDateAndTime(dtEvent.toDateTime())
                   binding.tvDescription.text = prettyDate
               }
               binding.tvTitle.text = scheduledGame.strEvent

               binding.root.setOnClickListener {
                  onItemClickListener?.let {
                        it(scheduledGame)
                  }
               }
           }
        }

    companion object {
        private const val TAG = "FIMTOWN|NextFiveEventsAdapter"
    }

    init {
        Log.i(TAG, "Initialized:NextFiveEventsAdapter")
    }

}









