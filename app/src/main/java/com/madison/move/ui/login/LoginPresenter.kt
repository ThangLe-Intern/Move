package com.madison.move.ui.login

import android.util.Log
import com.madison.move.data.DataManager
import com.madison.move.data.model.login.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(
    override var view: LoginContract.LoginView?
) : LoginContract.Presenter {

    companion object {
        const val EMAIL_INVALID = "EMAIL_INVALID"
        const val EMAIL_CONTAIN_SPACE = "EMAIL_CONTAIN_SPACE"
        const val PASSWORD_CONTAIN_SPACE = "PASSWORD_CONTAIN_SPACE"
        const val PASSWORD_NULL = "PASSWORD_NULL"
        const val EMAIL_NULL = "EMAIL_NULL"
        const val PASSWORD_EMAIL_NULL = "PASSWORD_EMAIL_NULL"
        const val INCORRECT_ACCOUNT = "INCORRECT_ACCOUNT"
    }


    override fun onShowLoadingPresenter() {

    }

    override fun onEnableButtonLoginPresenter() {
        view?.onEnableButtonLogin()
    }

    override fun onShowErrorPresenter(errorType: String) {
        view?.onShowError(errorType)
    }

    override fun onLoginClickPresenter(email: String, password: String) {
        if (password.isEmpty() || email.isEmpty()) {
            if (password.isEmpty() && email.isEmpty())
                return onShowErrorPresenter(PASSWORD_EMAIL_NULL)
            if (email.isEmpty()) return onShowErrorPresenter(EMAIL_NULL)
            return onShowErrorPresenter(PASSWORD_NULL)
        } else if (!isEmailValid(email)) {
            onShowErrorPresenter(EMAIL_INVALID)
        } else if (email.contains(" ")) {
            onShowErrorPresenter(EMAIL_CONTAIN_SPACE)
        } else if (password.contains(" ")) {
            onShowErrorPresenter(PASSWORD_CONTAIN_SPACE)
        } else {
            view?.onSendDataToActivity(email, password)
        }
    }



    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}