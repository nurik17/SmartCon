package kz.cifron.smartcon.feature_login.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
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

    private var isLoginEmpty = true
    private var isPasswordEmpty = true


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
            isLoginEmpty = savedEmail.isEmpty()
            isPasswordEmpty = savedPassword.isEmpty()
        }

        binding.loginButton.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val rememberMe = binding.saveCheckBox.isChecked

            viewModel.performLogin(email,password,rememberMe)

            if(rememberMe){
                viewModel.saveUserInfo(email,password)
            }
        }
        binding.saveCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val message = if(isChecked) "user info will be saved" else "User info will not be saved"
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
        binding.editTextEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                isLoginEmpty = p0.isNullOrEmpty()
                updateLoginButtonState()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        binding.editTextPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                isPasswordEmpty = p0.isNullOrEmpty()
                updateLoginButtonState()
            }

        })
        observeViewModel()
        updateLoginButtonState()
    }

    private fun updateLoginButtonState() {
        if(!isLoginEmpty || !isPasswordEmpty){
            binding.loginButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.isActiveButton))
            binding.loginButton.isEnabled = true
        }else{
            binding.loginButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.isNotActiveButton))
            binding.loginButton.isEnabled = false
        }
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

