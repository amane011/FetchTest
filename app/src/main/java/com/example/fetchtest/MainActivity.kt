package com.example.fetchtest

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewAll = findViewById<TextView>(R.id.view_all)
        val viewByGroup = findViewById<TextView>(R.id.view_by_group)
    }
}