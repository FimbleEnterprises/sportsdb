package com.fimbleenterprises.sportsdb

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fimbleenterprises.sportsdb.data.model.SportsTeam
import com.fimbleenterprises.sportsdb.databinding.FragmentViewSelectedTeamsScoresBinding
import com.fimbleenterprises.sportsdb.presentation.adapter.GameResultsAdapter
import com.fimbleenterprises.sportsdb.presentation.adapter.ScheduledGamesAdapter
import com.fimbleenterprises.sportsdb.presentation.viewmodel.SportsdbViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class ViewSelectedTeamsScoresFragment : Fragment() {

    private lateinit var binding: FragmentViewSelectedTeamsScoresBinding
    private lateinit var viewmodel: SportsdbViewModel
    private lateinit var scheduledAdapter: ScheduledGamesAdapter
    private lateinit var resultsAdapter: GameResultsAdapter
    private var team: SportsTeam? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_selected_teams_scores, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentViewSelectedTeamsScoresBinding.bind(view)
        viewmodel= (activity as MainActivity).viewModel
        scheduledAdapter = (activity as MainActivity).scheduledGamesAdapter
        resultsAdapter = (activity as MainActivity).gameResultsAdapter

        // Get the team selected by virtue of navArgs() created in the nav_graph
        val args : ViewSelectedTeamsScoresFragmentArgs by navArgs()

        // This should really never be null but just in case we bail.
        if (args.team == null) {
            findNavController().navigate(R.id.myTeamsFragment)
            Toast.makeText(context, getString(R.string.no_team_selected), Toast.LENGTH_SHORT).show()
            return
        }

        val strTeam = args.team!!
        team = Gson().fromJson(strTeam, SportsTeam::class.java)
        Log.i(TAG, "-=ViewSelectedTeamsScoresFragment:onViewCreated|Arg: ${team?.strTeam} =-")

        // Prepare the lists
        initRecyclerViews()

        // Retrieve and display this team's data
        showTeamDetails()

        // Wreck the game results list and rebuild if user opts to see detailed box scores.
        // I cannot think of a better way than to call notifyDataSetChanged() when this option
        // is changed.  It's infrequent enough that performance should never be a concern.  Perhaps
        // I don't know enough about using differ() in the adapter?
        viewmodel.showsummariesinboxscore.observe(viewLifecycleOwner) {
            resultsAdapter.notifyDataSetChanged()
        }

        // Set the title and log an event to Firebase analytics
        (activity as MainActivity).apply {
            this.title = team!!.strTeam
            myAnalytics.logViewedTeamScoresEvent(team!!.strTeam)
        }

        // region OPTIONS MENU
        /**
         * The way we interact with the options menu has changed in androidx.fragment#1.5.0-alpha04 **/
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.view_scores_menu, menu)
            }

            override fun onPrepareMenu(menu: Menu) {
                viewmodel.apply {
                    showsummariesinboxscore.value?.let {
                        menu.findItem(R.id.action_show_summaries_in_boxscore)?.setChecked(it)
                    }
                    super.onPrepareMenu(menu)
                }
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.action_show_summaries_in_boxscore -> {
                        MyApp.AppPreferences.showSummariesInBoxscore = !item.isChecked
                        viewmodel.apply { showsummariesinboxscore.value = !item.isChecked }
                    }
                    R.id.action_unfollow_team -> {
                        performUnfollowTeam()
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        // endregion

    }

    override fun onResume() {
        super.onResume()

        if (MyApp.AppPreferences.currentLeague == null) {
            Log.i(
                TAG,
                "-=ViewSelectedTeamsScoresFragment:onResume NO LEAGUE!  MUST CHOOSE LEAGUE =-"
            )
            findNavController().navigate(R.id.action_goto_select_league)
        }

    }

    private fun performUnfollowTeam() {
        // build alert dialog
        AlertDialog.Builder(activity)
            .setCancelable(false)
            .setTitle(getString(R.string.dialog_unfollow_team))
            .setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewmodel.apply {
                    unFollowTeam(team!!)
                    // deletedTeamCount is observed to serve as confirmation the db op has completed.
                    deleteCount.observe(viewLifecycleOwner) {
                        if (it > 0) {
                            // Give user the ability to undo the unfollow
                            Snackbar
                                .make(binding.root, getString(R.string.unfollowed), 8000)
                                .setAction(getString(R.string.undo)) {
                                    // Undo should be as easy as re-following the team again.
                                    viewmodel.followTeam(team!!)
                                }
                                .show()

                            // Navigate to my teams since we are no longer interested in this one.
                            findNavController().navigate(
                                R.id.action_goto_my_teams
                            )
                        }
                    }
                }
            }
        .show()
    }

    private fun showTeamDetails() {

        // Show team picture using Glide
        Glide.with(binding.imageView.context).
        load(team?.strTeamLogo).into(binding.imageView)

        if (team!!.strDescriptionEN == null) {
            binding.txtAbout.text = getString(R.string.no_desc_found)
        } else {
            binding.txtDescription.text = team!!.strDescriptionEN
        }

        getNextFiveGames()
        getLastFiveGames()
    }

    /**
     * Sets up the two adapters (past games and future games)
     */
    private fun initRecyclerViews() {

        binding.recyclerviewNext5.apply {
            adapter = this@ViewSelectedTeamsScoresFragment.scheduledAdapter
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(activity)
        }

        binding.recyclerviewLast5.apply {
            adapter = this@ViewSelectedTeamsScoresFragment.resultsAdapter
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(activity)
        }

        // If there are YouTube highlights then send the user to YouTube to view them on item click.
        resultsAdapter.setOnItemClickListener {
            if (!it.strVideo.isNullOrBlank()) {
                try {
                    val url = it.strVideo
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                } catch (e:Exception) {
                    Toast.makeText(
                        activity,
                        getString(R.string.highlights_fail, e.localizedMessage),
                        Toast.LENGTH_SHORT)
                    .show()
                }
            }
        }
    }

    /**
     * Calls the sportsdb API to the get the last five games played
     */
    private fun getLastFiveGames() {
        showLastFiveProgressBar()

        // Clear list
        resultsAdapter.differ.submitList(null)

        viewmodel.getLastFiveGames(team!!.idTeam)
        viewmodel.lastFiveEvents.observe(viewLifecycleOwner) { response ->
            when (response) {
                is com.fimbleenterprises.sportsdb.util.Resource.Success -> {
                    hideLastFiveProgressBar()
                    response.data?.let {
                        if (it.gameResults != null) {
                            resultsAdapter.differ.submitList(it.gameResults)
                            binding.last5Label.visibility = View.VISIBLE
                        } else {
                            binding.last5Label.visibility = View.GONE
                            resultsAdapter.differ.submitList(null)
                        }
                    }
                }
                is com.fimbleenterprises.sportsdb.util.Resource.Error -> {
                    hideLastFiveProgressBar()
                    response.message?.let { message: String ->
                        Toast.makeText(
                            activity,
                            getString(R.string.api_error, message), Toast.LENGTH_LONG)
                        .show()
                    }
                    binding.last5Label.visibility = View.GONE
                }
                is com.fimbleenterprises.sportsdb.util.Resource.Loading -> {
                    showLastFiveProgressBar()
                }
            }
        }
    }

    /**
     * Calls the sportsdb API to get the next five games to be played.
     */
    private fun getNextFiveGames() {
        showNextFiveProgressBar()

        // Clear list
        scheduledAdapter.differ.submitList(null)

        // Call API
        viewmodel.getNextFiveGames(team!!.idTeam)

        // Observe the results and do things with them
        viewmodel.scheduledEvents.observe(viewLifecycleOwner) { response ->
            when (response) {
                is com.fimbleenterprises.sportsdb.util.Resource.Success -> {
                    hideNextFiveProgressBar()
                    response.data?.let { it ->
                        Log.i(TAG, "StartFragment|getNextFiveGames(args:[]")
                        if (it.scheduledGames != null) {
                            scheduledAdapter.differ.submitList(it.scheduledGames) {
                                Log.i(
                                    TAG,
                                    "-= differ.submitList callback: ${scheduledAdapter.differ.currentList.size} items in list =-"
                                )
                            }
                            binding.next5label.visibility = View.VISIBLE
                        } else {
                            binding.next5label.visibility = View.GONE
                            scheduledAdapter.differ.submitList(null)
                        }
                    }
                }
                is com.fimbleenterprises.sportsdb.util.Resource.Error -> {
                    hideNextFiveProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, getString(R.string.api_error, message), Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is com.fimbleenterprises.sportsdb.util.Resource.Loading -> {
                    showNextFiveProgressBar()
                }

            }
        }
    }

    private fun showNextFiveProgressBar(){
        binding.progressBarNext5.visibility = View.VISIBLE
    }

    private fun hideNextFiveProgressBar(){
        binding.progressBarNext5.visibility = View.INVISIBLE
    }

    private fun showLastFiveProgressBar(){
        binding.progressBarLast5.visibility = View.VISIBLE
    }

    private fun hideLastFiveProgressBar(){
        binding.progressBarLast5.visibility = View.INVISIBLE
    }

    companion object {
        private const val TAG = "FIMTOWN|ViewSelectedTeamsScoresFragment"
    }

    init {
        Log.i(TAG, "Initialized:ViewSelectedTeamsScoresFragment")
    }
}