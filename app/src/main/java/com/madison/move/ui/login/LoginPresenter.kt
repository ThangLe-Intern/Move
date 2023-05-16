package com.madison.move.ui.login

import com.madison.move.data.model.User

class LoginPresenter(
    override var view: LoginContract.LoginView?
) : LoginContract.Presenter {

    companion object {
        const val EMAIL_INVALID = "EMAIL_INVALID"
        const val EMAIL_CONTAIN_SPACE = "EMAIL_CONTAIN_SPACE"
        const val PASSWORD_CONTAIN_SPACE = "PASSWORD_CONTAIN_SPACE"
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

    override fun onLoginClickPresenter(email: String, password: String, user: User) {

        if (!isEmailValid(email)) {
            onShowErrorPresenter(EMAIL_INVALID)
        } else if (email.contains(" ")) {
            onShowErrorPresenter(EMAIL_CONTAIN_SPACE)
        } else if (password.contains(" ")) {
            onShowErrorPresenter(PASSWORD_CONTAIN_SPACE)
        } else if (email != "vudung@gmail.com" || password != "123") {
            onShowErrorPresenter(INCORRECT_ACCOUNT)
        } else {
            user.role = true
            view?.onLoginClick(user)
        }


    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}