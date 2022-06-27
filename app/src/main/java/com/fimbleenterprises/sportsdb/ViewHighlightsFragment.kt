package com.fimbleenterprises.sportsdb

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fimbleenterprises.sportsdb.databinding.FragmentViewHighlightsBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class ViewHighlightsFragment : Fragment() {

    private lateinit var binding: FragmentViewHighlightsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_highlights, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentViewHighlightsBinding.bind(view)

        // Get the argument(s) we set up in nav_graph.xml
        val args : ViewHighlightsFragmentArgs by navArgs()

        try {
            playVideo(args.url)
        } catch (exception:Exception) {
            Log.e(TAG, "onViewCreated: ${exception.localizedMessage}"
                , exception)
            Toast.makeText(activity, "Failed to load highlights!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(
                R.id.action_goto_view_scores
            )
        }

        // Log to analytics
        (activity as MainActivity).myAnalytics.logViewedHighlightsEvent(args.teamname)
    }

    /**
     * Uses a third party YouTube API which is... okay.  The official YouTube API is fucking awful!
     * I would think very hard about whether or not to use the YouTube property at all after
     * this.  Disappointed!
     * https://github.com/PierfrancescoSoffritti/android-youtube-player
     */
    private fun playVideo(url: String) {
        val youTubePlayerView: YouTubePlayerView = binding.youtubePlayerView
        lifecycle.addObserver(youTubePlayerView)
        val videoid = url.substring((url.indexOf("=") + 1))
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoid, 0f)
            }
        })
    }

    companion object {
        private const val TAG = "FIMTOWN|viewHighlightsFragment"
    }

    init {
        Log.i(TAG, "Initialized:viewHighlightsFragment")
    }
}