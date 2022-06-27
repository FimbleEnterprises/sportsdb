package com.fimbleenterprises.sportsdb

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.billingclient.api.SkuDetails
import com.fimbleenterprises.sportsdb.databinding.ActivityMainBinding
import com.fimbleenterprises.sportsdb.presentation.adapter.GameResultsAdapter
import com.fimbleenterprises.sportsdb.presentation.adapter.LeaguesAdapter
import com.fimbleenterprises.sportsdb.presentation.adapter.ScheduledGamesAdapter
import com.fimbleenterprises.sportsdb.presentation.adapter.TeamsAdapter
import com.fimbleenterprises.sportsdb.presentation.viewmodel.SportsdbViewModel
import com.fimbleenterprises.sportsdb.presentation.viewmodel.SportsdbViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // region DAGGER INJECTIONS
    @Inject
    lateinit var sportsdbViewModelFactory: SportsdbViewModelFactory
    @Inject
    lateinit var teamsAdapter: TeamsAdapter
    @Inject
    lateinit var scheduledGamesAdapter: ScheduledGamesAdapter
    @Inject
    lateinit var gameResultsAdapter: GameResultsAdapter
    @Inject
    lateinit var leaguesAdapter: LeaguesAdapter
    @Inject
    lateinit var myAnalytics: MyAnalytics
    @Inject
    lateinit var myMyBillingWrapper: MyBillingWrapper
    // endregion

    lateinit var viewModel: SportsdbViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Wire up the bottom navigation menu
        navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainer.id) as NavHostFragment
        navController = navHostFragment.navController
        this.binding.bnvMain.setupWithNavController(navController)
        this.binding.bnvMain.setOnItemSelectedListener {
            if (it.itemId == R.id.myTeamsFragment) {
                navController.navigate(R.id.action_goto_my_teams)
            } else if (it.itemId == R.id.findTeamsFragment) {
                navController.navigate(R.id.action_goto_select_team)
            }
            true
        }

        // Get the vm for the sports side of the app
        viewModel = ViewModelProvider(this, sportsdbViewModelFactory)[SportsdbViewModel::class.java]

        // log analytics
        myAnalytics.logMainActivityEvent()


    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        super.onResume()

        // Check if user is not following any teams
        viewModel.getFollowedTeams().observe(this) {
            if (MyApp.AppPreferences.currentLeague.isNullOrEmpty() && MyApp.AppPreferences.currentTeam == null) {
                // User has no saved teams - let them rectify that
                Log.i(TAG, "-=MainActivity:onResume NO LEAGUE OR TEAM SELECTED!  MUST FIX! =-")
                navController.navigate(R.id.action_goto_select_league)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_privacy_policy -> {
                val url = "https://www.freeprivacypolicy.com/live/226786c7-4348-4919-bbb7-e0313f06562b"
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "FIMTOWN|MainActivity"
    }

    init {
        Log.i(TAG, "Initialized:MainActivity")
    }

}