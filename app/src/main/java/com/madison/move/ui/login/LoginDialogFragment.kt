package com.madison.move.ui.login

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable

import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

import android.view.inputmethod.InputMethodManager

import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible

import androidx.fragment.app.DialogFragment
import com.madison.move.R
import com.madison.move.databinding.FragmentLoginDialogBinding

class LoginDialogFragment : DialogFragment(), LoginContract.LoginView {
    private lateinit var binding: FragmentLoginDialogBinding
    private lateinit var presenter: LoginPresenter

    companion object{
        const val  EMAIL_INVALID = "EMAIL_INVALID"
        const val  EMAIL_CONTAIN_SPACE = "EMAIL_CONTAIN_SPACE"
        const val  PASSWORD_CONTAIN_SPACE = "PASSWORD_CONTAIN_SPACE"
        const val  INCORRECT_ACCOUNT = "INCORRECT_ACCOUNT"

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.apply {
            setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)

            setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginDialogBinding.inflate(inflater, container, false)

        presenter = LoginPresenter(this)
        presenter.apply {
            onEnableButtonLoginPresenter()
        }

        //Show and Hide Password
        onShowAndHidePassword()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgCloseLoginDialog.setOnClickListener {
            dialog?.dismiss()
        }

        binding.loginBtn.setOnClickListener {
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow( binding.loginBtn.windowToken, 0)

            binding.layoutErrorMessage.visibility = View.GONE
            binding.txtErrorEmail.visibility = View.GONE

            presenter.onLoginClickPresenter(binding.editLoginEmail.text.toString().trim(),binding.editLoginPassword.text.toString().trim())
        }

    }

    private fun onShowAndHidePassword(){
        val imgShowPassword:AppCompatImageView = binding.imgShowPassword
        val imgHidePassword:AppCompatImageView = binding.imgHidePassword

        imgShowPassword.setOnClickListener {
            binding.editLoginPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()

            //Move Cursor to end of edit text
            binding.editLoginPassword.setSelection(binding.editLoginPassword.text.toString().trim().length)
            imgShowPassword.visibility = View.GONE
            imgHidePassword.visibility = View.VISIBLE
        }

        imgHidePassword.setOnClickListener {
            binding.editLoginPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.editLoginPassword.setSelection(binding.editLoginPassword.text.toString().trim().length)
            imgHidePassword.visibility = View.GONE
            imgShowPassword.visibility = View.VISIBLE
        }

    }



    override fun onShowLoading() {

    }


    override fun onEnableButtonLogin() {
        val textWatcher:TextWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val txtEmail = binding.editLoginEmail.text.toString().trim()
                val txtPassword = binding.editLoginPassword.text.toString().trim()

                binding.loginBtn.isEnabled = txtEmail.isNotEmpty() && txtPassword.isNotEmpty()

            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        binding.editLoginEmail.addTextChangedListener(textWatcher)
        binding.editLoginPassword.addTextChangedListener(textWatcher)
    }

    override fun onShowError(errorType: String) {
        when(errorType){
            INCORRECT_ACCOUNT -> {
                binding.layoutErrorMessage.visibility = View.VISIBLE
            }
            EMAIL_INVALID ->{
                binding.txtErrorEmail.isVisible = true
                binding.txtErrorEmail.text =  getString(R.string.invalid_email)
            }
            EMAIL_CONTAIN_SPACE ->{
                binding.editLoginEmail.error = getString(R.string.email_white_space)
            }
            PASSWORD_CONTAIN_SPACE ->{
                binding.editLoginPassword.error = getString(R.string.password_white_space)
            }
        }
    }

    override fun onLoginClick() {
        dialog?.dismiss()
    }


    override fun onBottomNavigateSystemUI() {

    }
}