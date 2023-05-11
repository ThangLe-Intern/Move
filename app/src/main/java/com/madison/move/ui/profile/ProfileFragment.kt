package com.madison.move.ui.profile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.madison.move.R
import com.madison.move.data.model.User
import com.madison.move.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private var listState: ArrayList<String> =
        arrayListOf("None", "Ha Noi", "Da Nang", "Hue", "Ho Chi Minh", "Hai Phong")
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private val months = arrayOf(
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec"
    )
    private val years = (1900..2030).map { it.toString() }.toMutableList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleRadioButton()
        handleDropDownState()
        handleDropDownCountry()
        hideHintTextInputLayout()

        val bundle = arguments
        val user:User = bundle?.getParcelable<User>("user")!!

        setUserData(user)
        handleDropDownDob()

    }

    private fun setUserData(user:User){
        binding.editUsername.setText(user.username)
        binding.editProfileEmail.setText(user.email)
        binding.editProfileFullName.setText(user.fullname)
        binding.editProfileCity.setText(user.address)
        binding.imgProfileUser.setImageResource(user.avatar)

        if (user.gender == "Male"){
            binding.radioMale.isChecked = true
        }else{
            binding.radioFemale.isChecked = true
        }

        binding.dropdownYearText.setText("2001")
        binding.dropdownMonthText.setText("Jan")
        binding.dropdownDayText.setText("1")

        val monthSelected = binding.dropDownProfileMonth.editText?.text.toString()
        val yearSelected =  binding.dropDownProfileYear.editText?.text.toString()
        onHandleListOfDay(monthSelected,yearSelected)

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
        val yearAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, years.sortedDescending())

        binding.dropdownMonthText.setAdapter(monthAdapter)
        binding.dropdownYearText.setAdapter(yearAdapter)


    }

    private fun onYearSelected(){
        binding.dropdownYearText.setOnItemClickListener { parent, view, position, id ->
            val monthSelected = binding.dropDownProfileMonth.editText?.text.toString()
            val yearSelected: String = parent.getItemAtPosition(position).toString()
            onHandleListOfDay(monthSelected,yearSelected)
        }
    }

    private fun onMonthSelected() {
        binding.dropdownMonthText.setOnItemClickListener { parent, view, position, id ->
            val yearSelected = binding.dropDownProfileYear.editText?.text.toString()
            val monthSelected: String = parent.getItemAtPosition(position).toString()
            if (yearSelected != "") {
                onHandleListOfDay(monthSelected,yearSelected)
            }

        }
    }


    private fun onHandleListOfDay(monthSelected:String,yearSelected:String){
        binding.dropdownDayText.inputType = EditorInfo.TYPE_NULL
        val days: MutableList<String>
        if (isThirtyDaysMonth(monthSelected)) {
            days = (1..30).map { it.toString() }.toMutableList()
        } else if (monthSelected == "Feb") {
            days =
                if (isLeapYear(yearSelected.toInt())) {
                    (1..29).map { it.toString() }.toMutableList()
                } else {
                    (1..28).map { it.toString() }.toMutableList()
                }
        } else {
            days = (1..31).map { it.toString() }.toMutableList()
        }
        val dayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, days)
        binding.dropdownDayText.setAdapter(dayAdapter)
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


    private fun handleDropDownState() {
        for (i in 1..3) {
            listState.addAll(arrayListOf("Ha Noi", "Da Nang", "Hue", "Ho Chi Minh", "Hai Phong"))
        }
        arrayAdapter = context?.let {
            ArrayAdapter(
                it.applicationContext,
                R.layout.item_dropdown,
                listState
            )
        }!!
        binding.dropdownStateText.setAdapter(arrayAdapter)
        binding.dropdownStateText.inputType = EditorInfo.TYPE_NULL
    }

    private fun handleDropDownCountry() {
        for (i in 1..3) {
            listState.addAll(arrayListOf("Ha Noi", "Da Nang", "Hue", "Ho Chi Minh", "Hai Phong"))
        }
        arrayAdapter = context?.let {
            ArrayAdapter(
                it.applicationContext,
                R.layout.item_dropdown,
                listState
            )
        }!!
        binding.dropdownCountryText.setAdapter(arrayAdapter)
        binding.dropdownCountryText.inputType = EditorInfo.TYPE_NULL
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
    }


}