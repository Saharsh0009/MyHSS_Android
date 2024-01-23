package com.myhss.ui.events

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.myhss.Utils.DebouncedClickListener
import com.uk.myhss.R
import com.uk.myhss.databinding.ActivityEventRegistrationViewBinding
import com.uk.myhss.ui.dashboard.EventsFragment

class EventRegistrationViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventRegistrationViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventRegistrationViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topbar.headerTitle.text = "Youth Club Event"
        binding.topbar.backArrow.setOnClickListener(DebouncedClickListener {
            val i = Intent(
                this@EventRegistrationViewActivity,
                EventsFragment::class.java
            )
            startActivity(i)
            finishAffinity()
        })

        binding.nextLayout.setOnClickListener(DebouncedClickListener {
            val i = Intent(
                this@EventRegistrationViewActivity,
                EventsFragment::class.java
            )
            startActivity(i)
            finishAffinity()
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(
            this@EventRegistrationViewActivity,
            EventsFragment::class.java
        )
        startActivity(i)
        finishAffinity()
    }
}