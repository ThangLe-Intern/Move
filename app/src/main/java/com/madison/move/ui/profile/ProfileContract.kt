package com.madison.move.ui.profile

import com.madison.move.data.model.ObjectResponse
import com.madison.move.data.model.DataCountry
import com.madison.move.data.model.DataState
import com.madison.move.data.model.ProfileRequest
import com.madison.move.data.model.DataUser
import com.madison.move.ui.base.BasePresenter
import com.madison.move.ui.base.BaseView

interface ProfileContract {
    interface ProfileView : BaseView {
        fun onShowLoading()
        fun onShowError(errorType: String)
        fun onSuccessGetProfileData(profileResponse: ObjectResponse<DataUser>)
        fun onSuccessGetCountryData(countryResponse: ObjectResponse<List<DataCountry>>)
        fun onSuccessGetStateData(stateResponse: ObjectResponse<List<DataState>>)
        fun onSuccessUpdateProfile(updateProfileResponse: ObjectResponse<DataUser>)
        fun onErrorGetProfile(errorType: String)
    }

    interface Presenter : BasePresenter<ProfileView> {
        fun onShowLoadingPresenter()
        fun onShowErrorPresenter(errorType: String)
        fun onSaveProfileClickPresenter(token: String,profileRequest: ProfileRequest)

        fun getProfileUserDataPresenter(token: String)
        fun getCountryDataPresenter()
        fun getStateDataPresenter(countryID:Int)
    }

}