package com.madison.move.ui.login

import android.app.Dialog
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
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.madison.move.R
import com.madison.move.databinding.FragmentLoginDialogBinding
import com.madison.move.ui.menu.MainInterface


class LoginDialogFragment(var mOnInputListener: OnInputListener? = null) : DialogFragment(),
    LoginContract.LoginView {
    private lateinit var binding: FragmentLoginDialogBinding
    private lateinit var presenter: LoginPresenter

    companion object {
        const val EMAIL_INVALID = "EMAIL_INVALID"
        const val EMAIL_CONTAIN_SPACE = "EMAIL_CONTAIN_SPACE"
        const val PASSWORD_CONTAIN_SPACE = "PASSWORD_CONTAIN_SPACE"
        const val INCORRECT_ACCOUNT = "INCORRECT_ACCOUNT"
        const val PASSWORD_NULL = "PASSWORD_NULL"
        const val EMAIL_NULL = "EMAIL_NULL"
        const val PASSWORD_EMAIL_NULL = "PASSWORD_EMAIL_NULL"
    }


    var mListener: MainInterface? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Initialize the interface variable
        if (activity != null){
            mListener = activity as MainInterface
        }

        if (mListener == null) {
            throw ClassCastException("$activity must implement MainInterface")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginDialogBinding.inflate(inflater, container, false)

        presenter = LoginPresenter(this)
        presenter.apply {
            onEnableButtonLoginPresenter()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgCloseLoginDialog.setOnClickListener {
            dialog?.dismiss()
        }

        binding.editLoginEmail.requestFocus()
        onShowHidePassword()

        binding.loginBtn.setOnClickListener {
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.loginBtn.windowToken, 0)

            binding.layoutErrorMessage.visibility = View.GONE
            binding.txtErrorEmail.visibility = View.GONE

            val emailUserInput = binding.editLoginEmail.text.toString().trim()
            val passwordUserInput = binding.editLoginPassword.text.toString().trim()

            presenter.onLoginClickPresenter(emailUserInput, passwordUserInput)
        }

    }

    private fun onShowHidePassword() {
        binding.imgHidePassword.setOnClickListener {
            binding.imgHidePassword.visibility = View.GONE
            binding.imgShowPassword.visibility = View.VISIBLE

            binding.editLoginPassword.apply {
                transformationMethod = HideReturnsTransformationMethod.getInstance()
                setSelection(binding.editLoginPassword.length())
            }
        }

        binding.imgShowPassword.setOnClickListener {
            binding.imgShowPassword.visibility = View.GONE
            binding.imgHidePassword.visibility = View.VISIBLE

            binding.editLoginPassword.apply {
                transformationMethod = PasswordTransformationMethod.getInstance()
                setSelection(binding.editLoginPassword.length())
            }
        }
    }


    override fun onShowLoading() {

    }


    override fun onEnableButtonLogin() {
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.editLoginEmail.setBackgroundResource(R.drawable.custom_edittext)
                binding.editLoginPassword.setBackgroundResource(R.drawable.custom_edittext)
                binding.txtErrorEmail.visibility = View.GONE
                binding.txtErrorPassword.visibility = View.GONE
                binding.layoutErrorMessage.visibility = View.GONE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        binding.editLoginEmail.addTextChangedListener(textWatcher)
        binding.editLoginPassword.addTextChangedListener(textWatcher)
    }

    override fun onShowError(errorType: String) {
        when (errorType) {
            INCORRECT_ACCOUNT -> {
                binding.layoutErrorMessage.visibility = View.VISIBLE
            }
            EMAIL_INVALID -> {
                binding.txtErrorEmail.isVisible = true
                binding.txtErrorEmail.text = getString(R.string.invalid_email)
                binding.editLoginEmail.setBackgroundResource(R.drawable.custom_edittext_error)
            }
            EMAIL_CONTAIN_SPACE -> {
                binding.editLoginEmail.error = getString(R.string.email_white_space)
            }
            PASSWORD_CONTAIN_SPACE -> {
                binding.editLoginPassword.error = getString(R.string.password_white_space)
            }
            EMAIL_NULL -> {
                binding.txtErrorEmail.isVisible = true
                binding.txtErrorEmail.text = getString(R.string.txt_pls_enter_email)
                binding.editLoginEmail.setBackgroundResource(R.drawable.custom_edittext_error)
            }
            PASSWORD_NULL -> {
                binding.txtErrorPassword.isVisible = true
                binding.txtErrorPassword.text = getString(R.string.txt_pls_enter_password)
                binding.editLoginPassword.setBackgroundResource(R.drawable.custom_edittext_error)

            }
            PASSWORD_EMAIL_NULL -> {
                binding.txtErrorPassword.isVisible = true
                binding.txtErrorPassword.text = getString(R.string.txt_pls_enter_password)
                binding.editLoginPassword.setBackgroundResource(R.drawable.custom_edittext_error)
                binding.txtErrorEmail.isVisible = true
                binding.txtErrorEmail.text = getString(R.string.txt_pls_enter_email)
                binding.editLoginEmail.setBackgroundResource(R.drawable.custom_edittext_error)
            }
        }
    }


    override fun onSendDataToActivity(email: String, password: String) {

        if (mListener?.isDeviceOnlineCheck() == false){
            dismiss()
            mListener?.onShowDisconnectDialog()
        }else{
            mOnInputListener?.sendData(email, password, this)
            this.view?.visibility = View.INVISIBLE
            mListener?.onShowProgressBar()
        }

    }


    override fun onResponseError(errorType: String) {
        Toast.makeText(activity, errorType, Toast.LENGTH_SHORT).show()
    }

    //Send user token from Fragment To Activity
    interface OnInputListener {
        fun sendData(email: String, password: String, fragment: DialogFragment)
    }

    override fun onBottomNavigateSystemUI() {

    }


}