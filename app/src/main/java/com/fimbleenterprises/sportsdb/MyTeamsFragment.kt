package com.fimbleenterprises.sportsdb

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fimbleenterprises.sportsdb.databinding.FragmentListTeamsBinding
import com.fimbleenterprises.sportsdb.presentation.adapter.TeamsAdapter
import com.fimbleenterprises.sportsdb.presentation.viewmodel.SportsdbViewModel
import com.google.android.material.snackbar.Snackbar

class MyTeamsFragment : Fragment() {

    private lateinit var teamsadapter: TeamsAdapter
    private lateinit var viewmodel: SportsdbViewModel
    private lateinit var binding: FragmentListTeamsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_teams, container, false)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).title = "My Followed Teams"

        viewmodel.followedTeams.observe(viewLifecycleOwner) {
            when {
                it.isNullOrEmpty() -> {
                    binding.btnNoTeams.visibility = View.VISIBLE
                    binding.btnNoTeams.setTextColor(Color.BLUE)
                    binding.btnNoTeams.setOnClickListener {
                        if (MyApp.AppPreferences.currentLeague == null) {
                            findNavController().navigate(
                                R.id.action_goto_select_league
                            )
                        } else if (MyApp.AppPreferences.currentTeam == null) {
                            findNavController().navigate(
                                R.id.action_goto_select_team
                            )
                        }
                    }
                }
                else -> {
                    binding.btnNoTeams.visibility = View.GONE
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Attach viewbinding
        binding = FragmentListTeamsBinding.bind(view)

        // Grab refs to the viewmodel and adapter
        teamsadapter = (activity as MainActivity).teamsAdapter
        viewmodel = (activity as MainActivity).viewModel

        // Don't need search for this fragment
        binding.searchlayout.visibility = View.GONE
        // Don't need league changer here either
        binding.txtChangeLeague.visibility = View.GONE

        // Setup then use the recyclerview
        initRecyclerView()
        getMyFollowedTeams()

        // Enable our options menu
        setHasOptionsMenu(true)

        // Pop the backstack - I like this to be the "home" fragment.
        findNavController().popBackStack(R.id.action_goto_my_teams, true)
        Log.i(TAG, "-=MyTeamsFragment:onViewCreated Popped backstack. =-")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.my_teams_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_unfollow_all -> {
                viewmodel.unFollowAllTeams()
                findNavController().navigate(
                    R.id.action_goto_select_league
                )
            }
        }
        return true
    }

    private fun initRecyclerView() {
        binding.rvNews.apply {
            // Since we share this adapter with the FindTeams fragment it can sometimes be populated
            // with teams received from the API when we arrive.  That makes for a janky switch when
            // the adapter is re-populated with our db results.
            teamsadapter.differ.submitList(null)
            adapter = teamsadapter
            layoutManager = LinearLayoutManager(activity)
        }

        /**
         * Short click we just go to the view scores fragment
         */
        teamsadapter.setOnItemClickListener {
            MyApp.AppPreferences.currentTeam = it
            findNavController().navigate(R.id.action_goto_view_scores)
        }

        /**
         * On long click we offer the opportunity to un-follow a team
         */
        teamsadapter.setOnItemLongClickListener {
            val selectedTeam = it
            // build alert dialog
            val dialogBuilder = AlertDialog.Builder(activity)
            dialogBuilder.setMessage(getString(R.string.dialog_unfollow_team))
                .setCancelable(false)
                .setPositiveButton(":Yes") { _, _ ->
                    viewmodel.unFollowTeam(it)
                    viewmodel.deletedTeamCount.observe(viewLifecycleOwner) {
                        Snackbar.make(
                            binding.rvNews,
                            getString(R.string.team_unfollowed),
                            Snackbar.LENGTH_LONG
                        )
                            .setAction("Undo") {
                                // Undo should be as easy as re-following the team again.
                                viewmodel.followTeam(selectedTeam)
                            }
                            .show()
                    }
                }
                // negative button text and action
                .setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("AlertDialogExample")
            // show alert dialog
            alert.show()
        }
    }

    private fun getMyFollowedTeams() {
        viewmodel.getFollowedTeams().observe(viewLifecycleOwner) {
            if (it != null) {
                Log.i(TAG, "getMyTeams: Retrieved ${it.size} followed teams")
                teamsadapter.differ.submitList(it)
            }
        }
    }

    companion object {
        private const val TAG = "FIMTOWN|MyTeamsFragment"
    }

    init {
        Log.i(TAG, "Initialized:MyTeamsFragment")
    }
}