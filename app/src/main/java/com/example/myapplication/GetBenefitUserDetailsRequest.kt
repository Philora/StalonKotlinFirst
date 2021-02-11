package com.example.myapplication

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.avmed.memberapp.data.cache.ProfileDataCache
import com.avmed.memberapp.data.preferences.SharedPreferenceHelper
import com.avmed.memberapp.utility.NetworkConfig
import com.avmed.memberapp.utility.NetworkConfigValues
import java.util.*

/**
 * This class is used to send a network request to get benefit user details
 * based on the effective date.
 */
class GetBenefitUserDetailsRequest(method: Int, url: String?, private val mContext: Context,
                                   listener: Response.Listener<String?>?, errorListener: Response.ErrorListener?) : StringRequest(method, url, listener, errorListener) {
    val headers: Map<String, String>
        get() {
            val headers: MutableMap<String, String> = HashMap()
            if (SharedPreferenceHelper.getInstance() != null && SharedPreferenceHelper.getInstance().getPreference(mContext, "sessionid") != null) {
                val sessionId: String = SharedPreferenceHelper.getInstance().getPreference(mContext, "sessionid")
                if (sessionId != null) {
                    headers[NetworkConfig.COOKIE] = sessionId
                }
                headers[NetworkConfig.REFERER] = NetworkConfigValues.REFERER_VALUE
                headers[NetworkConfig.USER_AGENT] = NetworkConfigValues.USER_AGENT_VALUE
            }
            return headers
        }

    protected val params: Map<String, String?>
        protected get() {
            val date: String = ProfileDataCache.instance.benefitEffectiveDate.toString();
            val params: MutableMap<String, String?> = HashMap()
            params[NetworkConfig.REQUEST_TYPE] = NetworkConfigValues.BENIFITS
            params[NetworkConfig.REQUEST_SUB_TYPE] = NetworkConfigValues.BENIFITS_MEMBER_LIST
            params[NetworkConfig.PAGE_ID] = ""
            params[NetworkConfig.EXNTERNAL_ID] = ""
            params[NetworkConfig.USER_ID] = ""
            params[NetworkConfig.MPARAM] = ""
            params[NetworkConfig.USER_TYPE] = ""
            params[NetworkConfig.REQUEST_PARAMETERS_0_PARAM_NAME] = NetworkConfigValues.EFFECTIVE_DATE
            params[NetworkConfig.REQUEST_PARAMETERS_0_PARAM_VALUES] = date
            params[NetworkConfig.REQUEST_PARAMETERS_0_OPERATOR] = NetworkConfigValues.LE
            params[NetworkConfig.REQUEST_PARAMETERS_1_PARAM_NAME] = NetworkConfig.TERM_DATE
            params[NetworkConfig.REQUEST_PARAMETERS_1_PARAM_VALUES] = date
            params[NetworkConfig.REQUEST_PARAMETERS_1_OPERATOR] = NetworkConfigValues.GE
            return params
        }

}