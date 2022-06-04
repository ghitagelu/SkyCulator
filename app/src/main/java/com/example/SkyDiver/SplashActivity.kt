package com.example.SkyDiver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setTheme(R.style.AppTheme_NoActionBarWithoutBackground)

        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, StartingActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }

    companion object {
        const val SPLASH_TIME_OUT: Long = 500 // 1 sec
    }
}