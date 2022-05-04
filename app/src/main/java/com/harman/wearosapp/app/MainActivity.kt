package com.harman.wearosapp.app

import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.harman.wearosapp.app.databinding.ActivityMainBinding
import com.harman.wearosapp.app.hr_service.HealthServicesManager
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

//TODO move manager and permissions call to Fragment, when is added
class MainActivity : AppCompatActivity() {

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var binding: ActivityMainBinding


    private val manager: HealthServicesManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)



        setContentView(binding.root)
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                when (result) {
                    true -> {
                        Log.i("TAG", "Body sensors permission granted")
                        lifecycleScope.launch {
                            manager.registerForHeartRateData()
                        }
                    }
                    false -> {
                        Log.i("TAG", "Body sensors permission not granted")
                    }
                }
            }
        permissionLauncher.launch(android.Manifest.permission.BODY_SENSORS)


    }
}