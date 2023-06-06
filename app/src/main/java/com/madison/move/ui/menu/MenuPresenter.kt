package com.madison.move.ui.menu

import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.madison.move.R
import com.madison.move.data.DataManager
import com.madison.move.data.model.login.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuPresenter(override var view: MainContract.View?) : MainContract.Presenter {
    private val dataManager: DataManager = DataManager.instance
    override fun onGetTokenPresenter(email: String, password: String, fragment: DialogFragment) {
        dataManager.movieRepository.getTokenLogin(email, password)
            ?.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>, loginResponse: Response<LoginResponse>
                ) {
                    if (loginResponse.body() != null) {
                        view?.onSuccessGetToken(loginResponse.body()!!)
                        fragment.dismiss()
                    }

                    if (loginResponse.errorBody() != null) {
                        fragment.view?.findViewById<RelativeLayout>(R.id.layout_error_message)?.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                }
            })
    }

}