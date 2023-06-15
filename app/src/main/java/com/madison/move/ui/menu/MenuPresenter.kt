package com.madison.move.ui.menu

import com.madison.move.data.DataManager
import com.madison.move.data.model.ObjectResponse
import com.madison.move.data.model.DataUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuPresenter(override var view: MainContract.View?) : MainContract.Presenter {
    private val dataManager: DataManager = DataManager.instance
    override fun onGetTokenPresenter(email: String, password: String) {
        dataManager.movieRepository.getTokenLogin(email, password)
            ?.enqueue(object : Callback<ObjectResponse<DataUser>> {
                override fun onResponse(
                    call: Call<ObjectResponse<DataUser>>, loginResponse: Response<ObjectResponse<DataUser>>
                ) {
                    if (loginResponse.body() != null) {
                        loginResponse.code()
                        view?.onSuccessGetToken(loginResponse.body()!!)
                    }

                    if (loginResponse.errorBody() != null) {
                        loginResponse.code()
                        view?.onError(loginResponse.code().toString() )
                    }
                }

                override fun onFailure(call: Call<ObjectResponse<DataUser>>, t: Throwable) {
                }
            })
    }

    override fun logoutRequest(token: String) {
        dataManager.movieRepository.logOutUser("Bearer $token")
            ?.enqueue(object : Callback<ObjectResponse<DataUser>> {
                override fun onResponse(
                    call: Call<ObjectResponse<DataUser>>, logoutResponse: Response<ObjectResponse<DataUser>>
                ) {
                    if (logoutResponse.body() != null) {
                        view?.onSuccessLogout(logoutResponse.body()!!)
                    }
                }

                override fun onFailure(call: Call<ObjectResponse<DataUser>>, t: Throwable) {
                    t.message?.let { view?.onError(it) }
                }
            })
    }

}