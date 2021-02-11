package com.example.myapplication

import com.avmed.memberapp.data.cache.ProfileDataCache
import com.avmed.memberapp.data.model.AccountBalance
import com.avmed.memberapp.data.model.AccountBalanceOOP
import com.avmed.memberapp.data.model.AdvanceSearchFilters
import com.avmed.memberapp.data.model.ClaimSummaryModel
import com.avmed.memberapp.data.model.DashboardData
import com.avmed.memberapp.data.model.DemographyData
import com.avmed.memberapp.data.model.MemberDetails
import com.avmed.memberapp.data.model.MemberGroupDetails
import com.avmed.memberapp.data.model.MemberLobInfo
import com.avmed.memberapp.data.model.MessageData
import com.avmed.memberapp.data.model.Portlet
import com.avmed.memberapp.data.model.UpdatePasswordData

/**
 * This is singleton class to cache profile data and provides helper methods to access the same.
 */
class ProfileDataCache {
    private var mDashboardData: DashboardData? = null
    private var mMemberLobInfo: MemberLobInfo? = null
    private var mMemberDetails: List<MemberDetails>? = null
    private var mPortletList: List<Portlet>? = null
    private var mMemberGroupDetails: List<MemberGroupDetails>? = null
    private var mAccountBalance: AccountBalance? = null
    private var mAccountBalanceOOP: AccountBalanceOOP? = null
    private var mClaimSummary: ClaimSummaryModel? = null
    var benefitEffectiveDate: String? = null
    var externalKey2: String? = null
    var claimSearchDays: String? = null
    var memberGroupUserDefined: String? = null
    var claimNumber: String? = null
    var encryptClaimNumber: String? = null
    var hashCode: String? = null
    var authNumber: String? = null
    var msgID: String? = null
    var pageID = 0
    var param: String? = null
    var productCode: String? = null
    var notificationContent: String? = null
    private var mMessageData: MessageData? = null
    private var mPasswordData: UpdatePasswordData? = null
    private var mSearchFilters: AdvanceSearchFilters? = null
    var secureMessageSubjectsList: List<String>? = null
    var secureMessagesSearchData: Array<String>
    private var mDemographyData: DemographyData? = null
    var memberOHI: String? = null

    /**
     * Sets dashboard data object to cache
     *
     * @param dashboardData [DashboardData] object
     */
    var dashboardData: DashboardData?
        get() = mDashboardData
        set(dashboardData) {
            mDashboardData = dashboardData
        }

    /**
     * @return First Name associated with profile else null
     */
    val firstName: String?
        get() = if (mDashboardData != null) {
            mDashboardData.getFirstName()
        } else null

    /**
     * @return Last Name associated with profile else null
     */
    val lastName: String?
        get() = if (mDashboardData != null) {
            mDashboardData.getLastName()
        } else null

    fun setmMemberDetails(mMemberDetails: List<MemberDetails>?) {
        this.mMemberDetails = mMemberDetails
    }

    fun getmMemberDetails(): List<MemberDetails>? {
        return mMemberDetails
    }

    /**
     * Clears token cache.
     */
    fun clearCache() {
        mDashboardData = null
    }

    var memberLobInfo: MemberLobInfo?
        get() = mMemberLobInfo
        set(memberLobInfo) {
            mMemberLobInfo = memberLobInfo
        }

    fun getmMemberGroupDetails(): List<MemberGroupDetails>? {
        return mMemberGroupDetails
    }

    fun setmMemberGroupDetails(mMemberGroupDetails: List<MemberGroupDetails>?) {
        this.mMemberGroupDetails = mMemberGroupDetails
    }

    var searchFilters: AdvanceSearchFilters?
        get() = mSearchFilters
        set(mSearchFilters) {
            this.mSearchFilters = mSearchFilters
        }

    var messageData: MessageData?
        get() = mMessageData
        set(mMessageData) {
            this.mMessageData = mMessageData
        }

    var passwordData: UpdatePasswordData?
        get() = mPasswordData
        set(mPasswordData) {
            this.mPasswordData = mPasswordData
        }

    var demographyData: DemographyData?
        get() = mDemographyData
        set(demographyData) {
            mDemographyData = demographyData
        }

    var portletList: List<Any>?
        get() = mPortletList
        set(mPortletList) {
            this.mPortletList = mPortletList
        }

    var accountBalance: AccountBalance?
        get() = mAccountBalance
        set(mAccountBalance) {
            this.mAccountBalance = mAccountBalance
        }

    var accountBalanceOOP: AccountBalanceOOP?
        get() = mAccountBalanceOOP
        set(mAccountBalanceOOP) {
            this.mAccountBalanceOOP = mAccountBalanceOOP
        }

    var claimSummary: ClaimSummaryModel?
        get() = mClaimSummary
        set(mClaimSummary) {
            this.mClaimSummary = mClaimSummary
        }

    companion object {
        private var sInstance: ProfileDataCache? = null

        /**
         * Factory method to get profile data cache instance
         *
         * @return [com.avmed.memberapp.data.cache.ProfileDataCache] object
         */
        @get:Synchronized
        val instance: ProfileDataCache
            get() {
                if (sInstance == null) {
                    sInstance = ProfileDataCache()
                }
                return sInstance!!
            }
    }
}