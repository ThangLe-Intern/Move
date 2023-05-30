package com.madison.move.ui.profile

import com.madison.move.data.model.User
import com.madison.move.data.model.country.CountryResponse
import com.madison.move.data.model.state.StateResponse
import com.madison.move.data.model.update_profile.ProfileRequest
import com.madison.move.data.model.update_profile.UpdateProfileResponse
import com.madison.move.data.model.user_profile.ProfileResponse
import com.madison.move.ui.base.BasePresenter
import com.madison.move.ui.base.BaseView

interface ProfileContract {
    interface ProfileView : BaseView {
        fun onShowLoading()
        fun onShowError(errorType: String)
        fun onSuccessGetProfileData(profileResponse: ProfileResponse)
        fun onSuccessGetCountryData(countryResponse: CountryResponse)
        fun onSuccessGetStateData(stateResponse: StateResponse)
        fun onSuccessUpdateProfile(updateProfileResponse: UpdateProfileResponse)
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