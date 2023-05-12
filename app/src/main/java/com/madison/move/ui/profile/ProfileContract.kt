package com.madison.move.ui.profile

import com.madison.move.data.model.User
import com.madison.move.ui.base.BasePresenter
import com.madison.move.ui.base.BaseView

interface ProfileContract {
    interface ProfileView : BaseView {
        fun onShowLoading()
        fun onShowError(errorType: String)
        fun onSaveProfileClick()
    }

    interface Presenter: BasePresenter<ProfileView> {
        fun onShowLoadingPresenter()
        fun onShowErrorPresenter(errorType: String)
        fun onSaveProfileClickPresenter(userNewProfile: User)
    }

}