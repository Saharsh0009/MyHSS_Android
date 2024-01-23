package com.myhss.ui.events

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.myhss.Utils.DebouncedClickListener
import com.uk.myhss.R
import com.uk.myhss.databinding.ActivityEventRegistrationStepThreeBinding

class EventRegistrationStepThreeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventRegistrationStepThreeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_event_registration_step_three)
        binding = ActivityEventRegistrationStepThreeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.topbar.headerTitle.text = getString(R.string.event_registration)
        binding.topbar.backArrow.setOnClickListener(DebouncedClickListener {
            finish()
        })

        binding.backLayout.setOnClickListener(DebouncedClickListener {
            finish()
        })

        binding.nextLayout.setOnClickListener(DebouncedClickListener {
            val i = Intent(
                this@EventRegistrationStepThreeActivity,
                EventRegistrationStepFourActivity::class.java
            )
            startActivity(i)
        })

    }
}