package com.harman.wearosapp.app.ui.heart_rate_screen

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import com.harman.wearosapp.app.R
import com.harman.wearosapp.app.databinding.ActivityHeartRateBinding

class HeartRateActivity : AppCompatActivity() {

    lateinit var binding: ActivityHeartRateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeartRateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fragment = HeartRateFragment.newInstance()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.hrContainer, fragment)
                .commit()
        }
    }
}