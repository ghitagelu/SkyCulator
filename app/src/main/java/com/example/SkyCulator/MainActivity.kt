package com.example.SkyCulator


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)





        val buttonGoToList: Button = findViewById(R.id.button_nav_LOF)
        buttonGoToList.setOnClickListener(View.OnClickListener {
            // Code here executes on main thread after user presses button
            val intent = Intent(this, ListOfItems::class.java)
            startActivity(intent)

        })


    }






}
