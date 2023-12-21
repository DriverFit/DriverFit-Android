package id.ac.unri.driverfit.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import id.ac.unri.driverfit.R
import id.ac.unri.driverfit.databinding.ActivityMainBinding
import id.ac.unri.driverfit.ui.main.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.user.observe(this) { user ->
            if (user == null) {
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in arrayOf(
                    R.id.navigation_home,
                    R.id.navigation_detection,
                    R.id.navigation_profile
                )
            ) {
                binding.navView.visibility = View.VISIBLE
            } else {
                binding.navView.visibility = View.GONE
            }
        }

        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_detection, R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navHostFragment.navController, appBarConfiguration)
        binding.navView.setupWithNavController(navHostFragment.navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        return navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()
    }

}