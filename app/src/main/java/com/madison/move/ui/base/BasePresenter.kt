package com.madison.move.ui.base

interface BasePresenter<View : BaseView?> {
    var view: View?

    fun onDetach() {
        view = null //avoid memory leak
    }
}