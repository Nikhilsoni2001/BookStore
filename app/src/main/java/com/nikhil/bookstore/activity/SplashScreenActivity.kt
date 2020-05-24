package com.nikhil.bookstore.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.nikhil.bookstore.R

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            val startAct = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(startAct)
        },2000)

    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
