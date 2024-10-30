package com.example.SkyDiver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.SkyDiver.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setTheme(R.style.AppTheme_NoActionBarWithoutBackground)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, StartingActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
        Log.d("App stages", "App finished Splash activity and started StartingActivity")
    }

    companion object {
        const val SPLASH_TIME_OUT: Long = 500 // 1 sec
    }
}