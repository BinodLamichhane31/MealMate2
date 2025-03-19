package com.example.mealmate.ui.home.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mealmate.R
import com.example.mealmate.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fullNameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()

        fullNameTextView = view.findViewById(R.id.fullNameTextView)
        emailTextView = view.findViewById(R.id.emailTextView)
        logoutButton = view.findViewById(R.id.logoutButton)

        val user = auth.currentUser
        if (user != null) {
            emailTextView.text = user.email ?: "No Email"

            // Load full name from SharedPreferences
            val sharedPreferences = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val fullName = sharedPreferences?.getString("fullName", "No Name")
            fullNameTextView.text = fullName ?: "No Name"
        }

        logoutButton.setOnClickListener {
            // Show confirmation dialog before logging out
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Logout")
            builder.setMessage("Are you sure you want to logout?")

            builder.setPositiveButton("Yes") { dialog, _ ->
                // Logout user
                auth.signOut()

                // Clear the login state in SharedPreferences
                val sharedPreferences = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences?.edit()
                editor?.putBoolean("isLoggedIn", false) // Set login state to false
                editor?.apply()

                // Navigate to LoginActivity after logout
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

                dialog.dismiss() // Dismiss the dialog after the action
            }

            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Dismiss the dialog if the user cancels
            }

            // Show the confirmation dialog
            builder.create().show()
        }


        return view
    }
}
