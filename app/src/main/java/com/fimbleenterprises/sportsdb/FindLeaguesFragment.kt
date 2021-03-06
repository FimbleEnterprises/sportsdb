package com.fimbleenterprises.sportsdb

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fimbleenterprises.sportsdb.data.model.League
import com.fimbleenterprises.sportsdb.databinding.FragmentFindLeaguesBinding
import com.fimbleenterprises.sportsdb.presentation.adapter.LeaguesAdapter
import com.fimbleenterprises.sportsdb.presentation.viewmodel.SportsdbViewModel

class FindLeaguesFragment : Fragment() {

    private lateinit var binding: FragmentFindLeaguesBinding
    private lateinit var adapter: LeaguesAdapter
    private lateinit var viewmodel: SportsdbViewModel
    private var isLoading = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_leagues, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFindLeaguesBinding.bind(view)
        (activity as MainActivity).leaguesAdapter.also { adapter = it }
        (activity as MainActivity).viewModel.also { viewmodel = it }
        initRecyclerView()
        initSearchView()
        getAllLeagues()

    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).title = getString(R.string.choose_league)
    }

    /**
     * Set on click listener to the adapter and bind the adapter to the recyclerview
     */
    private fun initRecyclerView() {

        adapter.setOnItemClickListener {
            MyApp.AppPreferences.currentLeague = it.strLeague
            findNavController().navigate(
                R.id.action_goto_select_team
            )
        }

        binding.recyclerview.apply {
            adapter = this@FindLeaguesFragment.adapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    /**
     * Sets up the search view and adds some simple prereqs prior to searching
     */
    private fun initSearchView() {
        binding.svLeagues.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(usertext: String?): Boolean {
                if (usertext != null && usertext.length > 2) {
                    filterLeagues(usertext)
                } else {
                    getAllLeagues()
                }
                return false // false = Default action performed (hide keyboard in this case)
            }

            override fun onQueryTextChange(usertext: String?): Boolean {
                if (usertext == null || usertext.isEmpty()) {
                    getAllLeagues()
                } else if (usertext.length > 1) {
                    filterLeagues(usertext)
                }
                return false // false = Default action performed (show hints in this case)
            }
        })
        binding.svLeagues.isIconified = false
        binding.svLeagues.queryHint = getString(R.string.league_query_hint)
    }

    /**
     * Queries the API for all leagues
     */
    private fun getAllLeagues() {

        viewmodel.getAllLeagues()
        viewmodel.allLeagues.observe(viewLifecycleOwner) { response ->
            when (response) {
                is com.fimbleenterprises.sportsdb.util.Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { adapter.differ.submitList(it.leagues) }
                }
                is com.fimbleenterprises.sportsdb.util.Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { it: String ->
                        Toast.makeText(activity, "An error occurred : $it", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is com.fimbleenterprises.sportsdb.util.Resource.Loading -> {
                    showProgressBar()
                }

            }
        }
    }

    /**
     * Uses .contains on a few string properties of the League object and returns a list of matches.
     * We just rebind the adapter's differ bucket with this list instead of the full leagues list.
     */
    private fun filterLeagues(query:String) {

        viewmodel.filterAllLeagues(query)
        viewmodel.filteredLeagues.observe(viewLifecycleOwner) { response: List<League> ->
            adapter.differ.submitList(response)
        }

        // Add a boring event to Firebase analytics
        (activity as MainActivity).myAnalytics.logSearchedForLeagueEvent(query)
    }

    private fun showProgressBar(){
        isLoading = true
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        isLoading = false
        binding.progressBar.visibility = View.INVISIBLE
    }

    companion object {
        private const val TAG = "FIMTOWN|FindLeaguesFragment"
    }

    init {
        Log.i(TAG, "Initialized:FindLeaguesFragment")
    }

}