package com.stalon.app.sample

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.stalon.app.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val inFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outFormat = SimpleDateFormat("dd MMM yyyy hh:mm aa")


      /*  btn_Check.setOnClickListener(View.OnClickListener {

//            checkFormattedDateTime()
            val date = CommonUtils.ConvertDateTimeFormat.mDateFormat("2020-09-23T12:46:17.150102")
            val time = CommonUtils.ConvertDateTimeFormat.mTimeFormat("2020-09-23T12:46:17.150102")

            Toast.makeText(applicationContext, "Date :"+date + "Time :"+time, Toast.LENGTH_SHORT).show()
//            staticFormattedDateTime()
//            changeDateTime()
        })*/
    }

    private fun changeDateTime() {
        /* val millis: Long = TimeUnit.MILLISECONDS.convert(tme.toLong(), TimeUnit.MICROSECONDS)
           val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
           //  simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
           val dateString = simpleDateFormat.format(millis)


           val simpleTimeFormat = SimpleDateFormat("HH:mm:ss")
           // simpleTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
           val timeString = simpleTimeFormat.format(millis)*/
    }

    private fun staticFormattedDateTime() {
        val tme: String = "2020-09-23T12:46:17.150102"

        val dateTime: LocalDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.parse(tme, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"))
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val localDate: LocalDate = dateTime.toLocalDate()
        val localTime: LocalTime = dateTime.toLocalTime()

        Toast.makeText(applicationContext, "Date :" + localDate + "Time " + localTime, Toast.LENGTH_SHORT).show()
    }

    private fun checkFormattedDateTime() {
//        val startTime = "2013-02-27T21:06:30"
        val startTime = "2020-09-23T12:46:17.150102"

        val mFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        val date = mFormat.parse(startTime)

        val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
        val timeFormatter = SimpleDateFormat("HH:mm:ss")
        val displayValue = "Date " + dateFormatter.format(date) + " Time " + timeFormatter.format(date)
        Toast.makeText(applicationContext, displayValue, Toast.LENGTH_SHORT).show()
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