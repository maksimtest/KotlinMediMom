package com.medimom

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.medimom.data.AppRepository
import com.medimom.data.MainViewModel
import com.medimom.data.MainViewModelFactory
import com.medimom.data.UserSettings
import com.medimom.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userSettings: UserSettings
    private lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userSettings = (application as MyApp).userSettings

        val db = (application as MyApp).database
        val repository = AppRepository(db)

        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]
        mainViewModel.loadOnFirstActivity()


        val bottomNavView = binding.bottomNavigationView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment

        val navController = navHostFragment.navController

        bottomNavView.setupWithNavController(navController)
    }
}