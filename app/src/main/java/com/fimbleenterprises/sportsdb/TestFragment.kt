package com.fimbleenterprises.sportsdb

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fimbleenterprises.sportsdb.databinding.FragmentTestBinding
import com.fimbleenterprises.sportsdb.presentation.viewmodel.SportsdbViewModel
import com.google.firebase.database.*


class TestFragment : Fragment() {

    private lateinit var binding: FragmentTestBinding
    private lateinit var viewmodel: SportsdbViewModel

    // creating a variable for our Firebase Database.
    private lateinit var firebaseDatabase: FirebaseDatabase

    // creating a variable for our Database Reference for Firebase.
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTestBinding.bind(view)
        viewmodel = (activity as MainActivity).viewModel


    }

    companion object {
        private const val TAG = "FIMTOWN|TestFragment"
    }

    init {
        Log.i(TAG, "Initialized:TestFragment")
    }
}