package com.example.mealmate.ui.home.fragments

import ShakeDetector
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealmate.R
import com.example.mealmate.ui.auth.LoginActivity
import com.example.mealmate.ui.home.AddRecipeActivity
import com.google.firebase.auth.FirebaseAuth

abstract class BaseFragment : Fragment() {
    private lateinit var shakeDetector: ShakeDetector
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        shakeDetector = ShakeDetector(requireContext()) {
            val intent = Intent(requireContext(), AddRecipeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        shakeDetector.start()
    }

    override fun onPause() {
        super.onPause()
        shakeDetector.stop()
    }

}
