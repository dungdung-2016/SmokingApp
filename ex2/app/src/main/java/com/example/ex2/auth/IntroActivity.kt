package com.example.ex2.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ex2.R
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        button_login.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }

        button_signup.setOnClickListener{
            startActivity(Intent(this,SignupActivity::class.java))
        }
    }
}