package com.madison.move.ui.menu

import androidx.fragment.app.DialogFragment
import com.madison.move.data.model.login.LoginResponse
import com.madison.move.data.model.logout.LogoutResponse
import com.madison.move.ui.base.BaseView
import com.madison.move.ui.base.BasePresenter

/**
 * Create by SonLe on 04/05/2023
 */
interface MainContract {
    interface View : BaseView {
        fun onSuccessGetToken(loginResponse: LoginResponse)
        fun onSuccessLogout(logoutResponse: LogoutResponse)
        fun onError(error: String)
    }

    interface Presenter : BasePresenter<View> {
        fun onGetTokenPresenter(email: String, password: String, fragment: DialogFragment)
        fun logoutRequest(token: String)
    }
}