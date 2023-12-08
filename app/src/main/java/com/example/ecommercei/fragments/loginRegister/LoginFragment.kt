package com.example.ecommercei.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommercei.R
import com.example.ecommercei.activitys.ShoppingActivity
import com.example.ecommercei.databinding.FragmentLoginBinding
import com.example.ecommercei.dialog.setupButtonSheetDialog
import com.example.ecommercei.utils.Resource
import com.example.ecommercei.viewModel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment: Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private val viewModel : LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonLoginLogin.setOnClickListener {
                val email = edEmailLogIn.text.toString().trim()
                val password = edPasswordLogIn.text.toString()
                viewModel.login(email,password)
            }

            tvDontHaveAnAccount.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            tvForgotPasswordLogIn.setOnClickListener {
                setupButtonSheetDialog { email ->
                    viewModel.resetPassword(email)
                }
            }


            lifecycleScope.launch {
                viewModel.resetPassword.collect{
                    when(it){
                        is Resource.Error -> {
                            Snackbar.make(requireView(),"Error : ${it.message.toString()} ",Snackbar.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> {
                        }
                        is Resource.Success -> {
                            Snackbar.make(requireView(),"Reset link was sent to your email",Snackbar.LENGTH_SHORT).show()
                        }
                        is Resource.Unspecified -> Unit
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.login.collect{
                when(it){
                    is Resource.Loading -> {
                        binding.buttonLoginLogin.startAnimation()
                    }
                    is Resource.Success ->{
                        binding.buttonLoginLogin.revertAnimation()
                        Intent(requireActivity(),ShoppingActivity::class.java).also {intent ->
                            // remove and pop up login fragment
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    is Resource.Error ->{
                        binding.buttonLoginLogin.revertAnimation()
                        Snackbar.make(binding.buttonLoginLogin,it.message.toString(),Snackbar.LENGTH_LONG).show()
                    }
                    else ->Unit
                }
            }
        }
    }
}