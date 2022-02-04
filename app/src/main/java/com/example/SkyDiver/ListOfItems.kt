package com.example.SkyDiver

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ListOfItems : AppCompatActivity() {

    //=============================================================================
    //====================  T O   D O =============================================
    //=============================================================================

    //--------------------------L I S T S -----------------------------------------
        //https://www.vogella.com/tutorials/AndroidListView/article.html   - lists !
    //=============================================================================


    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_items)

        //=============================================================================
        //====================  I N I T S =============================================
        //=============================================================================
        val REQUEST_amount = 0;

        val textViewmaintext : TextView = findViewById(R.id.textView)



        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val temp =sharedPreferences
        val displaySavedItem : TextView = findViewById(R.id.textView_savedItem)
        displaySavedItem.text = temp?.getString("SavedAmount", null)






        //=============================================================================
        //====================  A D D   I N C O M E / E X P E N S E ===================
        //=============================================================================
        val buttonAddIncomeOrExpense: Button = findViewById(R.id.button_nav_IOE)
        buttonAddIncomeOrExpense.setOnClickListener( View.OnClickListener {
            // Code here executes on main thread after user presses button
            val intent = Intent(this, Add_IncomeOrExpense::class.java)
            startActivityForResult(intent,REQUEST_amount)
        })

        //=============================================================================
        //====================  A D D   R E M I N D E R ===============================
        //=============================================================================
        val buttonGoToPaymentReminder: Button = findViewById(R.id.button_nav_PR)
        buttonGoToPaymentReminder.setOnClickListener( View.OnClickListener {
            // Code here executes on main thread after user presses button
        val intent = Intent(this, Add_PaymentReminder::class.java)
        startActivity(intent)
        })







    }


    //=============================================================================
    //==================== R E C I E V E   D A T A     A D D   I N C O M E / E X P E N S E ===================
    //video help = https://www.youtube.com/watch?v=S1isQRnYAF4
    //=============================================================================

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK )
        {
            val textViewmaintext : TextView = findViewById(R.id.textView)
            val displaySavedItem : TextView = findViewById(R.id.textView_savedItem)

            //---------get the data from the received data
            val tempAmount = data?.getSerializableExtra( "amount")
            val tempExtraValue = data?.getSerializableExtra("sometingExtra")
            val tempType = data?.getSerializableExtra("Type")
                //put it in textViewmaintext
            textViewmaintext.text = tempType.toString()




            //----------------Save received info ---------------
            val editor = sharedPreferences!!.edit()
            editor.putString("SavedAmount", tempAmount.toString())
            editor.apply()

        }
        else
        {
            //ignore
        }
    }














//    override fun onClick(v: View) {
//
//        val textViewmaintext : TextView = findViewById(R.id.textView)
//        val amount = textViewmaintext.text.toString()
//
//
//        when (v.id) {
//            R.id.button_nav_IOE -> {
//
//                val action = ListOfItemsDirections.actionListOfItemsToAddPaymentReminder(amount)
//
//                v.findNavController().navigate(action)
//
//
//                }
//
//                else -> { //your code
//
//                     }
//                }
//            }



}
