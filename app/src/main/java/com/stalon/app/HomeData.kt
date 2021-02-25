package com.stalon.app

import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.home_activity.*

class HomeData: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        CommonUtils.ProgressBar.hideProgress()
        var mVinPass = DataCache.instance.mVinID
        txt_CacheData.setText(mVinPass)
        val myToast = Toast.makeText(applicationContext,mVinPass,Toast.LENGTH_SHORT)
        myToast.setGravity(Gravity.LEFT,200,200)
        myToast.show()
    }
}
