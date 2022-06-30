package com.fimbleenterprises.sportsdb

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fimbleenterprises.sportsdb.data.model.SportsTeam
import com.fimbleenterprises.sportsdb.databinding.FragmentViewSelectedTeamsScoresBinding
import com.fimbleenterprises.sportsdb.presentation.adapter.GameResultsAdapter
import com.fimbleenterprises.sportsdb.presentation.adapter.ScheduledGamesAdapter
import com.fimbleenterprises.sportsdb.presentation.viewmodel.SportsdbViewModel

class ViewSelectedTeamsScoresFragment : Fragment() {

    private lateinit var binding: FragmentViewSelectedTeamsScoresBinding
    private lateinit var viewModel: SportsdbViewModel
    private lateinit var scheduledGamesAdapter: ScheduledGamesAdapter
    private lateinit var gameResultsAdapter: GameResultsAdapter
    private lateinit var selectedTeam: SportsTeam

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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
        viewModel= (activity as MainActivity).viewModel
        scheduledGamesAdapter = (activity as MainActivity).scheduledGamesAdapter
        gameResultsAdapter = (activity as MainActivity).gameResultsAdapter

        val args : ViewSelectedTeamsScoresFragmentArgs by navArgs()
        selectedTeam = args.team
        Log.i(TAG, "-=ViewSelectedTeamsScoresFragment:onViewCreated NavArgs passed" +
                ": ${selectedTeam.strTeam} =-")

        findNavController().navigate(R.id.myTeamsFragment)
        Toast.makeText(context, "No team selected!", Toast.LENGTH_SHORT).show()

        initRecyclerViews()
        buildTeamDetails()

        // I cannot think of a better way than to go scorched earth and call notifyDataSetChanged()
        // when this option is changed.  It's infrequent enough that performance should never be a
        // concern.  Perhaps I don't know enough about using differ() in the adapter?
        viewModel.showsummariesinboxscore.observe(viewLifecycleOwner) {
            gameResultsAdapter.notifyDataSetChanged()
        }

        (activity as MainActivity).apply {
            this.title = selectedTeam.strTeam

            // log analytics
            myAnalytics.logViewedTeamScoresEvent(selectedTeam.strTeam)
        }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.view_scores_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        viewModel.showsummariesinboxscore.value?.let {
            menu.findItem(R.id.action_show_summaries_in_boxscore)?.setChecked(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_show_summaries_in_boxscore -> {
                MyApp.AppPreferences.showSummariesInBoxscore = !item.isChecked
                viewModel.showsummariesinboxscore.value = !item.isChecked
            }
        }
        return true
    }

    private fun buildTeamDetails() {

        // Show team picture using Glide
        Glide.with(binding.imageView.context).
        load(selectedTeam.strTeamLogo).into(binding.imageView)

        if (selectedTeam.strDescriptionEN == null) {
            binding.txtAbout.text = getString(R.string.no_desc_found)
        } else {
            binding.txtDescription.text = selectedTeam.strDescriptionEN
        }
        getNextFiveGames()
        getLastFiveGames()
    }

    private fun initRecyclerViews() {

        binding.recyclerviewNext5.apply {
            adapter = scheduledGamesAdapter
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(activity)
        }

        binding.recyclerviewLast5.apply {
            adapter = gameResultsAdapter
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(activity)
        }

        // Setup a itemclicklistener for clicked game results.  Probably only be applicable if the
        // game has YouTube highlights.
        gameResultsAdapter.setOnItemClickListener {
            if (it.strVideo != null) {
                val bundle = Bundle()
                bundle.putString("url", it.strVideo)
                bundle.putString("teamname", it.strEvent)
                findNavController().navigate(
                    R.id.viewHighlightsFragment
                    , bundle
                )
            }
        }
    }

    private fun getLastFiveGames() {
        showLastFiveProgressBar()

        // Clear list
        gameResultsAdapter.differ.submitList(null)

        viewModel.getLastFiveGames(selectedTeam.idTeam)
        viewModel.lastFiveEvents.observe(viewLifecycleOwner) { response ->
            when (response) {
                is com.fimbleenterprises.sportsdb.util.Resource.Success -> {
                    hideLastFiveProgressBar()
                    response.data?.let {
                        if (it.gameResults != null) {
                            gameResultsAdapter.differ.submitList(it.gameResults)
                            binding.last5Label.visibility = View.VISIBLE
                        } else {
                            binding.last5Label.visibility = View.GONE
                            gameResultsAdapter.differ.submitList(null)
                        }
                    }
                }
                is com.fimbleenterprises.sportsdb.util.Resource.Error -> {
                    hideLastFiveProgressBar()
                    response.message?.let {
                        Toast.makeText(activity, "An error occurred : $it", Toast.LENGTH_LONG)
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

    private fun getNextFiveGames() {
        showNextFiveProgressBar()

        // Clear list
        scheduledGamesAdapter.differ.submitList(null)

        viewModel.getNextFiveGames(selectedTeam.idTeam)
        viewModel.scheduledEvents.observe(viewLifecycleOwner) { response ->
            when (response) {
                is com.fimbleenterprises.sportsdb.util.Resource.Success -> {
                    hideNextFiveProgressBar()
                    response.data?.let { it ->
                        Log.i(TAG, "StartFragment|getNextFiveGames(args:[]")
                        if (it.scheduledGames != null) {
                            scheduledGamesAdapter.differ.submitList(it.scheduledGames) {
                                Log.i(
                                    TAG,
                                    "-= differ.submitList callback: ${scheduledGamesAdapter.differ.currentList.size} items in list =-"
                                )
                            }
                            binding.next5label.visibility = View.VISIBLE
                        } else {
                            binding.next5label.visibility = View.GONE
                            scheduledGamesAdapter.differ.submitList(null)
                        }
                    }
                }
                is com.fimbleenterprises.sportsdb.util.Resource.Error -> {
                    hideNextFiveProgressBar()
                    response.message?.let {
                        Toast.makeText(activity, "An error occurred : $it", Toast.LENGTH_LONG)
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