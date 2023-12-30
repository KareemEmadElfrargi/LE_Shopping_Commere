package com.example.ecommercei.fragments.shopping
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ecommercei.R
import com.example.ecommercei.activitys.LoginRegisterActivity
import com.example.ecommercei.databinding.FragmentProfileBinding
import com.example.ecommercei.utils.Resource
import com.example.ecommercei.utils.showButtonNavigationView
import com.example.ecommercei.viewModel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    val viewModel by viewModels<ProfileViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return requireNotNull(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.constraintProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_userAccountFragment)
        }
        binding.linearAllOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_ordersFragment)
        }
        binding.linearBilling.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToBillingFragment(
                0f,
                emptyArray(),false
            )
            findNavController().navigate(action)
        }

        binding.linearLogOut.setOnClickListener {
            viewModel.logOut()
            val intent = Intent(requireContext(), LoginRegisterActivity::class.java)
            startActivity(intent)
            requireActivity().finish()

        }
        //val versionCode = BuildConfig.VERSION_CODE
        binding.tvVersion.text = "Version 1.0"

        lifecycleScope.launch {
            viewModel.user.collectLatest { resUser ->
                when (resUser) {
                    is Resource.Loading -> {
                        binding.progressbarSettings.visibility = View.VISIBLE

                    }

                    is Resource.Success -> {
                        val user = resUser.data!!
                        binding.progressbarSettings.visibility = View.GONE
                        Glide.with(requireView())
                            .load(user.imagePath)
                            .error(ColorDrawable(Color.BLACK)).into(binding.imageUser)
                        binding.tvUserName.text = "${user.firstName} ${user.lastName}"
                    }

                    is Resource.Error -> {
                        Toast.makeText(
                            requireContext(),
                            resUser.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.progressbarSettings.visibility = View.GONE

                    }

                    is Resource.Unspecified -> Unit
                }
            }
        }
        onLogoutClick()
    }
    private fun onLogoutClick() {
        binding.linearLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, LoginRegisterActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            requireActivity().finish()
        }

    }
}