package com.stalon.app.utility

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.stalon.app.R
import java.text.SimpleDateFormat

class CommonUtils {

    /*fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }*/
    object DynamicProgressBarTitle {

        fun setProgressDialog(context: Context, message: String): AlertDialog {
            val padding = 50
            val linearLayout = LinearLayout(context)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            linearLayout.setPadding(padding, padding, padding, padding)
            linearLayout.gravity = Gravity.START
            var params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.gravity = Gravity.CENTER
            linearLayout.layoutParams = params

            val progressBar = ProgressBar(context)
            progressBar.isIndeterminate = true
            progressBar.setPadding(0, 0, padding, 0)
            progressBar.layoutParams = params

            params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.gravity = Gravity.CENTER
            val tvText = TextView(context)
            tvText.text = message
            tvText.setTextColor(Color.parseColor("#000000"))
            tvText.textSize = 20.toFloat()
            tvText.layoutParams = params

            linearLayout.addView(progressBar)
            linearLayout.addView(tvText)

            val builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            builder.setView(linearLayout)

            val dialog = builder.create()
            val window = dialog.window
            if (window != null) {
                val layoutParams = WindowManager.LayoutParams()
                layoutParams.copyFrom(dialog.window?.attributes)
                layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                dialog.window?.attributes = layoutParams
            }
            return dialog
        }
    }


    object ProgressBar {
        var mProgressDialog: Dialog? = null
        /**
         * Shows progress bar to the user and disables interaction
         */
        fun showProgress(context: Context?) {
            try {
                if (mProgressDialog != null) {
                    mProgressDialog!!.dismiss()
                    mProgressDialog = null
                }
                mProgressDialog = context?.let { Dialog(it) }
                mProgressDialog!!.setCancelable(false)
                mProgressDialog!!.setContentView(R.layout.progressbar_layout)
                if (mProgressDialog!!.window != null) {
                    mProgressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }
                mProgressDialog!!.show()
            } catch (e: Exception) {
                Log.d("CommUtils", "showProgress: ")
            }
        }

        /**
         * hides progress bar to the user and enables interaction.
         */
        fun hideProgress() {
            try {
                if (mProgressDialog != null) {
                    mProgressDialog!!.dismiss()
                }
            } catch (e: Exception) {
                Log.d("CommUtils", "hideProgress: ")
            }
        }
    }

    /**
     * Public method to determine if Device is connected to the internet.
     *
     * @param context context of the caller.
     */
    object ConnectivityCheck {
        @RequiresApi(api = Build.VERSION_CODES.M)
        fun isOnline(context: Context): Boolean {
            var isOnline = false
            try {
                val manager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val capabilities = manager.getNetworkCapabilities(manager.activeNetwork) // need ACCESS_NETWORK_STATE permission
                isOnline = capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return isOnline
        }
    }

    fun Context.toast(message:String, duration: Int){
        Toast.makeText(this, message , duration).show()
    }

    fun showAlertDialog(activity: Activity, msg: String?) {
        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
            .setMessage(msg)
            .setPositiveButton(activity.getString(R.string.ok), null).create().show()
    }

    fun showAlertDialogPosNeg(activity: Activity, msg: String?) {
        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
            .setMessage(msg)
            .setPositiveButton(activity.getString(R.string.ok), null)
            .setNegativeButton(activity.getString(R.string.ok), null)
            .create().show()
    }

    /**
     * Clear all activities
     */
    /*private fun logout() {
        if (getActivity() == null) {
            return
        }
        val builder = AlertDialog.Builder(getActivity())
        builder.setCancelable(false)

//Showing alert dialog to confirm for logout
        builder.setMessage(R.string.do_you_want_logout)
        builder.setPositiveButton(R.string.yes) { dialog, which -> logoutApp(getActivity()) }
        builder.setNegativeButton(R.string.no) { dialog, which ->
            dialog.cancel()
            val myIntent = Intent(getActivity(), DashboardActivity::class.java)
            getActivity().startActivity(myIntent)
            getActivity().finish()
        }
        val alert = builder.create()
        alert.show()
    }*/

    object ConvertDateTimeFormat {
        
        val mFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        fun mDateFormat(tme: String): String {
            val date = mFormat.parse(tme)
            val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
            val dateString = dateFormatter.format(date)
            return dateString
        }

        fun mTimeFormat(tme: String): String {
            val date = mFormat.parse(tme)
            val timeFormatter = SimpleDateFormat("HH:mm:ss")
            val timeString = timeFormatter.format(date)
            return timeString
        }
    }

}