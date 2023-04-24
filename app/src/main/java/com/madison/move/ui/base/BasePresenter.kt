package com.madison.move.ui.base

abstract class BasePresenter<View : BaseView?> protected constructor(view: View) {
    protected var view: View?

    init {
        this.view = view
    }

    fun onDetach() {
        view = null //avoid memory leak
    }
}