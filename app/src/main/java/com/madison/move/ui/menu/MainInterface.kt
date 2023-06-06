package com.madison.move.ui.menu

interface MainInterface {
    fun onShowDisconnectDialog()
    fun isDeviceOnlineCheck(): Boolean
    fun onReloadUserInfoMenu()
    fun onShowProgressBar()
    fun onHideProgressBar()
}