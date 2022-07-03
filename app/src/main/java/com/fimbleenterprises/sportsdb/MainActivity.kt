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
import androidx.core.view.MenuProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.fimbleenterprises.sportsdb.data.model.SportsTeam
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

    // region Dagger/Hilt dependency injections
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

        // Add menu items without overriding methods in the Activity
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onPrepareMenu(menu: Menu) {
                menu.findItem(R.id.action_purchase_pro)?.isVisible = !viewModel.userIsPro.get() == true
                super.onPrepareMenu(menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.action_privacy_policy -> {
                        val url = getString(R.string.privacy_policy_url)
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(browserIntent)
                    }
                    R.id.action_purchase_pro -> {
                        // viewModel.showOptionToBuyPro(supportFragmentManager)
                        showOptionToBuyPro()
                    }
                }
                return false
            }
        })


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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        return super.onKeyDown(keyCode, event)
    }

    // region Deprecated menu overrides
    // https://developer.android.com/jetpack/androidx/releases/fragment#1.5.0-alpha04

    // I should just delete this but after using these overrides for so many years I cannot
    // bring myself to see it go away fully just yet :)

    /*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        // Hide get pro menu option if user is already one
        menu.findItem(R.id.action_purchase_pro)?.isVisible = !viewModel.userIsPro.get() == true
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_privacy_policy -> {
                val url = getString(R.string.privacy_policy_url)
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }
            R.id.action_purchase_pro -> {
                // viewModel.showOptionToBuyPro(supportFragmentManager)
                showOptionToBuyPro()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    */

    // endregion

    /**
     * Gives the user the ability to purchase the pro version.
     */
    fun showOptionToBuyPro(team: SportsTeam) {
        val bundle = Bundle()
        bundle.putSerializable("team", team)
        PurchaseProDialogFragment(object : PurchaseProDialogFragment.OnPurchaseDecisionListener {
            // We can add this additional team to the followed teams
            override fun purchased() {
                MyApp.AppPreferences.isPro = true
                viewModel.run {
                    userIsPro.set(MyApp.AppPreferences.isPro)
                    followTeam(team)
                }
            }

            // We can only replace the current followed team with this selected team.
            override fun declined() {
                viewModel.run {
                    unFollowAllTeams()
                    followTeam(team)
                }
                supportFragmentManager.getFragment(bundle, SportsdbViewModel.PURCHASE_DIALOG_TAG)?.onDestroy()
            }
        }).show(supportFragmentManager, SportsdbViewModel.PURCHASE_DIALOG_TAG)
    }

    /**
     * Gives the user the ability to purchase the pro version.
     */
    private fun showOptionToBuyPro() {
        PurchaseProDialogFragment(object :
            PurchaseProDialogFragment.OnPurchaseDecisionListener {
            override fun purchased() {
                MyApp.AppPreferences.isPro = true
                viewModel.run { userIsPro.set(MyApp.AppPreferences.isPro) }
            }
            override fun declined() { }
        }).show(supportFragmentManager, null)
    }

    companion object {
        private const val TAG = "FIMTOWN|MainActivity"
    }

    init {
        Log.i(TAG, "Initialized:MainActivity")
    }

}