package com.madison.move.ui.login

class LoginPresenter(
    override var view: LoginContract.LoginView?
): LoginContract.Presenter {
    override fun onShowLoadingPresenter() {

    }

    override fun onEnableButtonLoginPresenter() {
            view?.onEnableButtonLogin()
    }

    override fun onLoginClick() {

    }
}