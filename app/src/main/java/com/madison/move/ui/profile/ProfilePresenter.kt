package com.madison.move.ui.profile

import android.util.Log
import com.madison.move.data.DataManager
import com.madison.move.data.model.User
import com.madison.move.data.model.country.CountryResponse
import com.madison.move.data.model.state.StateResponse
import com.madison.move.data.model.update_profile.ProfileRequest
import com.madison.move.data.model.update_profile.UpdateProfileResponse
import com.madison.move.data.model.user_profile.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilePresenter(override var view: ProfileContract.ProfileView?) :
    ProfileContract.Presenter {

    private val dataManager: DataManager = DataManager.instance

    companion object {
        const val FULL_NAME_AT_LEAST_4_CHARS = "FN_4_CH"
        const val USER_NAME_AT_LEAST_4_CHARS = "US_4_CH"
        const val USER_NAME_LENGTH = "US_LTH"
        const val USER_NAME_INVALID = "US_INVALID"
        const val USER_NAME_FORMAT = "US_FORMAT"
        const val FULL_NAMESAKE = "FULL_NAMESAKE"

    }

    override fun onShowLoadingPresenter() {
    }

    override fun onShowErrorPresenter(errorType: String) {
        view?.onShowError(errorType)
    }

    override fun onSaveProfileClickPresenter(token: String, profileRequest: ProfileRequest) {
        val listAcceptChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"

        if (profileRequest.fullname?.length!! < 4 || profileRequest.fullname.length > 100) {
            return onShowErrorPresenter(FULL_NAME_AT_LEAST_4_CHARS)
        }

        if (profileRequest.username?.length!! < 4) {
            return onShowErrorPresenter(USER_NAME_AT_LEAST_4_CHARS)
        }

        if (profileRequest.username.length > 25) {
            return onShowErrorPresenter(USER_NAME_LENGTH)
        }

        if (!hasNumber(profileRequest.username.toString()) || !hasAlphabet(profileRequest.username.toString())) {
            return onShowErrorPresenter(USER_NAME_FORMAT)
        }

        for (s in profileRequest.username.toString()) {
            if (s !in listAcceptChar) {
                return onShowErrorPresenter(USER_NAME_INVALID)
            }
        }

        dataManager.movieRepository.updateProfileUser("Bearer $token", profileRequest)
            ?.enqueue(object : Callback<UpdateProfileResponse> {
                override fun onResponse(
                    call: Call<UpdateProfileResponse>,
                    profileResponse: Response<UpdateProfileResponse>
                ) {
                    if (profileResponse.body() != null) {
                        view?.onSuccessUpdateProfile(profileResponse.body()!!)
                    }

                    if (profileResponse.errorBody() != null) {
                        view?.onShowError(FULL_NAMESAKE)
                    }
                }

                override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                    view?.onErrorGetProfile(t.message ?: "")
                }
            })


    }

    override fun getProfileUserDataPresenter(token: String) {
        dataManager.movieRepository.getUserProfile("Bearer $token")
            ?.enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(
                    call: Call<ProfileResponse>, profileResponse: Response<ProfileResponse>
                ) {
                    if (profileResponse.body() != null) {
                        view?.onSuccessGetProfileData(profileResponse.body()!!)
                    }

                    if (profileResponse.errorBody() != null) {

                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    view?.onErrorGetProfile(t.message ?: "")
                }
            })
    }

    override fun getCountryDataPresenter() {
        dataManager.movieRepository.getCountryData()?.enqueue(object : Callback<CountryResponse> {
            override fun onResponse(
                call: Call<CountryResponse>, countryResponse: Response<CountryResponse>
            ) {
                if (countryResponse.body() != null) {
                    view?.onSuccessGetCountryData(countryResponse.body()!!)
                }

                if (countryResponse.errorBody() != null) {

                }
            }

            override fun onFailure(call: Call<CountryResponse>, t: Throwable) {
                view?.onErrorGetProfile(t.message ?: "")
            }
        })
    }

    override fun getStateDataPresenter(countryID: Int) {
        dataManager.movieRepository.getStateData(countryID)
            ?.enqueue(object : Callback<StateResponse> {
                override fun onResponse(
                    call: Call<StateResponse>, stateResponse: Response<StateResponse>
                ) {

                    if (stateResponse.body() != null) {
                        view?.onSuccessGetStateData(stateResponse.body()!!)
                    }

                    if (stateResponse.errorBody() != null) {

                    }
                }

                override fun onFailure(call: Call<StateResponse>, t: Throwable) {
                    view?.onErrorGetProfile(t.message ?: "")
                }
            })
    }

    private fun hasNumber(input: String): Boolean {
        val regex = Regex("[0-9]")
        return regex.containsMatchIn(input)
    }

    private fun hasAlphabet(input: String): Boolean {
        val regex = Regex("[A-Za-z]")
        return regex.containsMatchIn(input)
    }

}