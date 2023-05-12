package com.madison.move.ui.profile

import com.madison.move.ui.base.BasePresenter
import com.madison.move.ui.base.BaseView
import com.madison.move.ui.login.LoginContract

interface ProfileContract {
    interface ProfileView : BaseView {
        fun onShowLoading()
        fun onShowError(errorType: String)
        fun onLoginClick()
    }

    interface Presenter: BasePresenter<ProfileContract.ProfileView> {
        fun onShowLoadingPresenter()
        fun onShowErrorPresenter(errorType: String)
        fun onLoginClickPresenter()
    }

}