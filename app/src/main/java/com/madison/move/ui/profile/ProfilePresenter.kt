package com.madison.move.ui.profile

import com.madison.move.data.DataManager
import com.madison.move.data.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilePresenter(override var view: ProfileContract.ProfileView?) :
    ProfileContract.Presenter {

    private val dataManager: DataManager = DataManager.instance

    companion object {
        const val FULL_NAME_LENGTH = "FN_LTH"
        const val FULL_NAME_FORMAT = "FN_FM"
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
        val listSpecialCharacter = "!@#$%^&*()_-+={}][|<>,?/.®©€¥£¢1234567890"
        if (profileRequest.fullname?.length!! < 4 ) {
            return onShowErrorPresenter(FULL_NAME_AT_LEAST_4_CHARS)
        }

        if (profileRequest.fullname.length > 100){
            return onShowErrorPresenter(FULL_NAME_LENGTH)
        }

        for (s in profileRequest.fullname.toString()) {
            if (s in listSpecialCharacter) {
                return onShowErrorPresenter(FULL_NAME_FORMAT)
            }
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
            ?.enqueue(object : Callback<ObjectResponse<DataUser>> {
                override fun onResponse(
                    call: Call<ObjectResponse<DataUser>>,
                    profileResponse: Response<ObjectResponse<DataUser>>
                ) {
                    if (profileResponse.body() != null) {
                        view?.onSuccessUpdateProfile(profileResponse.body()!!)
                    }

                    if (profileResponse.errorBody() != null) {
                        view?.onShowError(FULL_NAMESAKE)
                    }
                }

                override fun onFailure(call: Call<ObjectResponse<DataUser>>, t: Throwable) {
                    view?.onErrorGetProfile(t.message ?: "")
                }
            })


    }

    override fun getProfileUserDataPresenter(token: String) {
        dataManager.movieRepository.getUserProfile("Bearer $token")
            ?.enqueue(object : Callback<ObjectResponse<DataUser>> {
                override fun onResponse(
                    call: Call<ObjectResponse<DataUser>>,
                    profileResponse: Response<ObjectResponse<DataUser>>
                ) {
                    if (profileResponse.body() != null) {
                        view?.onSuccessGetProfileData(profileResponse.body()!!)
                    }

                    if (profileResponse.errorBody() != null){
                        view?.onErrorGetProfile(profileResponse.message())
                    }

                }

                override fun onFailure(call: Call<ObjectResponse<DataUser>>, t: Throwable) {
                    view?.onErrorGetProfile(t.message ?: "")
                }
            })
    }

    override fun getCountryDataPresenter() {
        dataManager.movieRepository.getCountryData()
            ?.enqueue(object : Callback<ObjectResponse<List<DataCountry>>> {
                override fun onResponse(
                    call: Call<ObjectResponse<List<DataCountry>>>,
                    countryResponse: Response<ObjectResponse<List<DataCountry>>>
                ) {
                    if (countryResponse.body() != null) {
                        view?.onSuccessGetCountryData(countryResponse.body()!!)
                    }
                    if (countryResponse.errorBody() != null) {
                        view?.onErrorGetProfile(countryResponse.message())
                    }
                }

                override fun onFailure(
                    call: Call<ObjectResponse<List<DataCountry>>>,
                    t: Throwable
                ) {
                    view?.onErrorGetProfile(t.message ?: "")
                }
            })
    }

    override fun getStateDataPresenter(countryID: Int) {
        dataManager.movieRepository.getStateData(countryID)
            ?.enqueue(object : Callback<ObjectResponse<List<DataState>>> {
                override fun onResponse(
                    call: Call<ObjectResponse<List<DataState>>>,
                    stateResponse: Response<ObjectResponse<List<DataState>>>
                ) {

                    if (stateResponse.body() != null) {
                        view?.onSuccessGetStateData(stateResponse.body()!!)
                    }

                    if (stateResponse.errorBody() != null) {
                        view?.onErrorGetProfile(stateResponse.message())
                    }
                }

                override fun onFailure(call: Call<ObjectResponse<List<DataState>>>, t: Throwable) {
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