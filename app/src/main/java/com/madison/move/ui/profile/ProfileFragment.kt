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
import com.madison.move.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private var listState:ArrayList<String> = arrayListOf("None","Ha Noi","Da Nang","Hue","Ho Chi Minh","Hai Phong")
    private lateinit var arrayAdapter: ArrayAdapter<String>

    private val days = (1..31).map { it.toString() }.toTypedArray()
    private val months = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    private val years = (1970..2030).map { it.toString() }.toTypedArray()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        handleDropDownState()
        handleDropDownCountry()
        hideHintTextInputLayout()
        handleDropDownDob()

        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleRadioButton()
    }


    private fun hideHintTextInputLayout(){
        binding.dropDownProfileCountry.isHintEnabled = false
        binding.dropDownProfileState.isHintEnabled = false
    }

    private fun handleDropDownDob(){
        binding.dropdownDayText.inputType = EditorInfo.TYPE_NULL
        binding.dropdownMonthText.inputType = EditorInfo.TYPE_NULL
        binding.dropdownYearText.inputType = EditorInfo.TYPE_NULL
        binding.dropDownProfileDay.isHintEnabled = false
        binding.dropDownProfileMonth.isHintEnabled = false
        binding.dropDownProfileYear.isHintEnabled = false

        val dayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, days)
        val monthAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, months)
        val yearAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, years)

        binding.dropdownDayText.setAdapter(dayAdapter)
        binding.dropdownMonthText.setAdapter(monthAdapter)
        binding.dropdownYearText.setAdapter(yearAdapter)


    }

    private fun handleDropDownState(){
        for (i in 1..3){
            listState.addAll(arrayListOf("Ha Noi","Da Nang","Hue","Ho Chi Minh","Hai Phong"))
        }
        arrayAdapter = context?.let { ArrayAdapter(it.applicationContext,R.layout.item_dropdown,listState) }!!
        binding.dropdownStateText.setAdapter(arrayAdapter)
        binding.dropdownStateText.inputType = EditorInfo.TYPE_NULL
    }

    private fun handleDropDownCountry(){
        for (i in 1..3){
            listState.addAll(arrayListOf("Ha Noi","Da Nang","Hue","Ho Chi Minh","Hai Phong"))
        }
        arrayAdapter = context?.let { ArrayAdapter(it.applicationContext,R.layout.item_dropdown,listState) }!!
        binding.dropdownCountryText.setAdapter(arrayAdapter)
        binding.dropdownCountryText.inputType = EditorInfo.TYPE_NULL
    }

    private fun handleRadioButton(){
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
                idRadioFemale-> if (checked) {
                    binding.radioMale.isChecked = false
                    binding.radioRatherNotSay.isChecked = false
                }
                idRadioRather -> if (checked){
                    binding.radioMale.isChecked = false
                    binding.radioFemale.isChecked = false
                }
            }
        }
    }


}