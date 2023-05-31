package com.madison.move.ui.profile


import android.app.Activity
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.service.autofill.UserData
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.madison.move.R
import com.madison.move.data.model.User
import com.madison.move.data.model.country.CountryResponse
import com.madison.move.data.model.country.DataCountry
import com.madison.move.data.model.state.DataState
import com.madison.move.data.model.state.StateResponse
import com.madison.move.data.model.update_profile.ProfileRequest
import com.madison.move.data.model.update_profile.UpdateProfileResponse
import com.madison.move.data.model.user_profile.DataUser
import com.madison.move.data.model.user_profile.ProfileResponse
import com.madison.move.databinding.FragmentProfileBinding
import com.madison.move.ui.base.BaseFragment
import kotlinx.coroutines.awaitCancellation
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*


class ProfileFragment : BaseFragment<ProfilePresenter>(), ProfileContract.ProfileView {

    companion object {
        const val FULL_NAME_AT_LEAST_4_CHARS = "FN_4_CH"
        const val USER_NAME_CONTAINS_WHITE_SPACE = "USER_WS"
        const val USER_NAME_LENGTH = "US_LTH"
        const val STATE_NOT_IN_LIST = "STATE_NOT_IN_LIST"
        const val COUNTRY_NOT_IN_LIST = "COUNTRY_NOT_IN_LIST"
        const val USER_NAME_INVALID = "US_INVALID"
        const val USER_NAME_FORMAT = "US_FORMAT"

    }

    private var getSharedPreferences: SharedPreferences? = null
    private var tokenUser: String? = null

    private lateinit var binding: FragmentProfileBinding
    private var newFullName = ""
    private var newUserName = ""
    private var isChangeRadioButton = false
    private var isFillAllDoB = false
    private var newCountry = ""
    private var newState = ""
    private var newCity = ""

    private var listDataCountry: ArrayList<DataCountry>? = null
    private var listDataState: ArrayList<DataState>? = null
    private var userData: DataUser? = null

    private lateinit var arrayAdapter: ArrayAdapter<String>
    private val months = arrayOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )
    private val currentYear = Year.now().value
    private val years = (1900..currentYear).map { it.toString() }.toMutableList()

    override fun createPresenter(): ProfilePresenter = ProfilePresenter(this)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        //Get Token From Preferences
        getSharedPreferences = requireContext().getSharedPreferences(
            "tokenUser", AppCompatActivity.MODE_PRIVATE
        )
        tokenUser = getSharedPreferences?.getString("token", null)


        onHandleLogic()

        return binding.root
    }

    private fun onHandleLogic() {

        //Get Data From Server
        presenter?.apply {
            getCountryDataPresenter()
            getProfileUserDataPresenter(tokenUser.toString())
        }


        //Enable Button Setting when All Field are Fill
        binding.saveSettingBtn.isEnabled = isAllFieldsNotNull()
        binding.saveSettingBtn.setOnClickListener {
            tokenUser?.let { token ->
                presenter?.onSaveProfileClickPresenter(
                    token, getNewProfile()
                )
            }
        }


        //Handle Function
        handleRadioButton()
        hideHintTextInputLayout()
        handleUserInput()
        handlePickerImage()
    }

    private fun getNewProfile(): ProfileRequest {
        val newUserName = binding.editUsername.text.toString().trim()
        val newFullName = binding.editProfileFullName.text.toString().trim()
        val newCountry = binding.dropdownCountryText.text.toString().trim()
        val newState = binding.dropdownStateText.text.toString().trim()
        val newCity = binding.editProfileCity.text.toString().trim()


//        val newAvatar = binding.imgProfileUser.tag.toString()

        //Get Gender
        val newGender: Int = if (binding.radioMale.isChecked) {
            1
        } else if (binding.radioFemale.isChecked) {
            2
        } else {
            3
        }

        //Get DOB
        val date =
            "${binding.dropdownYearText.text}-${binding.dropdownMonthText.text}-${binding.dropdownDayText.text}".trim()

        val format = "yyyy-MM-dd"
        val newDob = convertStringToDate(date, format)

        //Get Country & State ID
        val countryID = getIdCountry(newCountry)
        val stateId = getIdState(newState)


        return ProfileRequest(
            newCity,
            newDob,
            countryID,
            newFullName,
            newGender,
            "",
            stateId,
            newUserName,
        )

    }

    private fun convertStringToDate(dateString: String, format: String): Date? {
        val dateFormat = SimpleDateFormat(format)
        return try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun isAllFieldsNotNull(): Boolean {

        val newDayOfDob = binding.dropdownDayText.text.toString().trim()
        val newMonthOfDob = binding.dropdownMonthText.text.toString().trim()
        val newYearOfDob = binding.dropdownYearText.text.toString().trim()

        newFullName = binding.editProfileFullName.text.toString().trim()
        newUserName = binding.editUsername.text.toString().trim()
        newCountry = binding.dropdownCountryText.text.toString().trim()
        newState = binding.dropdownStateText.text.toString().trim()
        newCity = binding.editProfileCity.text.toString().trim()

        if (newDayOfDob.isNotEmpty() && newMonthOfDob.isNotEmpty() && newYearOfDob.isNotEmpty()) {
            isFillAllDoB = true
        }
        if (binding.radioMale.isChecked || binding.radioFemale.isChecked || binding.radioRatherNotSay.isChecked) {
            isChangeRadioButton = true
        }

        return newFullName.isNotEmpty() && newUserName.isNotEmpty() && newCountry.isNotEmpty() && newState.isNotEmpty() && newCity.isNotEmpty() && isChangeRadioButton && isFillAllDoB


    }

    private fun handleUserInput() {
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.editUsername.setBackgroundResource(R.drawable.custom_edittext)
                binding.editProfileFullName.setBackgroundResource(R.drawable.custom_edittext)
                binding.txtErrorUsername.visibility = View.GONE
                binding.txtErrorFullName.visibility = View.GONE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val listSpecialCharacter = "!@#$%^&*()_-+={}][|<>,?/.®©€¥£¢"

                //Handle  Input Full Name From User

                val nameText = binding.editProfileFullName.text.toString()
                val countDotChar = nameText.count { it == '.' }
                if (countDotChar >= 2) {
                    return binding.editProfileFullName.setText(nameText.dropLast(1))
                }

                if (nameText.contains("  ") || nameText.contains(" .")) {
                    return binding.editProfileFullName.setText(nameText.dropLast(1))
                }

                for (s in listSpecialCharacter) {
                    if (nameText.startsWith(s)) {
                        return binding.editProfileFullName.setText(nameText.dropLast(1))
                    }
                }


                //

                //Handle Input City From User
                val cityText = binding.editProfileCity.text.toString()
                val matches = arrayOf("  ", "..", ",,", "--", " ,", " .", "- -", "//", " /", "/ ")

                for (s in matches) {
                    if (cityText.contains(s)) {
                        return binding.editProfileCity.setText(cityText.dropLast(1))
                    }
                }

                for (s in listSpecialCharacter) {
                    if (cityText.startsWith(s)) {
                        return binding.editProfileCity.setText(cityText.dropLast(1))
                    }
                }

                //Handle Enable Save Setting Button
                binding.saveSettingBtn.isEnabled = isAllFieldsNotNull()

            }

            override fun afterTextChanged(s: Editable?) {


                binding.editProfileFullName.setSelection(binding.editProfileFullName.length())
                binding.editProfileCity.setSelection(binding.editProfileCity.length())
            }

        }

        binding.editUsername.addTextChangedListener(textWatcher)
        binding.editProfileFullName.addTextChangedListener(textWatcher)
        binding.editProfileCity.addTextChangedListener(textWatcher)
    }

    private var mProfileUri: Uri? = null
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                mProfileUri = uri
                binding.imgProfileUser.setLocalImage(uri, false)
            } else {
                parseError(it)
            }
        }

    private fun parseError(activityResult: ActivityResult) {
        if (activityResult.resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(activity, ImagePicker.getError(activityResult.data), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(activity, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handlePickerImage() {
        binding.txtProfileUpdatePicture.setOnClickListener {
            activity?.let { it1 ->
                ImagePicker.with(it1).crop().cropOval().maxResultSize(1000, 1000, false)
                    .provider(ImageProvider.BOTH) // Or bothCameraGallery()
                    .setDismissListener {
                        Log.d("ImagePicker", "onDismiss")
                    }.createIntentFromDialog { launcher.launch(it) }
            }
        }
    }


    private fun setUserData(user: DataUser) {

        binding.editUsername.setText(user.username)
        binding.editProfileEmail.setText(user.email)
        binding.editProfileFullName.setText(user.fullname)
        binding.editProfileCity.setText(user.address)

        //Set User Avatar
        if (user.img != null) {
            Glide.with(this).load(user.img).into(binding.imgProfileUser)
        } else {
            binding.imgProfileUser.setImageResource(R.drawable.avatar)
        }

        //Set User Gender
        when (user.gender) {
            1 -> {
                binding.radioMale.isChecked = true
            }
            2 -> {
                binding.radioFemale.isChecked = true

            }
            3 -> {
                binding.radioRatherNotSay.isChecked = true
            }
        }

        //Set User Dob
        if (user.birthday != null) {

            val dateParts = user.birthday.split("-")
            val year = dateParts[0]
            val month: String = dateParts[1]
            val day = dateParts[2]

            binding.dropdownYearText.setText(year)
            binding.dropdownDayText.setText(day)

            when (month) {
                "01" -> binding.dropdownMonthText.setText(months[0])
                "02" -> binding.dropdownMonthText.setText(months[1])
                "03" -> binding.dropdownMonthText.setText(months[2])
                "04" -> binding.dropdownMonthText.setText(months[3])
                "05" -> binding.dropdownMonthText.setText(months[4])
                "06" -> binding.dropdownMonthText.setText(months[5])
                "07" -> binding.dropdownMonthText.setText(months[6])
                "08" -> binding.dropdownMonthText.setText(months[7])
                "09" -> binding.dropdownMonthText.setText(months[8])
                "10" -> binding.dropdownMonthText.setText(months[9])
                "11" -> binding.dropdownMonthText.setText(months[10])
                "12" -> binding.dropdownMonthText.setText(months[11])
                "13" -> binding.dropdownMonthText.setText(months[12])
            }
        }
        listDataCountry?.forEach { it ->
            if (it.id == user.countryId) {
                binding.dropdownCountryText.setText(it.name)

                //Add data again
                if (listDataCountry != null) {
                    handleDropDownCountry(listDataCountry!!)
                }
            }
        }

        //Handle Dob Selected
        handleDropDownDob()
        val monthSelected = binding.dropDownProfileMonth.editText?.text.toString()
        val yearSelected = binding.dropDownProfileYear.editText?.text.toString()
        onHandleListOfDay(monthSelected, yearSelected)

    }

    override fun onShowLoading() {

    }

    override fun onSuccessGetProfileData(profileResponse: ProfileResponse) {
        userData = profileResponse.dataUser
        listDataCountry?.forEach {
            if (it.id != null && it.id == userData?.countryId) presenter?.getStateDataPresenter(it.id)
        }
        if (userData != null && listDataCountry != null) {
            userData?.let { setUserData(it) }
        }
    }

    override fun onSuccessGetCountryData(countryResponse: CountryResponse) {
        handleDropDownCountry(countryResponse.dataCountry as ArrayList<DataCountry>)
        listDataCountry = countryResponse.dataCountry
        listDataCountry?.forEach {
            if (it.id != null && it.id == userData?.countryId) presenter?.getStateDataPresenter(it.id)
        }
        if (userData != null && listDataCountry != null) {
            userData?.let { setUserData(it) }
        }
    }

    override fun onSuccessGetStateData(stateResponse: StateResponse) {
        handleDropDownState(stateResponse.data as ArrayList<DataState>)
        listDataState = stateResponse.data
        if (userData?.stateId != null) {
            //Set State Infomation
            listDataState?.forEach {
                if (it.id == userData!!.stateId) {
                    binding.dropdownStateText.setText(it.name)
                    if (listDataState != null) {
                        handleDropDownState(listDataState!!)
                    }
                }
            }
            binding.saveSettingBtn.isEnabled = isAllFieldsNotNull()
        }
    }

    override fun onSuccessUpdateProfile(updateProfileResponse: UpdateProfileResponse) {
        binding.txtErrorFullName.visibility = View.GONE
        binding.txtErrorFullName.focusable = View.FOCUSABLE
        Toast.makeText(
            activity?.applicationContext,
            updateProfileResponse.message.toString(),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onErrorGetProfile(errorType: String) {
        Toast.makeText(activity, errorType, Toast.LENGTH_SHORT).show()
    }

    override fun onShowError(errorType: String) {
        when (errorType) {
            FULL_NAME_AT_LEAST_4_CHARS -> {
                binding.txtErrorFullName.apply {
                    visibility = View.VISIBLE
                    text = context.getString(R.string.error_fullname_chars)
                }
                binding.editProfileFullName.apply {
                    requestFocus()
                    setBackgroundResource(R.drawable.custom_edittext_error)
                }
            }

            USER_NAME_LENGTH -> {
                binding.txtErrorUsername.apply {
                    visibility = View.VISIBLE
                    text = context.getString(R.string.error_user_name)
                }
                binding.editUsername.apply {
                    setBackgroundResource(R.drawable.custom_edittext_error)
                    requestFocus()
                }

            }

            USER_NAME_INVALID -> {
                binding.txtErrorUsername.apply {
                    visibility = View.VISIBLE
                    text = context.getString(R.string.txt_error_spc_chars)
                }
                binding.editUsername.apply {
                    setBackgroundResource(R.drawable.custom_edittext_error)
                    requestFocus()
                }
            }

            USER_NAME_FORMAT -> {
                binding.txtErrorUsername.apply {
                    visibility = View.VISIBLE
                    text = context.getString(R.string.txt_error_contains_alpnum)
                }
                binding.editUsername.apply {
                    setBackgroundResource(R.drawable.custom_edittext_error)
                    requestFocus()
                }
            }

            STATE_NOT_IN_LIST -> {

            }

            COUNTRY_NOT_IN_LIST -> {

            }

            USER_NAME_CONTAINS_WHITE_SPACE -> {

            }
        }
    }


    private fun hideHintTextInputLayout() {
        binding.dropDownProfileCountry.isHintEnabled = false
        binding.dropDownProfileState.isHintEnabled = false
        binding.dropDownProfileDay.isHintEnabled = false
        binding.dropDownProfileMonth.isHintEnabled = false
        binding.dropDownProfileYear.isHintEnabled = false
    }

    private fun handleDropDownDob() {
        binding.dropdownMonthText.inputType = EditorInfo.TYPE_NULL
        binding.dropdownYearText.inputType = EditorInfo.TYPE_NULL

        onMonthSelected()
        onYearSelected()

        val monthAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, months)

        val yearAdapter =
            ArrayAdapter(requireContext(), R.layout.item_dropdown, years.sortedDescending())

        binding.dropdownMonthText.setAdapter(monthAdapter)
        binding.dropdownYearText.setAdapter(yearAdapter)


    }

    private fun onYearSelected() {
        binding.dropdownYearText.setOnItemClickListener { parent, _, position, _ ->
            val monthSelected = binding.dropDownProfileMonth.editText?.text.toString()
            val yearSelected: String = parent.getItemAtPosition(position).toString()
            onHandleListOfDay(monthSelected, yearSelected)

            binding.dropdownDayText.text.clear()
            binding.saveSettingBtn.isEnabled = false

        }
    }

    private fun onMonthSelected() {
        binding.dropdownMonthText.setOnItemClickListener { parent, _, position, _ ->
            val yearSelected = binding.dropDownProfileYear.editText?.text.toString()
            val monthSelected: String = parent.getItemAtPosition(position).toString()
            if (yearSelected != "") {
                onHandleListOfDay(monthSelected, yearSelected)
            }

            binding.dropdownDayText.text.clear()
            binding.saveSettingBtn.isEnabled = false

        }


    }


    private fun onHandleListOfDay(monthSelected: String, yearSelected: String) {
        binding.dropdownDayText.inputType = EditorInfo.TYPE_NULL
        val days: MutableList<String>
        if (isThirtyDaysMonth(monthSelected)) {
            days = (1..30).map { it.toString() }.toMutableList()
        } else if (monthSelected == "Feb") {
            days = if (isLeapYear(yearSelected.toInt())) {
                (1..29).map { it.toString() }.toMutableList()
            } else {
                (1..28).map { it.toString() }.toMutableList()
            }
        } else {
            days = if (binding.dropdownMonthText.text.toString()
                    .isNotEmpty() || binding.dropdownYearText.text.toString().isNotEmpty()
            ) {
                (1..31).map { it.toString() }.toMutableList()
            } else {
                mutableListOf()
            }
        }

        val dayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, days)
        binding.dropdownDayText.setAdapter(dayAdapter)


        binding.dropdownDayText.setOnItemClickListener { parent, view, position, id ->
            binding.saveSettingBtn.isEnabled = isAllFieldsNotNull()
        }

    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)
    }

    private fun isThirtyDaysMonth(month: String): Boolean {
        return (month in arrayOf(
            "Apr",
            "Jun",
            "Sep",
            "Nov",
        ))
    }


    private fun handleDropDownState(listDataState: ArrayList<DataState>) {
        val listState = ArrayList<String>()
        listDataState.forEach {
            it.name?.let { it1 -> listState.add(it1) }
        }


        arrayAdapter = context?.let {
            ArrayAdapter(
                it.applicationContext, R.layout.item_dropdown, listState
            )
        }!!

        binding.dropdownStateText.setAdapter(arrayAdapter)
        binding.dropdownStateText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val inputStateText = binding.dropdownStateText.text.toString().trim()
                if (inputStateText !in listState) {
                    binding.dropdownStateText.text.clear()
                }
                binding.saveSettingBtn.isEnabled = isAllFieldsNotNull()
            }
        }
    }

    private fun handleDropDownCountry(listDataCountry: ArrayList<DataCountry>) {

        val listCountry = ArrayList<String>()
        listDataCountry.forEach {
            it.name?.let { it1 -> listCountry.add(it1) }
        }

        arrayAdapter = context?.let {
            ArrayAdapter(
                it.applicationContext, R.layout.item_dropdown, listCountry
            )
        }!!

        binding.dropdownCountryText.setAdapter(arrayAdapter)
//        binding.dropdownCountryText.inputType = EditorInfo.TYPE_NULL


        binding.dropdownCountryText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val inputStateText = binding.dropdownCountryText.text.toString().trim()
                if (inputStateText !in listCountry) {
                    binding.dropdownCountryText.text.clear()
                }
                binding.saveSettingBtn.isEnabled = isAllFieldsNotNull()
            }
        }

        binding.dropdownCountryText.setOnItemClickListener { parent, view, position, id ->
            binding.dropdownStateText.text.clear()
            val countrySelected = binding.dropDownProfileCountry.editText?.text.toString()
            //Get Id Of Country
            getIdCountry(countrySelected)?.let { presenter?.getStateDataPresenter(it) }
        }
    }

    private fun getIdCountry(countryName: String): Int? {
        listDataCountry?.forEach {
            if (countryName == it.name) {
                return it.id
            }
        }
        return 0
    }

    private fun getIdState(stateName: String): Int? {
        listDataState?.forEach {
            if (stateName == it.name) {
                return it.id
            }
        }
        return 0
    }

    private fun handleRadioButton() {
        binding.radioMale.setOnClickListener {
            onRadioButtonClicked(binding.radioMale)
        }

        binding.radioFemale.setOnClickListener {
            onRadioButtonClicked(binding.radioFemale)
        }

        binding.radioRatherNotSay.setOnClickListener {
            onRadioButtonClicked(binding.radioRatherNotSay)
        }
    }

    private fun onRadioButtonClicked(view: View) {
        val idRadioMale = binding.radioMale.id
        val idRadioFemale = binding.radioFemale.id
        val idRadioRather = binding.radioRatherNotSay.id

        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.id) {
                idRadioMale -> if (checked) {
                    binding.radioFemale.isChecked = false
                    binding.radioRatherNotSay.isChecked = false
                }
                idRadioFemale -> if (checked) {
                    binding.radioMale.isChecked = false
                    binding.radioRatherNotSay.isChecked = false
                }
                idRadioRather -> if (checked) {
                    binding.radioMale.isChecked = false
                    binding.radioFemale.isChecked = false
                }
            }
        }
        binding.saveSettingBtn.isEnabled = isAllFieldsNotNull()

    }


}