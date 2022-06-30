package com.fimbleenterprises.sportsdb

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AbsListView
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fimbleenterprises.sportsdb.databinding.FragmentListTeamsBinding
import com.fimbleenterprises.sportsdb.presentation.adapter.TeamsAdapter
import com.fimbleenterprises.sportsdb.presentation.viewmodel.SportsdbViewModel

class FindTeamsFragment : Fragment() {

    private  lateinit var viewmodel: SportsdbViewModel
    private lateinit var teamsAdapter: TeamsAdapter
    private lateinit var binding: FragmentListTeamsBinding


    private var isLoading = false
    private var isScrolling = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_teams, container, false)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).title = getString(R.string.please_choose_team)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListTeamsBinding.bind(view)
        viewmodel = (activity as MainActivity).viewModel
        teamsAdapter = (activity as MainActivity).teamsAdapter

        // If there is no league selected then we should rectify that if possible.
        when (MyApp.AppPreferences.currentLeague) {
            null -> {
                binding.txtChangeLeague.text = getString(R.string.choose_league)
            }
            else -> {
                binding.txtChangeLeague.text = "${MyApp.AppPreferences.currentLeague}"
            }
        }

        // Style the textview to look like a hyperlink
        binding.txtChangeLeague.setTextColor(Color.BLUE)
        binding.txtChangeLeague.paintFlags =
            binding.txtChangeLeague.paintFlags.plus(Paint.UNDERLINE_TEXT_FLAG)

        binding.txtChangeLeague.setOnClickListener {
            findNavController().navigate(
                R.id.action_goto_select_league
            )
        }

        initSearchView()
        initRecyclerView()
        getAllTeams()

        // Double check if user has purchased pro
        viewmodel.queryUserPurchasedPro()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.find_teams_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_change_league -> {
                findNavController().navigate(
                    R.id.action_goto_select_league
                )
            }
        }
        return true
    }

    @Suppress("SimplifyBooleanWithConstants")
    private fun initRecyclerView() {
        binding.rvNews.apply {
            // Since we share this adapter with the MyTeams fragment it can and will be populated
            // with saved teams from the db when we arrive.  That makes for a janky switch when the
            // adapter is re-populated with our API results.
            this@FindTeamsFragment.teamsAdapter.differ.submitList(null)

            // Remember scroll position throughout lifecycle
            this@FindTeamsFragment.teamsAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

            adapter = this@FindTeamsFragment.teamsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@FindTeamsFragment.onScrollListener)

            teamsAdapter.setOnItemClickListener { clickedTeam ->
                // Retrieve followed teams from Room db asynchronously
                viewmodel.getFollowedTeams().observe(viewLifecycleOwner) { teamList ->

                    // Bundle an arg for the view cores frag
                    val bundle = Bundle()
                    bundle.putSerializable("team", clickedTeam)

                    // If user is following a team we need to check if they have the pro version
                    // allowing them to follow a second, third etc.
                    if (teamList.isNotEmpty()) {
                        viewmodel.run {
                            if (viewmodel.userIsPro.get() == true) {
                                followTeam(clickedTeam)
                                findNavController().navigate(R.id.viewSelectedTeamsScoresFragment, bundle)
                            } else {
                                viewmodel.showOptionToBuyPro(clickedTeam, childFragmentManager)
                            }
                        }
                    } else {
                        // User is not following any teams and should be allowed to follow this one.
                        viewmodel.followTeam(clickedTeam)
                        findNavController().navigate(
                            R.id.action_goto_view_scores, bundle
                        )
                    }
                }
            }
        }
    }

    /**
     * Sets up the search view
     */
    private fun initSearchView() {
        binding.svTeams.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(usertext: String?): Boolean {
                if (usertext != null && usertext.length > 2) {
                    searchTeams(usertext)
                } else {
                    getAllTeams()
                }
                return false // false= Default action performed (hide keyboard in this case)
            }

            override fun onQueryTextChange(usertext: String?): Boolean {
                if (usertext == null || usertext.isEmpty()) {
                    getAllTeams()
                }
                return false // false = Default action performed (show hints in this case)
            }
        })
        binding.svTeams.setOnCloseListener {
            initRecyclerView()
            getAllTeams()
            false
        }
        binding.svTeams.queryHint = getString(R.string.search_teams_hint, MyApp.AppPreferences.currentLeague)
        binding.svTeams.isIconified = false
    }

    /**
     * Queries the API for all teams in the currently selected league.
     */
    private fun getAllTeams() {

        // Clear current list - if search yields zero results the existing list will remain and this
        // would be very confusing.
        teamsAdapter.differ.submitList(null)

        MyApp.AppPreferences.currentLeague?.let { viewmodel.getAllTeamsByLeague(it) }
        viewmodel.allTeamsAPIResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is com.fimbleenterprises.sportsdb.util.Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { it ->
                        if (it.sportsTeams.isNullOrEmpty()) {
                            binding.resultsContainer.visibility = View.VISIBLE
                            teamsAdapter.differ.submitList(null)
                        } else {
                            teamsAdapter.differ.submitList(it.sportsTeams.toList())
                            binding.resultsContainer.visibility = View.GONE
                        }
                    }
                }
                is com.fimbleenterprises.sportsdb.util.Resource.Error -> {
                    hideProgressBar()
                    binding.resultsContainer.visibility = View.GONE
                    response.message?.let {
                        Toast.makeText(activity, "An error occurred : $it", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is com.fimbleenterprises.sportsdb.util.Resource.Loading -> {
                    showProgressBar()
                    binding.resultsContainer.visibility = View.GONE
                }
            }
        }
    }

    /**
     * Queries the API for teams with a name containing the search argument.
     */
    private fun searchTeams(query:String) {

        viewmodel.searchTeams(query)
        viewmodel.searchedTeams.observe(viewLifecycleOwner) { response ->
            when (response) {
                is com.fimbleenterprises.sportsdb.util.Resource.Success -> {

                    hideProgressBar()
                    response.data?.let {
                        teamsAdapter.differ.submitList(it.sportsTeams)
                    }
                }
                is com.fimbleenterprises.sportsdb.util.Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(activity, "An error occurred : $it", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is com.fimbleenterprises.sportsdb.util.Resource.Loading -> {
                    showProgressBar()
                }
            }
        }

        // Log analytics
        (activity as MainActivity).myAnalytics.logSearchedForTeamEvent(query)
    }

    private fun showProgressBar(){
        isLoading = true
        binding.progressBarNext5.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        isLoading = false
        binding.progressBarNext5.visibility = View.INVISIBLE
    }

    /**
     * Not implemented but could be handy if we ever needed to introduce pagination
     */
    private val onScrollListener = object : RecyclerView.OnScrollListener() {

       override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
           super.onScrollStateChanged(recyclerView, newState)
           if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
               isScrolling = true
           }
       }

       @Suppress("UNUSED_VARIABLE")
       override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
           super.onScrolled(recyclerView, dx, dy)
           val layoutManager = binding.rvNews.layoutManager as LinearLayoutManager
           val sizeOfTheCurrentList = layoutManager.itemCount
           val visibleItems = layoutManager.childCount
           val topPosition = layoutManager.findFirstVisibleItemPosition()
           val hasReachedEnd = topPosition+visibleItems >= sizeOfTheCurrentList
       }
    }

    companion object {
        private const val TAG = "FIMTOWN|FindTeamsFragment"
    }

    init {
        Log.i(TAG, "Initialized:FindTeamsFragment")
    }
}
















