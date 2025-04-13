package com.example.fetchtest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fetchtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ItemViewModel
    private var viewByGroupFragment: Fragment? = null
    private var viewAllFragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val repository = ItemRepository()
        val factory = ItemViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ItemViewModel::class.java]
        val viewAll = binding.viewAll
        val viewByGroup = binding.viewByGroup
        createAndShowViewAllFragment()

        viewAll.setOnClickListener {
            createAndShowViewAllFragment()
        }
        viewByGroup.setOnClickListener {
        }

    }

    private fun createAndShowViewAllFragment() {
        if (viewAllFragment == null) {
            viewAllFragment = ViewAllFragment()
        }
        viewAllFragment?.let {
            showFragment(it)
        }

    }

    private fun showFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            ).replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}