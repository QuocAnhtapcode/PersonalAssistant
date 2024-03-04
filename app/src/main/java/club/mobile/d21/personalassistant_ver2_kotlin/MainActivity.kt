package club.mobile.d21.personalassistant_ver2_kotlin

import android.icu.util.Calendar
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import club.mobile.d21.personalassistant_ver2_kotlin.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_container)
        AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_calendar,
                R.id.navigation_profile
            )
        )
        navView.setupWithNavController(navController)
        setupActions()
    }
    private fun setupActions() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE d MMMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        binding.todayDate.text = formattedDate
    }
}
