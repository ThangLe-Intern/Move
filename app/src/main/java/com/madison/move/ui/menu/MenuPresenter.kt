package com.madison.move.ui.menu

import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.madison.move.R
import com.madison.move.data.DataManager
import com.madison.move.data.model.login.LoginResponse
import com.madison.move.data.model.logout.LogoutResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuPresenter(override var view: MainContract.View?) : MainContract.Presenter {
    private val dataManager: DataManager = DataManager.instance
    override fun onGetTokenPresenter(email: String, password: String) {
        dataManager.movieRepository.getTokenLogin(email, password)
            ?.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>, loginResponse: Response<LoginResponse>
                ) {
                    if (loginResponse.body() != null) {
                        view?.onSuccessGetToken(loginResponse.body()!!)
                    }

                    if (loginResponse.errorBody() != null) {
                        view?.onError(null)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                }
            })
    }

    override fun logoutRequest(token: String) {
        dataManager.movieRepository.logOutUser("Bearer $token")
            ?.enqueue(object : Callback<LogoutResponse> {
                override fun onResponse(
                    call: Call<LogoutResponse>, logoutResponse: Response<LogoutResponse>
                ) {
                    if (logoutResponse.body() != null) {
                        view?.onSuccessLogout(logoutResponse.body()!!)
                    }
                }

                override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                    t.message?.let { view?.onError(it) }
                }
            })
    }

}