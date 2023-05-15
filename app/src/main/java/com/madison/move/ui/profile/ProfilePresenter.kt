package com.madison.move.ui.profile

import com.madison.move.data.model.User

class ProfilePresenter(override var view: ProfileContract.ProfileView?, var user:User) :
    ProfileContract.Presenter {

    companion object{
        const val FULL_NAME_AT_LEAST_4_CHARS = "FN_4_CH"
        const val USER_NAME_CONTAINS_WHITE_SPACE = "USER_WS"
    }

    override fun onShowLoadingPresenter() {
    }

    override fun onShowErrorPresenter(errorType: String) {
        view?.onShowError(errorType)
    }

    override fun onSaveProfileClickPresenter(userNewProfile:User) {
        if (userNewProfile.fullname.length < 4 && user.fullname.length != userNewProfile.fullname.length ){
          return onShowErrorPresenter(FULL_NAME_AT_LEAST_4_CHARS)
        }



        view?.onSaveProfileClick()

    }
}