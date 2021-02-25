package com.stalon.app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_Check.setOnClickListener(View.OnClickListener {
            CommonUtils.ProgressBar.showProgress(this)


            if (CommonUtils.ConnectivityCheck.isOnline(applicationContext)) {
                var mVinDI: String = "1234567890"
                val p = DataCache.instance
                p.mVinID = mVinDI
                startActivity(Intent(this, HomeData::class.java))
            } else {
                Toast.makeText(applicationContext, "No Internet", Toast.LENGTH_SHORT).show()
                CommonUtils.ProgressBar.hideProgress()
            }
        })
    }

    override fun onBackPressed() {
        logout();
    }

    /**
     * Clear all activities
     */
    private fun logout() {

        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Logout")
        //set message for alert dialog
        builder.setMessage("Do you want to Logout?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            finish()
        }
        //performing cancel action
        /*builder.setNeutralButton("Cancel"){dialogInterface , which ->
            Toast.makeText(applicationContext,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
        }*/
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            //Toast.makeText(applicationContext,"clicked No",Toast.LENGTH_LONG).show()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}