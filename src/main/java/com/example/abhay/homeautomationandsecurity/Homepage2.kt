package com.example.abhay.homeautomationandsecurity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_homepage2.*


class Homepage2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage2)

        access_mode.setOnClickListener {

            val intent = Intent(this, AccessMode ::class.java)
            startActivity(intent)
        }

        security_mode.setOnClickListener {

            val intent = Intent(this, SecurityMode ::class.java)
            startActivity(intent)
        }
    }
}
