package com.madison.move.ui.login

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.madison.move.databinding.FragmentLoginDialogBinding

class LoginDialogFragment : DialogFragment(), LoginContract.LoginView {
    private lateinit var binding: FragmentLoginDialogBinding
    private lateinit var presenter: LoginPresenter

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

        binding.imgCloseLoginDialog.setOnClickListener {
            dialog?.dismiss()
        }
        presenter = LoginPresenter(this)


        return binding.root
    }

    override fun onShowLoading() {

    }

    override fun onDisableButtonLogin() {

    }

    override fun onEnableButtonLogin() {

    }

    override fun onBottomNavigateSystemUI() {

    }
}