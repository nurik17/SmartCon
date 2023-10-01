package kz.cifron.smartcon.feature_login.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kz.cifron.smartcon.R
import kz.cifron.smartcon.databinding.FragmentLoginBinding
import kz.cifron.smartcon.feature_login.domain.LoginRepository
import kz.cifron.smartcon.feature_login.data.LoginState
import kz.cifron.smartcon.feature_login.domain.RetrofitClient
import kz.cifron.smartcon.feature_login.data.User


class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel : LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = RetrofitClient.authInstanceApi
        val repository = LoginRepository(apiService)
        viewModel = ViewModelProvider(this, LoginViewModelFactory(repository,requireContext()))[LoginViewModel::class.java]

        val savedUserInfo = viewModel.getSavedUserInfo()
        savedUserInfo?.let { (savedEmail, savedPassword) ->
            binding.editTextEmail.setText(savedEmail)
            binding.editTextPassword.setText(savedPassword)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val rememberMe = binding.saveCheckBox.isChecked

            viewModel.performLogin(email,password,rememberMe)

            if(rememberMe){
                viewModel.saveUserInfo(email,password)
            }
           /* val buttonColor = if(email.isEmpty() && password.isEmpty()){
                R.color.buttonBackgroundEmpty
            }else{
                R.color.buttonBackgroundFilled
            }
            binding.loginButton.setBackgroundResource(buttonColor)*/
        }
        binding.saveCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val message = if(isChecked) "user info will be saved" else "User info will not be saved"
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loginStateLiveData.observe(viewLifecycleOwner){ loginState ->
            when (loginState) {
                is LoginState.Success -> handleSuccessState(loginState.user, loginState.token)
                is LoginState.Error -> handleErrorState()
                LoginState.Loading -> showLoadingState()
            }
        }
    }

    private fun showLoadingState() {
        binding.progressBar.visibility = View.VISIBLE
        binding.loginButton.isEnabled = false
    }

    private fun handleErrorState() {
        binding.progressBar.visibility = View.GONE
        binding.loginButton.isEnabled = true
        Toast.makeText(requireContext(), "Error this user doesn't exist", Toast.LENGTH_SHORT).show()
    }

    private fun handleSuccessState(user: User, token: String) {
        findNavController().navigate(R.id.action_id_loginFragment_to_id_homeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

