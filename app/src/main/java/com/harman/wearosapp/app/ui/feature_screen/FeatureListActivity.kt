package com.harman.wearosapp.app.ui.feature_screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.harman.wearosapp.app.R
import com.harman.wearosapp.app.databinding.ActivityMainBinding

class FeatureListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val fragment = FeatureListFragment.newInstance()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, fragment)
                .commit()
        }
        setContentView(binding.root)
    }
}