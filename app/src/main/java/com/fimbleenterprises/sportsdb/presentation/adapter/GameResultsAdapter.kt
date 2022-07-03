package com.fimbleenterprises.sportsdb.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fimbleenterprises.sportsdb.MyApp
import com.fimbleenterprises.sportsdb.R
import com.fimbleenterprises.sportsdb.data.model.GameResult
import com.fimbleenterprises.sportsdb.databinding.GameResultListItemBinding
import com.fimbleenterprises.sportsdb.util.Helpers
import org.joda.time.DateTime

class GameResultsAdapter constructor(private val context: Context):RecyclerView.Adapter<GameResultsAdapter.EventsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val binding = GameResultListItemBinding
            .inflate(LayoutInflater.from(parent.context),parent,false)
        return EventsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val game = differ.currentList[position]
        holder.bind(game)
    }

     /**
     * This a required callback object needed to leverage the differ val below.  This setup is
     * designed to replace the old .notifyDatasetChanged method that was deemed overy inefficient.
     */
    private val callback = object : DiffUtil.ItemCallback<GameResult>() {

        /*
        Compares a single item using an arbitrary definition of sameness (in this case we chose
        the item's url property because they are always unique to the item.

        Called to check whether two objects represent the same item.
        For example, if your items have unique ids, this method should check their id equality.

        Note: null items in the list are assumed to be the same as another null item and are
        assumed to not be the same as a non-null item. This callback will not be invoked for
        either of those cases.

        */
        override fun areItemsTheSame(oldItem: GameResult, newItem: GameResult): Boolean {
            return oldItem.strEvent == newItem.strEvent
        }

        /*
        Called to check whether two items have the same data.
        This information is used to detect if the contents of an item have changed.
        */
        override fun areContentsTheSame(oldItem: GameResult, newItem: GameResult): Boolean {
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
    private var onItemClickListener: ((GameResult)->Unit)? = null

    /* Sets the onItemClickListener that magically returns an Team object as "it".
    Still no understand!  Caller will implement it as such:
        adapter.setOnItemClickListener {
            // it == GameResult
            it.title
            it.context
            etc.
        }
    */
    fun setOnItemClickListener(listener : (GameResult) -> Unit) {
        onItemClickListener = listener
    }

    // must be implemented.  Return your list's size
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class EventsViewHolder(
        private val binding:GameResultListItemBinding): RecyclerView.ViewHolder(binding.root) {

            @SuppressLint("SetTextI18n")
            fun bind(gameResult: GameResult){
               Log.i(TAG, "EventsViewHolder|bind(args:[event]")

               if (gameResult.dateEvent != null && gameResult.dateEvent != "") {

                   try { // Parsing dates is never trivial
                       val dtEvent = DateTime(gameResult.dateEvent)
                       val prettyDate = Helpers.DatesAndTimes.getPrettyDate(dtEvent)
                       binding.txtDate.text ="${prettyDate}\n${gameResult.strEvent}"
                   } catch (exception:Exception) {
                       Log.e(TAG, "bind: ${exception.localizedMessage}"
                           , exception)
                   }
               }

               when {// Scores when applicable
                   !gameResult.strHomeTeam.isNullOrEmpty() && !gameResult.intHomeScore.isNullOrEmpty() -> {
                       binding.txtHomeTeam.text = "${gameResult.strHomeTeam} - ${gameResult.intHomeScore}"
                   }
               }

               when {// Scores when applicable
                   !gameResult.strAwayTeam.isNullOrEmpty() && !gameResult.intAwayScore.isNullOrEmpty() -> {
                       // e.g. "Baltimore Oriels - 5"
                       binding.txtVisitors.text = "${gameResult.strAwayTeam} - ${gameResult.intAwayScore}"
                   }
               }

               // Hide if no values
               binding.txtHomeTeam.apply {
                   if (gameResult.strHomeTeam.isNullOrEmpty()) this.visibility = GONE else this.visibility = VISIBLE
               }

               // Hide if no values
               binding.txtVisitors.apply {
                   if (gameResult.strAwayTeam.isNullOrEmpty()) this.visibility = GONE else this.visibility = VISIBLE
               }

               // Check if highlights are available
               if (!gameResult.strVideo.isNullOrBlank()) {
                   binding.txtHighlights.text = context.getString(R.string.highlights_available)
                   Helpers.Animations.pulseAnimation(binding.txtHighlights)
               } else {
                   binding.txtHighlights.visibility = GONE
               }

               when { // User can opt to show ugly ass box scores (prolly only applies to MLB)
                   MyApp.AppPreferences.showSummariesInBoxscore -> {
                       binding.txtSummary.visibility = VISIBLE
                       if (!gameResult.strResult.isNullOrEmpty()) {
                           binding.txtSummary.text = "${Html.fromHtml("<br />${gameResult
                               .strResult}", Html.FROM_HTML_SEPARATOR_LINE_BREAK_DIV)}"
                           binding.txtSummary.setTextColor(Color.BLUE)
                       } else {
                           binding.txtSummary.text = context.getString(R.string.boxscore_not_available)
                           binding.txtSummary.setTextColor(Color.RED)
                       }
                   }
                   else -> {
                       binding.txtSummary.visibility = GONE
                   }
               }

               binding.root.setOnClickListener {
                  onItemClickListener?.let {
                        it(gameResult)
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









