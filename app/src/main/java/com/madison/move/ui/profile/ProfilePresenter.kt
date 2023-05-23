package com.madison.move.ui.profile

import com.madison.move.data.model.User

class ProfilePresenter(override var view: ProfileContract.ProfileView?, var user:User) :
    ProfileContract.Presenter {

    companion object{
        const val FULL_NAME_AT_LEAST_4_CHARS = "FN_4_CH"
        const val USER_NAME_LENGTH = "US_LTH"
        const val USER_NAME_INVALID = "US_INVALID"
        const val USER_NAME_FORMAT = "US_FORMAT"
    }

    override fun onShowLoadingPresenter() {
    }

    override fun onShowErrorPresenter(errorType: String) {
        view?.onShowError(errorType)
    }

    override fun onSaveProfileClickPresenter(userNewProfile:User) {
        val listAcceptChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"

        if (userNewProfile.fullname?.length!! < 4 || userNewProfile.fullname?.length!! >100 ){
          return onShowErrorPresenter(FULL_NAME_AT_LEAST_4_CHARS)
        }
        if (userNewProfile.username?.length!! < 4 || userNewProfile.username?.length!! > 25){
            return onShowErrorPresenter(USER_NAME_LENGTH)
        }

        if (!hasNumber(userNewProfile.username.toString())){
            return onShowErrorPresenter(USER_NAME_FORMAT)
        }

        for (s in userNewProfile.username.toString()) {
            if (s !in listAcceptChar) {
                return onShowErrorPresenter(USER_NAME_INVALID)
            }
        }

        view?.onSaveProfileClick()

    }

    private fun hasNumber(input: String): Boolean {
        val regex = Regex("[0-9]")
        return regex.containsMatchIn(input)
    }

}