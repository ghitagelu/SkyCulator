package com.example.SkyCulator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Add_PaymentReminder : AppCompatActivity() {

    //=============================================================================
    //====================  T O   D O =============================================

    //--------------------------S T U F F -----------------------------------------
    //https://www.youtube.com/watch?v=nl-dheVpt8o - send notification even if the app is closed
    //https://www.youtube.com/watch?v=8lVVPtJBZRg change text on certain date

    //https://www.youtube.com/watch?v=FbpD5RZtbCc THIS LOOKS LIKE THE SHIT
    // look into JobScheduler

    //do something when the phone powersOn (startup) / restart phone
    // https://www.youtube.com/watch?v=4_CkU9L2mCo  -Nice presenter - look into him

    //=============================================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add__payment_reminder)
    }
}
