package com.fimbleenterprises.sportsdb

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Used for sending custom (and stock) Firebase analytics to the Firebase console
 */
class MyAnalytics(val firebaseAnalytics: FirebaseAnalytics) {
    companion object {

        private const val EVENT_NAME_GENERAL = "general_use"

        private const val ITEM_NAME_VIEWED_HIGHLIGHTS = "viewed_highlights"
        private const val ITEM_NAME_FOLLOWED_TEAM = "followed_team"
        private const val ITEM_NAME_UNFOLLOWED_TEAM = "unfollowed_team"
        private const val ITEM_NAME_VIEWED_SCORES = "viewed_scores"
        private const val ITEM_NAME_SEARCHED_FOR_LEAGUE = "searched_for_league"
        private const val ITEM_NAME_SEARCHED_FOR_TEAM = "searched_for_team"
        private const val ITEM_NAME_MAIN_ACTIVITY = "main_activity_on_create"

        private const val ITEMID_VIEWED_HIGHLIGHTS = 1
        private const val ITEMID_FOLLOWED_TEAM = 2
        private const val ITEMID_UNFOLLOWED_TEAM = 3
        private const val ITEMID_VIEWED_SCORES = 4
        private const val ITEMID_SEARCHED_FOR_LEAGUE = 5
        private const val ITEMID_SEARCHED_FOR_TEAM = 6
        private const val ITEMID_MAIN_ACTIVITY = 7

    }

    /**
     * Logs an event recording that the user has followed a team.
     */
    fun logMainActivityEvent(strContent: String? = "") {
        val bundle = Bundle()
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, ITEMID_MAIN_ACTIVITY)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, ITEM_NAME_MAIN_ACTIVITY)
        bundle.putString(FirebaseAnalytics.Param.CONTENT, strContent)
        firebaseAnalytics.logEvent(EVENT_NAME_GENERAL, bundle)
    }

    /**
     * Logs an event recording that the user has followed a team.
     */
    fun logFollowedTeamEvent(strContent: String? = "") {
        val bundle = Bundle()
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, ITEMID_FOLLOWED_TEAM)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, ITEM_NAME_FOLLOWED_TEAM)
        bundle.putString(FirebaseAnalytics.Param.CONTENT, strContent)
        firebaseAnalytics.logEvent(EVENT_NAME_GENERAL, bundle)
    }

    /**
     * Logs an event recording that the user has unfollowed a team.
     */
    fun logUnFollowedTeamEvent(strContent: String? = "") {
        val bundle = Bundle()
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, ITEMID_UNFOLLOWED_TEAM)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, ITEM_NAME_UNFOLLOWED_TEAM)
        bundle.putString(FirebaseAnalytics.Param.CONTENT, strContent)
        firebaseAnalytics.logEvent(EVENT_NAME_GENERAL, bundle)
    }

    /**
     * Logs an event recording that the user has opened the ViewHighlightsFragment.
     */
    fun logViewedHighlightsEvent(strContent: String? = "") {
        val bundle = Bundle()
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, ITEMID_VIEWED_HIGHLIGHTS)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, ITEM_NAME_VIEWED_HIGHLIGHTS)
        bundle.putString(FirebaseAnalytics.Param.CONTENT, strContent)
        firebaseAnalytics.logEvent(EVENT_NAME_GENERAL, bundle)
    }

    /**
     * Logs an event recording that the user has unfollowed a team.
     */
    fun logViewedTeamScoresEvent(teamName: String? = "") {
        val bundle = Bundle()
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, ITEMID_VIEWED_SCORES)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, ITEM_NAME_VIEWED_SCORES)
        bundle.putString(FirebaseAnalytics.Param.CONTENT, teamName)
        firebaseAnalytics.logEvent(EVENT_NAME_GENERAL, bundle)
    }

    /**
     * Logs an event recording that the user has unfollowed a team.
     */
    fun logSearchedForLeagueEvent(leagueName: String? = "") {
        val bundle = Bundle()
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, ITEMID_SEARCHED_FOR_LEAGUE)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, ITEM_NAME_SEARCHED_FOR_LEAGUE)
        bundle.putString(FirebaseAnalytics.Param.CONTENT, leagueName)
        firebaseAnalytics.logEvent(EVENT_NAME_GENERAL, bundle)
    }

    /**
     * Logs an event recording that the user has unfollowed a team.
     */
    fun logSearchedForTeamEvent(teamName: String? = "") {
        val bundle = Bundle()
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, ITEMID_SEARCHED_FOR_TEAM)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, ITEM_NAME_SEARCHED_FOR_TEAM)
        bundle.putString(FirebaseAnalytics.Param.CONTENT, teamName)
        firebaseAnalytics.logEvent(EVENT_NAME_GENERAL, bundle)
    }
}