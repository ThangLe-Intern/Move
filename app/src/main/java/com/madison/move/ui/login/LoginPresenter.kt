package com.madison.move.ui.login

class LoginPresenter(
    override var view: LoginContract.LoginView?
) : LoginContract.Presenter {
    override fun onShowLoadingPresenter() {

    }

    override fun onEnableButtonLoginPresenter() {
        view?.onEnableButtonLogin()
    }

    override fun onShowErrorPresenter(errorType: String) {
        view?.onShowError(errorType)
    }

    override fun onLoginClickPresenter(email:String,password:String) {

        if (!isEmailValid(email)){
            onShowErrorPresenter("Invalid Email")
        }else if (email.contains(" ")){
            onShowErrorPresenter( "Email contains White Space")
        }else if (password.contains(" ")){
            onShowErrorPresenter("Password contains White Space")
        }else if (email != "nguyenvudung030121@gmail.com" || password != "1"){
            onShowErrorPresenter("NotAccount")
        }else{
            view?.onLoginClick()
        }


    }

    fun isEmailValid(email: String): Boolean { return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() }

}