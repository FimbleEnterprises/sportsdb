package com.fimbleenterprises.sportsdb

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.fimbleenterprises.sportsdb.data.model.SportsTeam
import com.google.gson.Gson
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Singleton

@HiltAndroidApp
class MyApp : Application() {



    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
        /**
         * This is deprecated and will be auto-initialized unless you opt out using a meta-data
         * line in the manifest.
         */
        /*FacebookSdk.sdkInitialize(applicationContext) {
            Log.i(TAG, "-= MyApp:onCreate FacebookSdk was initialized! =-")
        }
        AppEventsLogger.activateApp(this)*/
    }

    @Singleton
    object AppPreferences {
        private const val NAME = "ALL_PREFS"
        private const val MODE = Context.MODE_PRIVATE
        private lateinit var prefs: SharedPreferences

        // list of preferences
        private val MY_TEAM = "PREF_TEAM"
        private val SHOW_SUMMARIES = "PREF_SHOWSUMMARIES"
        private val PREF_SELECTED_LEAGUE = "PREF_SELECTED_LEAGUE"
        private val PREF_IS_PRO = "PREF_IS_PRO"

        fun init(context: Context) {
            prefs = context.getSharedPreferences(NAME, MODE)
        }

        /**
         * Gets/Sets the team to track.
         */
        var currentTeam : SportsTeam?
        get() = Gson().fromJson(prefs.getString(MY_TEAM, null), SportsTeam::class.java)
        set(value: SportsTeam?) {
            prefs.edit().putString(MY_TEAM, Gson().toJson(value)).commit()
        }

        /**
         * Whether or not to show additional details in the boxscores
         */
        var showSummariesInBoxscore : Boolean
            get() = prefs.getBoolean(SHOW_SUMMARIES, false)
            set(value) = prefs.edit().putBoolean(SHOW_SUMMARIES, value).apply()

        /**
         * The league name to use when searching teams
         */
        var currentLeague: String?
            get() = prefs.getString(PREF_SELECTED_LEAGUE, null)
            set(value) {
                prefs.edit().putString(PREF_SELECTED_LEAGUE, value).commit()
            }


        /**
         * The league name to use when searching teams
         */
        var isPro: Boolean
            get() = prefs.getBoolean(PREF_IS_PRO, false)
            set(value: Boolean) {
                prefs.edit().putBoolean(PREF_IS_PRO, value).commit()
            }

    }

    companion object {
        private const val TAG = "FIMTOWN|MyApp"
    }

    init {
        Log.i(TAG, "Initialized:MyApp")
    }
}
