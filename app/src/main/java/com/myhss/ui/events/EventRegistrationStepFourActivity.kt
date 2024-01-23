package com.myhss.ui.events

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.myhss.Utils.DebouncedClickListener
import com.uk.myhss.Guru_Dakshina_OneTime.GuruDakshinaOneTimeCompleteActivity
import com.uk.myhss.R
import com.uk.myhss.databinding.ActivityEventRegistrationStepFourBinding

class EventRegistrationStepFourActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventRegistrationStepFourBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventRegistrationStepFourBinding.inflate(layoutInflater)
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
            Toast.makeText(this, "Click on Payment button", Toast.LENGTH_SHORT).show()
            val i = Intent(
                this@EventRegistrationStepFourActivity,
                GuruDakshinaOneTimeCompleteActivity::class.java
            )
            i.putExtra("paidAmount", "100")
            i.putExtra("orderId", "ODS0004698")
            i.putExtra("status", "Succeeded")
            i.putExtra("giftAid", "")
            i.putExtra("screen", "2")
            startActivity(i)
            finishAffinity()
        })
    }
}