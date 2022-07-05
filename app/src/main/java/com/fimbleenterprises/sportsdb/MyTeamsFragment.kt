package com.fimbleenterprises.sportsdb

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fimbleenterprises.sportsdb.data.model.SportsTeam
import com.fimbleenterprises.sportsdb.databinding.FragmentListTeamsBinding
import com.fimbleenterprises.sportsdb.presentation.adapter.TeamsAdapter
import com.fimbleenterprises.sportsdb.presentation.viewmodel.SportsdbViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

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

        // Query the database for saved teams and observe the results
        viewmodel.run {
            getFollowedTeams()
            followedTeams.observe(viewLifecycleOwner) { myteams ->
                if (!myteams.isNullOrEmpty()) {
                    Log.i(TAG, "getMyTeams: Retrieved ${myteams.size} followed teams")
                    with(teamsadapter) { differ.submitList(myteams) }
                    View.GONE.also {
                        binding.txtNoTeams.visibility = it
                        binding.btnNoTeams.visibility = it
                    }

                } else {
                    View.GONE.also { binding.txtNoTeams.visibility = it }
                    View.VISIBLE.also { binding.btnNoTeams.visibility = it }
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
            }
        }

        // Pop the backstack - I like this to be the "home" fragment.
        findNavController().popBackStack(R.id.action_goto_my_teams, true)
        Log.i(TAG, "-=MyTeamsFragment:onViewCreated Popped backstack. =-")

        // region OPTIONS MENU
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.my_teams_menu, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
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
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        // endregion
    }

    private fun initRecyclerView() {
        binding.rvTeams.apply {
            // Since we share this adapter with the FindTeams fragment it can sometimes be populated
            // with teams received from the API when we arrive.  That makes for a janky switch when
            // the adapter is re-populated with our db results.  So we clear it first.
            teamsadapter.differ.submitList(null)
            teamsadapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            adapter = this@MyTeamsFragment.teamsadapter
            layoutManager = LinearLayoutManager(activity)
        }

        /**
         * Short click we just go to the view scores fragment
         */
        teamsadapter.setOnItemClickListener {
            MyApp.AppPreferences.currentTeam = it
            val bundle = Bundle()
            bundle.putString("team", Gson().toJson(it))
            findNavController().navigate(R.id.action_goto_view_scores, bundle)
        }

        /**
         * On long click we offer the opportunity to un-follow a team
         */
        teamsadapter.setOnItemLongClickListener { selectedTeam: SportsTeam ->

            // build alert dialog containing the user's options
            val dialogBuilder = AlertDialog.Builder(activity)
            dialogBuilder
                .setCancelable(false)
                .setTitle(getString(R.string.dialog_unfollow_team))
                .setPositiveButton(":Yes") { _, _ ->
                    viewmodel.unFollowTeam(selectedTeam)
                    viewmodel.deleteCount.observe(viewLifecycleOwner) {
                       val snackbar = Snackbar.make(
                            binding.rvTeams,
                            getString(R.string.unfollowed),
                           8000
                        ).setAction("Undo") {
                                // Undo should be as easy as re-following the team again.
                                viewmodel.followTeam(selectedTeam)
                        }
                        snackbar.show()
                    }
                }
                // negative button text and action
                .setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }

            val alert = dialogBuilder.create()
            alert.show()
        }
    }

    companion object {
        private const val TAG = "FIMTOWN|MyTeamsFragment"
    }

    init {
        Log.i(TAG, "Initialized:MyTeamsFragment")
    }

}