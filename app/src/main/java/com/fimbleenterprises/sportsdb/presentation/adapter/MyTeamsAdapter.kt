package com.fimbleenterprises.sportsdb.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fimbleenterprises.sportsdb.data.model.SportsTeam
import com.bumptech.glide.Glide
import com.fimbleenterprises.sportsdb.databinding.ListItemBinding

class MyTeamsAdapter constructor():RecyclerView.Adapter<MyTeamsAdapter.TeamsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamsViewHolder {
        val binding = ListItemBinding
            .inflate(LayoutInflater.from(parent.context),parent,false)
        return TeamsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeamsViewHolder, position: Int) {
        val team = differ.currentList[position]
        holder.bind(team)
    }

     /**
     * This a required callback object needed to leverage the differ val below.  This setup is
     * designed to replace the old .notifyDatasetChanged method that was deemed overy inefficient.
     */
    private val callback = object : DiffUtil.ItemCallback<SportsTeam>() {

        /*
        Compares a single item using an arbitrary definition of sameness (in this case we chose
        the item's url property because they are always unique to the item.

        Called to check whether two objects represent the same item.
        For example, if your items have unique ids, this method should check their id equality.

        Note: null items in the list are assumed to be the same as another null item and are
        assumed to not be the same as a non-null item. This callback will not be invoked for
        either of those cases.

        */
        override fun areItemsTheSame(oldItem: SportsTeam, newItem: SportsTeam): Boolean {
            return oldItem.strTeam == newItem.strTeam
        }

        /*
        Called to check whether two items have the same data.
        This information is used to detect if the contents of an item have changed.
        */
        override fun areContentsTheSame(oldItem: SportsTeam, newItem: SportsTeam): Boolean {
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
    private var onItemClickListener: ((SportsTeam)->Unit)? = null

    /* Sets the onItemClickListener that magically returns an Team object as "it".
    Still no understand!  Caller will implement it as such:
        adapter.setOnItemClickListener {
            // it == Team
            it.title
            it.context
            etc.
        }
    */
    fun setOnItemClickListener(listener : (SportsTeam) -> Unit) {
        onItemClickListener = listener
    }

    // must be implemented.  Return your list's size
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class TeamsViewHolder(
        val binding:ListItemBinding):
        RecyclerView.ViewHolder(binding.root){
           fun bind(sportsTeam: SportsTeam){

               /*when {
                   showDeleteButton -> {
                       binding.fabDeleteItem.visibility = View.VISIBLE
                   } else -> {
                       binding.fabDeleteItem.visibility = View.GONE
                   }
               }*/

               binding.tvTitle.text = sportsTeam.strTeam
               binding.tvDescription.text = sportsTeam.strDescriptionEN
               binding.tvbottom1.text = sportsTeam.strSport
               binding.tvbottom2.text = sportsTeam.strLeague

               Glide.with(binding.ivMainImage.context).
               load(sportsTeam.strTeamLogo).
               into(binding.ivMainImage)

               binding.root.setOnClickListener {
                  onItemClickListener?.let {
                        it(sportsTeam)
                  }
               }
           }
        }

    companion object {
        private const val TAG = "FIMTOWN|NewsAdapter"
    }

}









