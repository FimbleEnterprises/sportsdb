package com.fimbleenterprises.sportsdb.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fimbleenterprises.sportsdb.data.model.ScheduledGame
import com.fimbleenterprises.sportsdb.databinding.EventListItemBinding
import com.fimbleenterprises.sportsdb.util.Helpers
import org.joda.time.DateTime

@Suppress("unused")
class ScheduledGamesAdapter: RecyclerView.Adapter<ScheduledGamesAdapter.EventsViewHolder>() {

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
     * designed to replace the old .notifyDatasetChanged method that was deemed overly inefficient.
     */
    private val callback = object : DiffUtil.ItemCallback<ScheduledGame>() {

        /*
        Compares a single item using an arbitrary definition of sameness (in this case we chose
        the item's strEvent property because they are always unique to the item.
        */
        override fun areItemsTheSame(oldItem: ScheduledGame, newItem: ScheduledGame): Boolean {
            return oldItem.strEvent == newItem.strEvent
        }

        /*
        Called to check whether two items have the same data.
        This is used to detect if the contents of an item have changed.
        */
        override fun areContentsTheSame(oldItem: ScheduledGame, newItem: ScheduledGame): Boolean {
           return oldItem == newItem
        }

    }

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
            // it == ScheduledGame
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
                   /*val dtEvent = DateTime(scheduledGame.dateEvent)
                   val prettyDate = Helpers.DatesAndTimes.getPrettyDate(dtEvent)
                   binding.tvDescription.text = prettyDate*/
                   binding.tvDescription.text = scheduledGame.toPrettyDateTime()
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









