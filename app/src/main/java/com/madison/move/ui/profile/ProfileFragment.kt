package com.madison.move.ui.profile


import android.app.Activity
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.madison.move.R
import com.madison.move.data.model.*
import com.madison.move.databinding.FragmentProfileBinding
import com.madison.move.ui.base.BaseFragment
import com.madison.move.ui.menu.MainMenuActivity
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*


class ProfileFragment : BaseFragment<ProfilePresenter>(), ProfileContract.ProfileView {

    companion object {
        const val FULL_NAME_AT_LEAST_4_CHARS = "FN_4_CH"
        const val USER_NAME_CONTAINS_WHITE_SPACE = "USER_WS"
        const val USER_NAME_AT_LEAST_4_CHARS = "US_4_CH"
        const val USER_NAME_LENGTH = "US_LTH"
        const val STATE_NOT_IN_LIST = "STATE_NOT_IN_LIST"
        const val COUNTRY_NOT_IN_LIST = "COUNTRY_NOT_IN_LIST"
        const val USER_NAME_INVALID = "US_INVALID"
        const val USER_NAME_FORMAT = "US_FORMAT"
        const val USERNAME_NAMESAKE = "FULL_NAMESAKE"
        const val TOKEN_USER_PREFERENCE = "tokenUser"
        const val TOKEN = "token"
        const val USER_DATA = "user"
        const val FULL_NAME_LENGTH = "FN_LTH"
        const val FULL_NAME_FORMAT = "FN_FM"
    }

    private var getSharedPreferences: SharedPreferences? = null
    private var tokenUser: String? = null
    private var isOpenGallery = false
    private var mProfileUri: Uri? = null

    private lateinit var binding: FragmentProfileBinding
    private var newFullName = ""
    private var newUserName = ""
    private var isChangeRadioButton = false
    private var isFillAllDoB = false
    private var newCountry = ""
    private var newState = ""
    private var newCity = ""
    private var avatarUrl: String? = null
    private var listDataCountry: ArrayList<DataCountry>? = null
    private var listDataState: ArrayList<DataState>? = null
    private var userData: DataUser? = null

    private lateinit var arrayAdapter: ArrayAdapter<String>
    private val months = arrayOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )
    private val currentYear = Year.now().value
    private val years = (1900..currentYear).map { it.toString() }.toMutableList()

    //Firebase
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null

    override fun createPresenter(): ProfilePresenter = ProfilePresenter(this)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        storage = FirebaseStorage.getInstance()
        storageReference = storage?.reference


        //Get Token From Preferences
        getSharedPreferences = requireContext().getSharedPreferences(
            TOKEN_USER_PREFERENCE, AppCompatActivity.MODE_PRIVATE
        )
        tokenUser = getSharedPreferences?.getString(TOKEN, null)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (!isOpenGallery) {
            mListener?.onShowProgressBar()
            onHandleLogic()
        }
    }

    private fun onHandleLogic() {

        //Get Data From Server
        presenter?.apply {
            getCountryDataPresenter()
            getProfileUserDataPresenter(tokenUser ?: "")
        }


        //Enable Button Setting when All Field are Fill
        binding.saveSettingBtn.isEnabled = isAllFieldsNotNull()

        binding.saveSettingBtn.setOnClickListener {
            isOpenGallery = false
            if (mListener?.isDeviceOnlineCheck() == false) {
                mListener?.onShowDisconnectDialog()
            } else {
                mListener?.onShowProgressBar()

                if (mProfileUri != null) {
                    uploadImageToFireBase()
                } else {
                    tokenUser?.let { token ->
                        presenter?.onSaveProfileClickPresenter(
                            token, getNewProfile()
                        )
                    }
                }
            }
        }


        //Handle Function
        handleRadioButton()
        hideHintTextInputLayout()
        handleUserInput()
        handlePickerImage()
    }

    private fun uploadImageToFireBase() {

        //Make Image Name Base On Date
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CHINA)
        val dateNow = Date()
        val filename = formatter.format(dateNow)

        val imgRef = storageReference?.child("images/${filename}.jpg")
        mProfileUri?.let { uri ->
            imgRef?.putFile(uri)
                ?.addOnSuccessListener { taskSnapshot ->
                    imgRef.downloadUrl.addOnSuccessListener {
                        if (it != null) {
                            avatarUrl = it.toString()
                            Log.d("HEHE", avatarUrl.toString())

                            //Call Update Profile
                            tokenUser?.let { token ->
                                presenter?.onSaveProfileClickPresenter(
                                    token, getNewProfile()
                                )
                            }
                        } else {
                            mListener?.onHideProgressBar()
                            Toast.makeText(activity, "Get URL Failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                ?.addOnFailureListener { exception ->
                    mListener?.onHideProgressBar()
                    Toast.makeText(activity, "Upload Image Failed", Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun getNewProfile(): ProfileRequest {
        val newUserName = binding.editUsername.text.toString().trim()
        val newFullName = binding.editProfileFullName.text.toString().trim()
        val newCountry = binding.dropdownCountryText.text.toString().trim()
        val newState = binding.dropdownStateText.text.toString().trim()
        val newCity = binding.editProfileCity.text.toString().trim()

        //Get Gender
        val newGender: Int = if (binding.radioMale.isChecked) {
            1
        } else if (binding.radioFemale.isChecked) {
            2
        } else {
            3
        }

        val newMonth: String = when (binding.dropdownMonthText.text.toString()) {
            months[0] -> "01"
            months[1] -> "02"
            months[2] -> "03"
            months[3] -> "04"
            months[4] -> "05"
            months[5] -> "06"
            months[6] -> "07"
            months[7] -> "08"
            months[8] -> "09"
            months[9] -> "10"
            months[10] -> "11"
            months[11] -> "12"
            else -> "01"
        }

        //Get DOB
        val date =
            "${binding.dropdownYearText.text}-${newMonth}-${binding.dropdownDayText.text}".trim()


        //Get Country & State ID
        val countryID = getIdCountry(newCountry)
        val stateId = getIdState(newState)


        return ProfileRequest(
            newCity,
            date,
            countryID,
            newFullName,
            newGender,
            avatarUrl,
            stateId,
            newUserName,
        )

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

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                isOpenGallery = true
                val uri = it.data?.data
                mProfileUri = uri
                if (uri != null) {
                    binding.imgProfileUser.setLocalImage(uri, false)
                }
            } else {
                parseError(it)
            }
        }

    private fun handlePickerImage() {
        binding.txtProfileUpdatePicture.setOnClickListener {
            activity?.let { it1 ->
                ImagePicker.with(it1).crop().cropOval().maxResultSize(1000, 1000, true)
                    .provider(ImageProvider.BOTH) // Or bothCameraGallery()
                    .setDismissListener {
                        Log.d("ImagePicker", "onDismiss")
                    }.createIntentFromDialog { launcher.launch(it) }
            }
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
                binding.radioFemale.isChecked = false
                binding.radioRatherNotSay.isChecked = false
            }
            2 -> {
                binding.radioMale.isChecked = false
                binding.radioFemale.isChecked = true
                binding.radioRatherNotSay.isChecked = false
            }
            3 -> {
                binding.radioMale.isChecked = false
                binding.radioFemale.isChecked = false
                binding.radioRatherNotSay.isChecked = true
            }
            else -> {
                binding.radioMale.isChecked = true
                binding.radioFemale.isChecked = false
                binding.radioRatherNotSay.isChecked = false
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

                //Add data againr
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

    override fun onSuccessGetProfileData(profileResponse: ObjectResponse<DataUser>) {

        userData = profileResponse.data
        listDataCountry?.forEach {
            if (it.id != null && it.id == userData?.countryId) presenter?.getStateDataPresenter(it.id)
        }
        if (userData != null && listDataCountry != null) {
            userData?.let { setUserData(it) }
        }


        if (binding.txtErrorUsername.visibility != View.VISIBLE) {

            //Set Data to Preferences
            getSharedPreferences = requireContext().getSharedPreferences(
                MainMenuActivity.TOKEN_USER_PREFERENCE, AppCompatActivity.MODE_PRIVATE
            )
            val gson = Gson()
            val newUserDataString = gson.toJson(userData)

            with(getSharedPreferences?.edit()) {
                this?.putString(USER_DATA, newUserDataString)
                this?.apply()
            }

            //Reload Info User At MenuBar
            onReloadUserMenu()
        }

    }

    override fun onSuccessGetCountryData(countryResponse: ObjectResponse<List<DataCountry>>) {
        handleDropDownCountry(countryResponse.data as ArrayList<DataCountry>)
        listDataCountry = countryResponse.data
        listDataCountry?.forEach {
            if (it.id != null && it.id == userData?.countryId) presenter?.getStateDataPresenter(it.id)
        }
        if (userData != null && listDataCountry != null) {
            userData?.let { setUserData(it) }
        }
        mListener?.onHideProgressBar()

    }

    override fun onSuccessGetStateData(stateResponse: ObjectResponse<List<DataState>>) {
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

    override fun onSuccessUpdateProfile(updateProfileResponse: ObjectResponse<DataUser>) {
        avatarUrl = null
        binding.txtErrorFullName.visibility = View.GONE
        binding.txtErrorFullName.focusable = View.FOCUSABLE
        onResume()

        Toast.makeText(
            activity?.applicationContext,
            updateProfileResponse.message.toString(),
            Toast.LENGTH_SHORT
        ).show()

    }

    override fun onErrorGetProfile(errorType: String) {
        mListener?.onShowDisconnectDialog()
    }


    override fun onShowError(errorType: String) {
        when (errorType) {

            FULL_NAME_AT_LEAST_4_CHARS -> {
                binding.txtErrorFullName.apply {
                    visibility = View.VISIBLE
                    text = context.getString(R.string.error_user_name)
                }
                binding.editProfileFullName.apply {
                    requestFocus()
                    setBackgroundResource(R.drawable.custom_edittext_error)
                }
            }

            FULL_NAME_LENGTH -> {
                binding.txtErrorFullName.apply {
                    visibility = View.VISIBLE
                    text = context.getString(R.string.txt_error_max_char)
                }
                binding.editProfileFullName.apply {
                    requestFocus()
                    setBackgroundResource(R.drawable.custom_edittext_error)
                }
            }

            FULL_NAME_FORMAT -> {
                binding.txtErrorFullName.apply {
                    visibility = View.VISIBLE
                    text = context.getString(R.string.txt_error_fn_format)
                }
                binding.editProfileFullName.apply {
                    requestFocus()
                    setBackgroundResource(R.drawable.custom_edittext_error)
                }
            }

            USER_NAME_AT_LEAST_4_CHARS -> {
                binding.txtErrorUsername.apply {
                    visibility = View.VISIBLE
                    text = context.getString(R.string.error_user_name)
                }
                binding.editUsername.apply {
                    setBackgroundResource(R.drawable.custom_edittext_error)
                    requestFocus()
                }
            }

            USER_NAME_LENGTH -> {
                binding.txtErrorUsername.apply {
                    visibility = View.VISIBLE
                    text = context.getString(R.string.txt_error_us_25_char)
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

            USERNAME_NAMESAKE -> {
                binding.txtErrorUsername.apply {
                    visibility = View.VISIBLE
                    text = context.getString(R.string.txt_error_full_name_sake)
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
        mListener?.onHideProgressBar()
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