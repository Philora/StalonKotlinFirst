package com.example.myapplication

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.avmed.memberapp.R
import com.avmed.memberapp.data.cache.ProfileDataCache
import com.avmed.memberapp.data.model.AccountBalance
import com.avmed.memberapp.data.model.AccountBalanceOOP
import com.avmed.memberapp.data.model.ClaimSummaryModel
import com.avmed.memberapp.data.model.MemberDetails
import com.avmed.memberapp.data.preferences.SharedPreferenceHelper
import com.avmed.memberapp.presentation.login.LoginActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import java.net.URLEncoder
import java.util.*
import java.util.regex.Pattern

object GeneralUtils {
    private var mProgressDialog: Dialog? = null

    /**
     * Shows progress bar to the user and disables interaction
     */
    fun showProgress(context: Context) {
        try {
            if (mProgressDialog != null) {
                mProgressDialog!!.dismiss()
                mProgressDialog = null
            }
            mProgressDialog = Dialog(context)
            mProgressDialog!!.setCancelable(false)
            mProgressDialog.setContentView(R.layout.progressbar_layout)
            if (mProgressDialog!!.window != null) {
                mProgressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            mProgressDialog!!.show()
        } catch (e: Exception) {
            Logger.e(StringConstants.EXCEPTION, StringConstants.EXCEPTION)
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
            Logger.e(StringConstants.EXCEPTION, StringConstants.EXCEPTION)
        }
    }

    /**
     * set the color for views
     *
     * @param ctx   of specific class
     * @param color specified
     * @return applied Color of int value
     */
    fun getColor(ctx: Context, color: Int): Int {
        val res = ctx.resources
        val appliedColor: Int
        appliedColor = if (Build.VERSION.SDK_INT < 23) {
            res.getColor(color)
        } else {
            ContextCompat.getColor(ctx, color)
        }
        return appliedColor
    }

    /**
     * Public method to determine if Device is connected to the internet.
     *
     * @param context context of the caller.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {   // As per Shraddha's guidance(12-05-2020) checked with Naga for Network Connectivity
        var isOnline = false
        try {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities = manager.getNetworkCapabilities(manager.activeNetwork) // need ACCESS_NETWORK_STATE permission
            isOnline = capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isOnline
        /* Old code - WPS
       ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        }
        return false;*/
    }

    fun showTitle(activity: AppCompatActivity, toolbar: Toolbar, title: String?) {
        val textView = activity.findViewById<TextView>(R.id.textView2)
        val fragmentTitle = toolbar.findViewById<TextView>(R.id.textViewTitle)
        val imageViewSearch = activity.findViewById<ImageView>(R.id.imageViewSearch)
        imageViewSearch.visibility = View.GONE
        textView.visibility = View.VISIBLE
        if (!TextUtils.isEmpty(title)) {
            fragmentTitle.text = title
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fragmentTitle.setTextColor(activity.getColor(R.color.black_overlay))
            }
        }
    }

    /**
     * Logs out from the app and launches login screen.
     *
     * @param context context of the caller.
     */
    fun logoutApp(context: Context) {
        ProfileDataCache.getInstance().clearCache()
        SharedPreferenceHelper.getInstance().clearPreference(context, AppConstants.AUTHORIZATION_TOKEN)
        SharedPreferenceHelper.getInstance().clearPreference(context, "test")
        SharedPreferenceHelper.getInstance().clearPreference(context, "test1")
        SharedPreferenceHelper.getInstance().clearPreference(context, StringConstants.PARAM2)
        SharedPreferenceHelper.getInstance().clearPreference(context, StringConstants.PARAM1)
        SharedPreferenceHelper.getInstance().clearPreference(context, "Language")
        SharedPreferenceHelper.getInstance().clearPreference(context, "sessionid")
        SharedPreferenceHelper.getInstance().clearPreference(context, "rememberMeStatus")
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
        System.exit(0)
    }

    /**
     * Used to convert HashMap data to String
     */
    fun convertToBody(params: Map<String?, String?>): String {
        val sb = StringBuilder()
        for ((key1) in params) {
            if (params[key1.toString()] != null) {
                sb.append(key1.toString()).append("=")
                sb.append(params[key1.toString()]).append("&")
            }
        }
        var data = sb.toString()
        data = data.substring(0, data.length - 1)
        return data
    }

    fun getBase64String(data: String): String {
        try {
            val bytesEncoded = Base64.encode(data.toByteArray(), 0)
            return String(bytesEncoded, StringConstants.DEFAULT_TEXT_ENCODING)
        } catch (e: Exception) {
            Logger.e(StringConstants.EXCEPTION, StringConstants.EXCEPTION)
        }
        return ""
    }

    fun getEncodedString(original: String): String? {
        // Encode data on your side using BASE64
        try {
            val bytesEncoded = Base64.encode(original.toByteArray(), 0)
            val coded = String(bytesEncoded, StringConstants.DEFAULT_TEXT_ENCODING)
            return URLEncoder.encode(coded, StringConstants.DEFAULT_TEXT_ENCODING)
        } catch (e: Exception) {
            Logger.e(StringConstants.EXCEPTION, StringConstants.EXCEPTION)
        }
        return null
    }

    fun getRealmString(env: String, context: Context): String {
        val param1: String = SharedPreferenceHelper.getInstance().getPreference(context, StringConstants.PARAM2)
        val param2: String = SharedPreferenceHelper.getInstance().getPreference(context, StringConstants.PARAM1)
        //String param = ProfileDataCache.getInstance().getParam();
        //Log.i("decrypt pass is",param1);
        //param1 = EncryptManager.decrypt(param1, param);
        //param2 = EncryptManager.decrypt(param2, param);
        val url = "realm=https%3A%2F%2F" + env + ".healthcare.nttdataservices.com%2FHealthGen%2FServices%2F" +
                "&UserName=" + getEncodedString(param1) +
                "&Password=" + getEncodedString(param2)
        Log.i("realm", url)
        return "realm=https%3A%2F%2F" + env + ".healthcare.nttdataservices.com%2FHealthGen%2FServices%2F" +
                "&UserName=" + getEncodedString(param1) +
                "&Password=" + getEncodedString(param2)
    }

    /**
     *  * To get request sub type based on the current login user
     *  * @return auth request sub type
     *  
     */
    val authRequestSubType: String
        get() {
            var reqSubType: String = NetworkConfigValues.AUTHSEARCH
            val personNumber: String = ProfileDataCache.getInstance().getmMemberDetails().get(0).getPersonNumber()
            if (personNumber != null && personNumber == "01") {
                reqSubType = NetworkConfigValues.AUTHSEARCH_SUBSCRIBER
            }
            return reqSubType
        }

    /**
     *  * To get request sub type based on the current login user
     *  * @return claim request sub type
     *  
     */
    val claimRequestSubType: String
        get() {
            var reqSubType: String = NetworkConfigValues.CLAIMLIST
            val personNumber: String = ProfileDataCache.getInstance().getmMemberDetails().get(0).getPersonNumber()
            if (personNumber != null && personNumber == "01") {
                reqSubType = NetworkConfigValues.CLAIMLISTSUBSCRIBER
            }
            return reqSubType
        }

    /**
     * Used to plot Account Balance data and plot it using Vertical bar Chart through MP Android chart library
     *
     * @param accountBalance account balance object
     */
    fun getVerticalChartResults(context: Context, accountBalance: AccountBalance, isFamily: Boolean): BarChart {
        val mBChart = BarChart(context)
        mBChart.invalidate()
        val yVals1: ArrayList<BarEntry> = ArrayList<BarEntry>()
        val myCOLORSCHART = intArrayOf(Color.rgb(225, 73, 73), Color.rgb(41, 171, 226))
        val xValues = Arrays.asList(*context.resources.getStringArray(R.array.Account_Balance_Range))
        mBChart.getDescription().setEnabled(false)
        // scaling can now only be done on x- and y-axis separately
        mBChart.setPinchZoom(false)
        mBChart.setDrawGridBackground(false)
        mBChart.setDrawBarShadow(false)
        mBChart.setDrawValueAboveBar(true)
        mBChart.setHighlightFullBarEnabled(false)

        // change the position of the y-labels
        val leftAxis: YAxis = mBChart.getAxisRight()
        leftAxis.setDrawGridLines(false)
        leftAxis.setEnabled(false)
        leftAxis.setAxisMinimum(0f) // this replaces setStartAtZero(true)
        mBChart.getAxisRight().setEnabled(false)
        if (isFamily) {
            val inNetworkFamilyDeductible: Float = accountBalance.getInNetworkFamilyDeductible().toFloat()
            val inNetworkFamilyDeductibleSpent: Float = accountBalance.getInNetworkFamilyDeductibleSpent().toFloat()
            val deductibleFamilyInNetwork = inNetworkFamilyDeductible - inNetworkFamilyDeductibleSpent
            val outNetworkFamilyDeductible: Float = accountBalance.getOutNetworkFamilyDeductible().toFloat()
            val outNetworkFamilyDeductibleSpent: Float = accountBalance.getOutNetworkFamilyDeductibleSpent().toFloat()
            val deductibleFamilyOutNetwork = outNetworkFamilyDeductible - outNetworkFamilyDeductibleSpent
            yVals1.add(BarEntry(0, floatArrayOf(inNetworkFamilyDeductibleSpent, deductibleFamilyInNetwork)))
            yVals1.add(BarEntry(1, floatArrayOf(outNetworkFamilyDeductibleSpent, deductibleFamilyOutNetwork)))
        } else {
            val inNetworkIndDeductible: Float = accountBalance.getInNetworkIndDeductible().toFloat()
            val inNetworkIndDeductibleSpent: Float = accountBalance.getInNetworkIndDeductibleSpent().toFloat()
            val deductibleInNetwork = inNetworkIndDeductible - inNetworkIndDeductibleSpent
            val outNetworkIndDeductible: Float = accountBalance.getOutNetworkIndDeductible().toFloat()
            val outNetworkIndDeductibleSpent: Float = accountBalance.getOutNetworkIndDeductibleSpent().toFloat()
            val deductibleIndOutNetwork = outNetworkIndDeductible - outNetworkIndDeductibleSpent
            yVals1.add(BarEntry(0, floatArrayOf(inNetworkIndDeductibleSpent, deductibleInNetwork)))
            yVals1.add(BarEntry(1, floatArrayOf(outNetworkIndDeductibleSpent, deductibleIndOutNetwork)))
        }
        val set1: BarDataSet
        if (mBChart.getData() != null &&
                mBChart.getData().getDataSetCount() > 0) {
            set1 = mBChart.getData().getDataSetByIndex(0) as BarDataSet
            if (!yVals1.isEmpty()) {
                set1.setValues(yVals1)
                mBChart.getData().notifyDataChanged()
                mBChart.notifyDataSetChanged()
            }
        } else {
            set1 = BarDataSet(yVals1, "")
            set1.setColors(myCOLORSCHART)
            set1.setDrawValues(true)
            set1.setVisible(true)
            val dataSets: ArrayList<IBarDataSet> = ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            data.setValueFormatter(AccountBalanceValueFormatter())
            data.setValueTextColor(Color.BLACK)
            data.setValueTextSize(8f)
            mBChart.setExtraRightOffset(30f)
            set1.setStackLabels(arrayOf("Met", "Remaining"))
            data.setBarWidth(0.4f)
            mBChart.setData(data)
        }
        val l: Legend = mBChart.getLegend()
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        val xAxis: XAxis = mBChart.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE)
        xAxis.setValueFormatter(IndexAxisValueFormatter(xValues))
        xAxis.setDrawLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.setLabelCount(7)
        xAxis.setGranularity(1f)
        xAxis.setGranularityEnabled(true)
        mBChart.setVisibleXRangeMaximum(4)
        mBChart.setTouchEnabled(false)
        mBChart.invalidate()
        return mBChart
    }

    /**
     * Used to plot Account Balance OOP data and plot it using Vertical bar Chart through MP Android chart library
     *
     * @param accountBalance account balance object
     */
    fun getVerticalChartResultsOOP(context: Context, accountBalance: AccountBalanceOOP, isFamily: Boolean): BarChart {
        val mBChart = BarChart(context)
        mBChart.invalidate()
        val yVals1: ArrayList<BarEntry> = ArrayList<BarEntry>()
        val myCOLORSCHART = intArrayOf(Color.rgb(44, 49, 120), Color.rgb(41, 171, 226))
        val xValues = Arrays.asList(*context.resources.getStringArray(R.array.Account_Balance_OOP_Range))
        mBChart.getDescription().setEnabled(false)
        // scaling can now only be done on x- and y-axis separately
        mBChart.setPinchZoom(false)
        mBChart.setDrawGridBackground(false)
        mBChart.setDrawBarShadow(false)
        mBChart.setDrawValueAboveBar(true)
        mBChart.setHighlightFullBarEnabled(false)

        // change the position of the y-labels
        val leftAxis: YAxis = mBChart.getAxisRight()
        leftAxis.setDrawGridLines(false)
        leftAxis.setEnabled(false)
        leftAxis.setAxisMinimum(0f) // this replaces setStartAtZero(true)
        mBChart.getAxisRight().setEnabled(false)
        if (isFamily) {
            val inNetworkFamilyOOP: Float = accountBalance.getInNetworkFamilyOOP().toFloat()
            val inNetworkFamilyOOPSpent: Float = accountBalance.getInNetworkFamilyOOPSpent().toFloat()
            val oopFamilyInNetwork = inNetworkFamilyOOP - inNetworkFamilyOOPSpent
            val outNetworkFamilyOOP: Float = accountBalance.getOutNetworkFamilyOOP().toFloat()
            val outNetworkFamilyOOPSpent: Float = accountBalance.getOutNetworkFamilyOOPSpent().toFloat()
            val oopFamilyOutNetwork = outNetworkFamilyOOP - outNetworkFamilyOOPSpent
            yVals1.add(BarEntry(0, floatArrayOf(inNetworkFamilyOOPSpent, oopFamilyInNetwork)))
            yVals1.add(BarEntry(1, floatArrayOf(outNetworkFamilyOOPSpent, oopFamilyOutNetwork)))
        } else {
            val inNetworkIndOOP: Float = accountBalance.getInNetworkIndOOP().toFloat()
            val inNetworkIndOOPSpent: Float = accountBalance.getInNetworkIndOOPSpent().toFloat()
            val oopInNetwork = inNetworkIndOOP - inNetworkIndOOPSpent
            val outNetworkIndOOP: Float = accountBalance.getOutNetworkIndOOP().toFloat()
            val outNetworkIndOOPSpent: Float = accountBalance.getOutNetworkIndOOPSpent().toFloat()
            val oopIndOutNetwork = outNetworkIndOOP - outNetworkIndOOPSpent
            yVals1.add(BarEntry(0, floatArrayOf(inNetworkIndOOPSpent, oopInNetwork)))
            yVals1.add(BarEntry(1, floatArrayOf(outNetworkIndOOPSpent, oopIndOutNetwork)))
        }
        val set1: BarDataSet
        if (mBChart.getData() != null &&
                mBChart.getData().getDataSetCount() > 0) {
            set1 = mBChart.getData().getDataSetByIndex(0) as BarDataSet
            if (!yVals1.isEmpty()) {
                set1.setValues(yVals1)
                mBChart.getData().notifyDataChanged()
                mBChart.notifyDataSetChanged()
            }
        } else {
            set1 = BarDataSet(yVals1, "")
            set1.setColors(myCOLORSCHART)
            set1.setDrawValues(true)
            set1.setVisible(true)
            val dataSets: ArrayList<IBarDataSet> = ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            data.setValueFormatter(AccountBalanceValueFormatter())
            data.setValueTextColor(Color.BLACK)
            data.setValueTextSize(8f)
            mBChart.setExtraRightOffset(30f)
            set1.setStackLabels(arrayOf("Met", "Remaining"))
            data.setBarWidth(0.4f)
            mBChart.setData(data)
        }
        val l: Legend = mBChart.getLegend()
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        val xAxis: XAxis = mBChart.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.TOP)
        xAxis.setValueFormatter(IndexAxisValueFormatter(xValues))
        xAxis.setDrawLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.setLabelCount(7)
        xAxis.setGranularity(1f)
        xAxis.setGranularityEnabled(true)
        mBChart.setVisibleXRangeMaximum(4)
        mBChart.setTouchEnabled(false)
        mBChart.invalidate()
        return mBChart
    }

    /**
     * Used to plot Claim Summary data and plot it using Horizontal bar Chart through MP Android chart library
     *
     * @param model claim summary object
     */
    fun getHorizontalChartResults(context: Context, model: ClaimSummaryModel): HorizontalBarChart {
        val myCOLORS = intArrayOf(Color.rgb(140, 198, 64), Color.rgb(41, 171, 226), Color.rgb(44, 49, 120),
                Color.rgb(4, 85, 165), Color.rgb(7, 182, 131))
        val mHChart = HorizontalBarChart(context)
        val entries: ArrayList<BarEntry> = ArrayList<BarEntry>()
        val daysCount: MutableList<Int> = ArrayList()
        if (model.getDays15() == null) daysCount.add(0) else {
            daysCount.add(model.getDays15().toInt())
        }
        if (model.getDays30() == null) daysCount.add(0) else {
            daysCount.add(model.getDays30().toInt())
        }
        if (model.getDays60() == null) daysCount.add(0) else {
            daysCount.add(model.getDays60().toInt())
        }
        if (model.getDays90() == null || model.getDays90Plus() == null) {
            daysCount.add(0)
        } else {
            daysCount.add(model.getDays90().toInt() + model.getDays90Plus().toInt())
        }
        val labels = Arrays.asList(*context.resources.getStringArray(R.array.Claims_Range))
        val xl: XAxis = mHChart.getXAxis()
        xl.setPosition(XAxis.XAxisPosition.BOTTOM)
        xl.setDrawAxisLine(false)
        xl.setDrawGridLines(false)
        val xaxisFormatter = LabelValueFormatter(labels)
        xl.setValueFormatter(xaxisFormatter)
        xl.setGranularity(1)
        val yl: YAxis = mHChart.getAxisLeft()
        yl.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        yl.setDrawGridLines(false)
        yl.setEnabled(false)
        yl.setAxisMinimum(0f)
        val yr: YAxis = mHChart.getAxisRight()
        yr.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        yr.setDrawGridLines(false)
        yr.setDrawLabels(false)
        yr.setAxisMinimum(0f)
        for (i in daysCount.indices) {
            entries.add(BarEntry(i, daysCount[i]))
        }
        val d = BarDataSet(entries, "New DataSet")
        val colors = ArrayList<Int>()
        for (c in myCOLORS) colors.add(c)
        d.setColors(colors)
        val sets: ArrayList<IBarDataSet> = ArrayList<IBarDataSet>()
        sets.add(d)
        val cd = BarData(sets)
        cd.setBarWidth(0.5f)
        cd.setValueTextSize(13f)
        mHChart.setData(cd)
        mHChart.setDrawBarShadow(false)
        mHChart.getDescription().setEnabled(false)
        mHChart.setDrawValueAboveBar(true)
        mHChart.setPinchZoom(false)
        mHChart.setDrawGridBackground(false)
        mHChart.getLegend().setEnabled(false)
        mHChart.setTouchEnabled(false)
        mHChart.animateXY(2000, 2000)
        mHChart.invalidate()
        return mHChart
    }

    fun isPasswordValid(param1: String): Boolean {
        var isValid = true
        val pattern = Pattern.compile(StringConstants.MPARAM_REGEX)
        if (param1.length < 9 || param1.length > 20) {
            isValid = false
        }
        if (!pattern.matcher(param1).matches()) {
            isValid = false
        }
        return isValid
    }

    val todayDate: String
        get() {
            val cal = Calendar.getInstance()
            val year = cal[Calendar.YEAR]
            val month = cal[Calendar.MONTH] + 1
            val day = cal[Calendar.DAY_OF_MONTH]
            return "$month-$day-$year"
        }

    fun generateRandomString(): String {
        var uuid = UUID.randomUUID().toString()
        uuid = uuid.replace("-", "")
        return uuid
    }

    fun showAlertDialog(activity: Activity, msg: String?) {
        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
                .setMessage(msg)
                .setPositiveButton(activity.getString(R.string.ok), null).create().show()
    }

    fun getPersonNumber(name: String): String {
        val detailsList: List<MemberDetails> = ProfileDataCache.getInstance().getmMemberDetails()
        for (details in detailsList) {
            val item: String = details.getFirstName().toString() + " " + details.getLastName()
            Log.i(name, item)
            if (name.equals(item, ignoreCase = true)) {
                return details.getPersonNumber()
            }
        }
        return "01"
    }

    fun showAlertDialogTitle(activity: Activity, title: String?, message: String?) {
        val builder = AlertDialog.Builder(activity)
        /*TextView txtTitle = new TextView(activity);   // Todo Make into Center align as same as iOS
        txtTitle.setText(title);
        txtTitle.setPadding(10, 10, 10, 10);
        txtTitle.setGravity(Gravity.CENTER);*/builder.setTitle(title)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(activity.getString(R.string.ok), null).create().show()
    }
}