package ru.kpfu.itis.android.asadullin.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.data.db.entity.UserEntity
import ru.kpfu.itis.android.asadullin.databinding.FragmentLoginBinding
import ru.kpfu.itis.android.asadullin.di.ServiceLocator
import ru.kpfu.itis.android.asadullin.utils.ApplicationRegex.EMAIL_PATTERN
import ru.kpfu.itis.android.asadullin.utils.ApplicationRegex.MEDIUM_PASSWORD_PATTERN
import ru.kpfu.itis.android.asadullin.utils.ApplicationRegex.STRONG_PASSWORD_PATTERN
import ru.kpfu.itis.android.asadullin.utils.PasswordEncryptor
import java.time.LocalDateTime

class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!

    private val sharedPreferences by lazy {
        requireActivity().getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        with(binding) {
            if (getUserId() != -1) {
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            }

            btnLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (!EMAIL_PATTERN.matcher(email).matches()) {
                    tilEmail.error = getString(R.string.invalid_email)
                    return@setOnClickListener
                }

                lifecycleScope.launch(Dispatchers.IO) {
                    val users: List<UserEntity>? =
                        ServiceLocator.getDatabaseInstance().userDao.getUsersByEmail(email)
                    if (users?.isEmpty() == true) {
                        withContext(Dispatchers.Main) {
                            tilEmail.error =
                                getString(R.string.user_with_this_email_does_not_exists)
                        }
                    } else {
                        val user = users?.get(0)
                        val userPassword = user?.password ?: ""

                        withContext(Dispatchers.Main) {
                            tilEmail.error = null

                            if (PasswordEncryptor.checkPassword(password, userPassword)) {
                                saveUserId(user?.userId ?: -1)
                                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                            } else {
                                tilPassword.error = getString(R.string.wrong_password)
                            }
                        }

                    }
                }
            }

            btnRegister.setOnClickListener {
                etEmail.setText("")
                etPassword.setText("")
                tilEmail.error = null
                tilPassword.error = null
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    private fun saveUserId(userId: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("userId", userId)
        editor.apply()
    }

    private fun getUserId() = sharedPreferences.getInt("userId", -1)
}

