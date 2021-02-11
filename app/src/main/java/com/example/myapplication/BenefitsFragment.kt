package com.example.myapplication

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdynamics.eumagent.runtime.Instrumentation
import com.avmed.memberapp.data.cache.ProfileDataCache
import com.avmed.memberapp.data.model.AccountBalance
import com.avmed.memberapp.data.model.AccountBalanceOOP
import com.avmed.memberapp.data.model.BenefitUserDetails
import com.avmed.memberapp.data.model.BenefitUserProduct
import com.avmed.memberapp.data.model.MemberLobInfo
import com.avmed.memberapp.presentation.base.BaseFragment
import com.avmed.memberapp.presentation.benefits.adapter.BenefitChildViewAdapter
import com.avmed.memberapp.utility.CustomRecyclerView
import com.avmed.memberapp.utility.CustomScrollView
import com.avmed.memberapp.utility.GeneralUtils
import com.avmed.memberapp.utility.Logger
import com.avmed.memberapp.utility.StringConstants
import com.github.mikephil.charting.charts.BarChart

/**
 * This fragment is used to display medical benefits to the user.
 */
class BenefitsFragment : BaseFragment(), BenefitsView, View.OnClickListener {
    //Member variables declaration
    private var tvMemberIdValue: TextView? = null
    private var tvMedicalPlanDetailView: TextView? = null
    private var tvDobDetailTextView: TextView? = null
    private var tvEffectiveDetailTextView: TextView? = null
    private var tvBenefitYearTextView: TextView? = null
    private var tvMemberName: TextView? = null
    private var familyText: TextView? = null
    private var individualText: TextView? = null
    private var familyTextOOP: TextView? = null
    private var individualTextOOP: TextView? = null
    private var llChartView: LinearLayout? = null
    private var llOOPChartView: LinearLayout? = null
    private var recycleView: CustomRecyclerView? = null
    private var presenter: BenefitsPresenter? = null
    private var mAccountOOPModel: AccountBalanceOOP? = null
    private var mAccountModel: AccountBalance? = null

    /**
     * This override method is used to inflate the layout for Dashboard fragment
     *
     * @return rootView
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                     savedInstanceState: Bundle?): View {
        if (getActivity() != null) {
            Instrumentation.start(StringConstants.APP_DYNAMICS_KEY, getActivity().getApplicationContext())
        }
        if (container != null) {
            container.removeAllViews()
            container.clearDisappearingChildren()
        }
        val rootView: View = inflater.inflate(R.layout.fragment_benefit, container, false)
        presenter = BenefitsPresenterImpl()
        presenter.onAttach(this)
        recycleView = rootView.findViewById(R.id.recyclerBenefitDetailView)
        val view = rootView.findViewById<View>(R.id.account_header)
        val oopView = rootView.findViewById<View>(R.id.accountoop_header)
        val info: MemberLobInfo = ProfileDataCache.getInstance().getMemberLobInfo()
        if (StringConstants.LOB_AVMED.equals(info.getLOB())) { //  && StringConstants.ENTITY_ID_MEDMARSOL.equals(info.getCurrentRelEntityID()) // ToDo has to check with API call (GetMemberEligibleInfo -> GetMemberLobInfo)
            view.visibility = View.GONE
            oopView.visibility = View.GONE
        }
        val scrollView: ScrollView = rootView.findViewById<View>(R.id.fragmentContainerScrollViewBenefits) as CustomScrollView
        recycleView.setHasFixedSize(true)

        //Setting title to fragment
        if (getActivity() != null) {
            val fragmentTitle: TextView = getActivity().findViewById(R.id.textViewTitle)
            fragmentTitle.setText(R.string.my_benefits)
            val imageViewSearch: ImageView = getActivity().findViewById(R.id.imageViewSearch)
            imageViewSearch.visibility = View.GONE
        }
        recycleView.setLayoutManager(LinearLayoutManager(getActivity()))
        val tvDate = rootView.findViewById<TextView>(R.id.datesSpinner)
        tvMemberIdValue = rootView.findViewById(R.id.memberIdValue)
        tvMedicalPlanDetailView = rootView.findViewById(R.id.medicalPlanDetailView)
        tvDobDetailTextView = rootView.findViewById(R.id.dobDetailTextView)
        tvEffectiveDetailTextView = rootView.findViewById(R.id.effectiveDetailTextView)
        tvBenefitYearTextView = rootView.findViewById(R.id.benefitYearTextView)
        tvMemberName = rootView.findViewById(R.id.spinnerMemberName)
        llChartView = rootView.findViewById(R.id.chartView)
        llOOPChartView = rootView.findViewById(R.id.oopChartView)
        familyText = rootView.findViewById(R.id.textViewFamily)
        individualText = rootView.findViewById(R.id.textViewIndividual)
        familyTextOOP = rootView.findViewById(R.id.textViewFamilyOOP)
        individualTextOOP = rootView.findViewById(R.id.textViewIndividualOOP)
        familyText.setOnClickListener(this)
        individualText.setOnClickListener(this)
        familyTextOOP.setOnClickListener(this)
        individualTextOOP.setOnClickListener(this)

        //Touch listener to disable the scroll of the parent view
        recycleView.setOnTouchListener({ v, event ->
            scrollView.requestDisallowInterceptTouchEvent(false)
            val action: Int = event.getActionMasked()
            scrollView.performClick()
            if (action == MotionEvent.ACTION_UP) {
                scrollView.requestDisallowInterceptTouchEvent(false)
            }
            false
        })
        var today: String = GeneralUtils.getTodayDate()
        today = today.replace("-", "/")
        tvDate.text = today
        ProfileDataCache.Companion.getInstance().setBenefitEffectiveDate(today)

        //Calling BenefitUserDetails and Account balance related APIs if internet is available
        if (getActivity() != null) {
            if (GeneralUtils.isOnline(getActivity())) {
                presenter.getBenefitUserDetails()
                /*if (StringConstants.ENTITY_ID_MEDMARSOL.equals(info.getCurrentRelEntityID())) { // ToDo has to check with API call (GetMemberEligibleInfo -> GetMemberLobInfo)
                    presenter.getAccountBalance();
                    presenter.getAccountBalanceOOP();
                }*/
                // ToDo Has to check
                presenter.getAccountBalance()
                presenter.getAccountBalanceOOP()
            } else {
                GeneralUtils.showAlertDialog(getActivity(), getActivity().getString(R.string.login_no_internet))
            }
        }
        return rootView
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.textViewFamily -> {
                familyText!!.setTypeface(null, Typeface.BOLD)
                individualText!!.setTypeface(null, Typeface.NORMAL)
                loadAccountBalanceDetails(true)
            }
            R.id.textViewIndividual -> {
                familyText!!.setTypeface(null, Typeface.NORMAL)
                individualText!!.setTypeface(null, Typeface.BOLD)
                loadAccountBalanceDetails(false)
            }
            R.id.textViewFamilyOOP -> {
                familyTextOOP!!.setTypeface(null, Typeface.BOLD)
                individualTextOOP!!.setTypeface(null, Typeface.NORMAL)
                loadAccountBalanceOOPDetails(true)
            }
            R.id.textViewIndividualOOP -> {
                familyTextOOP!!.setTypeface(null, Typeface.NORMAL)
                individualTextOOP!!.setTypeface(null, Typeface.BOLD)
                loadAccountBalanceOOPDetails(false)
            }
            else -> {
            }
        }
    }

    /*
     This method is used to load account balance out of packet details in BarChart
     */
    private fun loadAccountBalanceOOPDetails(isFamily: Boolean) {
        if (mAccountOOPModel != null && getActivity() != null) {
            val mBarChart: BarChart = GeneralUtils.getVerticalChartResultsOOP(getActivity(), mAccountOOPModel, isFamily)
            llOOPChartView!!.removeAllViews()
            llOOPChartView!!.addView(mBarChart, RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT))
        }
    }

    /*
     This method is used to load account balance details in BarChart
     */
    private fun loadAccountBalanceDetails(isFamily: Boolean) {
        try {
            if (mAccountModel != null && getActivity() != null) {
                val mBarChart: BarChart = GeneralUtils.getVerticalChartResults(getActivity(), mAccountModel, isFamily)
                llChartView!!.removeAllViews()
                llChartView!!.addView(mBarChart, RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT))
            }
        } catch (e: Exception) {
            Logger.i(StringConstants.EXCEPTION, e)
        }
    }

    /*
     This method is a Call back for account balance API call result
     */
    fun onAccountBalanceLoading(balance: AccountBalance) {
        mAccountModel = balance
        loadAccountBalanceDetails(false)
    }

    /*
    This method is a Call back for account balance oop API call result
    */
    fun onAccountBalanceOOPLoading(balanceOOP: AccountBalanceOOP) {
        mAccountOOPModel = balanceOOP
        loadAccountBalanceOOPDetails(false)
    }

    /*
    This method is used to handle the response of benefit user details
     API response and bind the response to UI components
     */
    fun onUserDetailsLoadingCompleted(details: BenefitUserDetails) {
        try {
            presenter.getBenefitProductDetails()
            val memberID: String = details.getSubscriberID().toString() + "-" + details.getPersonNumber()
            tvMemberIdValue!!.text = memberID
            tvMedicalPlanDetailView!!.isSelected = true
            tvMedicalPlanDetailView.setText(details.getPlanName())
            tvDobDetailTextView.setText(details.getDateOfBirth())
            tvEffectiveDetailTextView.setText(details.getCoverageDate())
            tvBenefitYearTextView.setText(details.getCoverageDate())
            val name: String = details.getFirstName().toString() + " " + details.getLastName()
            tvMemberName!!.text = name
        } catch (e: Exception) {
            Logger.e(StringConstants.EXCEPTION, StringConstants.EXCEPTION)
        }
    }

    /*
    This method is used to handle the response of benefit product details
     API response and bind the response to UI components
     */
    fun onProductDetailsLoadingCompleted(productsList: List<BenefitUserProduct?>) {
        if (!productsList.isEmpty()) {
            val adapter = BenefitChildViewAdapter(getActivity(), productsList)
            recycleView.setItemAnimator(DefaultItemAnimator())
            recycleView.setAdapter(adapter)
            adapter.notifyDataSetChanged()
        }
    }
}