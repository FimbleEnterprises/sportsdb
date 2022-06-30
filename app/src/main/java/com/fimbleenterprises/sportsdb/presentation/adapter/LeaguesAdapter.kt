package com.fimbleenterprises.sportsdb.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fimbleenterprises.sportsdb.data.model.League
import com.fimbleenterprises.sportsdb.databinding.EventListItemBinding

class LeaguesAdapter :RecyclerView.Adapter<LeaguesAdapter.LeaguesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaguesViewHolder {
        val binding = EventListItemBinding
            .inflate(LayoutInflater.from(parent.context),parent,false)
        return LeaguesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeaguesViewHolder, position: Int) {
        val league = differ.currentList[position]
        holder.bind(league)
    }

     /**
     * This a required callback object needed to leverage the differ val below.  This setup is
     * designed to replace the old .notifyDatasetChanged method that was deemed overly inefficient.
     */
    private val callback = object : DiffUtil.ItemCallback<League>() {

        /*
        Compares a single item using an arbitrary definition of sameness (in this case we chose
        the item's url property because they are always unique to the item.

        Called to check whether two objects represent the same item.
        For example, if your items have unique ids, this method should check their id equality.

        Note: null items in the list are assumed to be the same as another null item and are
        assumed to not be the same as a non-null item. This callback will not be invoked for
        either of those cases.

        */
        override fun areItemsTheSame(oldLeague: League, newLeague: League): Boolean {
            return oldLeague.strLeague == newLeague.strLeague
        }

        /*
        Called to check whether two items have the same data.
        This information is used to detect if the contents of an item have changed.
        */
        override fun areContentsTheSame(oldItem: League, newItem: League): Boolean {
           return oldItem == newItem
        }

     } // callback

    /**
     * This leverages the AsyncListDiffer class from the DiffUtil library.  It is effectively the
     * bucket that holds our listview items.  It's cool but still new and kinda scary to me.
     */
    val differ = AsyncListDiffer(this, callback)

    /* This is a tricksy way of creating an onItemClick listener that makes for an object to
       somehow magically be implied (it == your object) by the caller where it is implemented.
       I do not understand how this works. */
    private var onItemClickListener: ((League)->Unit)? = null
    private var onItemLongClickListener: ((League)->Unit)? = null

    /* Sets the onItemClickListener that magically returns a Team object as "it".
    Still no understand!  Caller will implement it as such:
        adapter.setOnItemClickListener {
            // it == Team
            it.title
            it.context
            etc.
        }
    */
    fun setOnItemClickListener(listener : (League) -> Unit) {
        onItemClickListener = listener
    }

    // must be implemented.  Return your list's size
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class LeaguesViewHolder(val binding:EventListItemBinding): RecyclerView.ViewHolder(binding.root){

           fun bind(league: League){
               binding.tvTitle.text = league.strLeague
               binding.tvDescription.text = league.strSport

               binding.root.setOnClickListener {
                  onItemClickListener?.let {
                        it(league)
                  }
               }

               binding.root.setOnLongClickListener {
                   onItemLongClickListener?.let {
                       it(league)
                   }
                   false
               }
           }
        }



    companion object {
        private const val TAG = "FIMTOWN|LeaguesAdapter"
    }

    init {
        Log.i(TAG, "Initialized:LeaguesAdapter")
    }

}









