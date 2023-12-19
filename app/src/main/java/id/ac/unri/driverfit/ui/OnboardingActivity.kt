package id.ac.unri.driverfit.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import id.ac.unri.driverfit.databinding.ActivityOnboardingBinding
import id.ac.unri.driverfit.ui.onboarding.OnboardingAdapter
import id.ac.unri.driverfit.ui.onboarding.OnboardingViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {

    private val viewModel: OnboardingViewModel by lazy {
        ViewModelProvider(this)[OnboardingViewModel::class.java]
    }

    private var myAdapter: OnboardingAdapter? = null

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition {
            when (runBlocking { viewModel.loggedIn.first() }) {
                true -> Intent(this, MainActivity::class.java)
                false -> when (runBlocking { viewModel.onboarding.first() }) {
                    true -> Intent(this, AuthActivity::class.java)
                    false -> return@setKeepOnScreenCondition false
                }
            }.let { intent ->
                startActivity(intent)
                finish()
            }

            false
        }
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myAdapter = OnboardingAdapter()

        binding.viewPager2.adapter = myAdapter
        binding.viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        viewModel.data.observe(this) {
            myAdapter?.updateData(it)
        }

        // setup the indicator
        binding.indicatorViewPager2.setViewPager(binding.viewPager2)

        binding.buttonGetStarted.setOnClickListener {
            viewModel.onboarding(true)
            val intentMainActivity = Intent(this, AuthActivity::class.java)
            startActivity(intentMainActivity)
            finish()
        }

    }
}