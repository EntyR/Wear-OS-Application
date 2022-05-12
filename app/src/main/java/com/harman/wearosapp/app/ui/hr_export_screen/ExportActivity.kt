package com.harman.wearosapp.app.ui.hr_export_screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.harman.wearosapp.app.R
import com.harman.wearosapp.app.databinding.ActivityExportBinding

class ExportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExportBinding.inflate(layoutInflater)
        val fragment = SubmitFragment.newInstance()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, fragment)
                .commit()
        }
        setContentView(binding.root)
    }


    companion object {
        fun navigateTo(fragmentToNavigate: Fragment, transactionName: String, fragmentManager: FragmentManager) {
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragmentToNavigate)
            transaction.commit()
        }
    }
}