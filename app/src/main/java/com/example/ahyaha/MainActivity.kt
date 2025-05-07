package com.example.ahyaha
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.ahyaha.presentation.view.MainScreen
import com.example.ahyaha.auth.LoginScreen

import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ahyaha.presentation.view.AddDonorView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()

        // Test Firebase connection
        testFirebaseConnection()

        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "mainScreen"
            ) {
                composable("login") {
                    LoginScreen(
                        navController = navController,
                        viewModel = hiltViewModel()
                    )
                }
                composable("mainScreen") {
                    MainScreen(
                        donorViewModel = hiltViewModel(),
                        bloodTypeViewModel = hiltViewModel(),
                        navController = navController,
                        isLoggedIn = false // Default to not logged in
                    )
                }
                composable("addDonor") {
                    AddDonorView(navController = navController)
                }
                composable("mainScreenLoggedIn") {
                    MainScreen(
                        donorViewModel = hiltViewModel(),
                        bloodTypeViewModel = hiltViewModel(),
                        navController = navController,
                        isLoggedIn = true // User is logged in
                    )
                }
            }
        }
    }

private fun testFirebaseConnection() {
    db.collection("test")
        .document("test")
        .set(hashMapOf("test" to "test"))
        .addOnSuccessListener {
            val successMessage = "Firebase connection successful"
            android.util.Log.d("Firebase", successMessage)
            Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            val errorMessage = "Firebase connection failed: ${e.message}\nCause: ${e.cause}"
            android.util.Log.e("Firebase", errorMessage, e)
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
}
}