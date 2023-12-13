package ru.kpfu.itis.android.asadullin.ui.fragments

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
import ru.kpfu.itis.android.asadullin.data.db.dao.UserDao
import ru.kpfu.itis.android.asadullin.data.db.entity.UserEntity
import ru.kpfu.itis.android.asadullin.databinding.FragmentRegistrationBinding
import ru.kpfu.itis.android.asadullin.di.ServiceLocator
import ru.kpfu.itis.android.asadullin.model.UserModel
import ru.kpfu.itis.android.asadullin.utils.ApplicationRegex.PHONE_PATTERN
import ru.kpfu.itis.android.asadullin.utils.ApplicationRegex.USERNAME_PATTERN
import ru.kpfu.itis.android.asadullin.utils.ApplicationRegex.EMAIL_PATTERN
import ru.kpfu.itis.android.asadullin.utils.ApplicationRegex.MEDIUM_PASSWORD_PATTERN
import ru.kpfu.itis.android.asadullin.utils.ApplicationRegex.STRONG_PASSWORD_PATTERN
import ru.kpfu.itis.android.asadullin.utils.PasswordEncryptor


class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding: FragmentRegistrationBinding get() = _binding!!

    private val userDao: UserDao = ServiceLocator.getDatabaseInstance().userDao


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        with(binding) {
            var username = ""
            var phoneNumber = ""
            var email = ""
            var password = ""

            btnRegister.isEnabled =
                checkButtonAccess(username, email, phoneNumber, password)

            etUsername.setOnFocusChangeListener { _, _ ->
                username = etUsername.text.toString()
                btnRegister.isEnabled =
                    checkButtonAccess(username, email, phoneNumber, password)
            }

            etPhoneNumber.setOnFocusChangeListener { _, _ ->
                phoneNumber = etPhoneNumber.text.toString()
                btnRegister.isEnabled =
                    checkButtonAccess(username, email, phoneNumber, password)
            }

            etEmail.setOnFocusChangeListener { _, _ ->
                email = etEmail.text.toString()
                btnRegister.isEnabled =
                    checkButtonAccess(username, email, phoneNumber, password)
            }

            etPassword.setOnFocusChangeListener { _, _ ->
                password = etPassword.text.toString()
                btnRegister.isEnabled =
                    checkButtonAccess(username, email, phoneNumber, password)
            }

            btnRegister.setOnClickListener {
                val newUser = UserModel(
                    phoneNumber = phoneNumber,
                    name = username,
                    email = email,
                    password = PasswordEncryptor.encryptPassword(password)
                )
                lifecycleScope.launch(Dispatchers.IO) {
                    userDao.insertUserModel(UserEntity.fromUserModel(newUser))
                }

                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }

    private fun checkButtonAccess(
        username: String,
        email: String,
        phone: String,
        password: String
    ): Boolean {
        var isAccessible = true

        with(binding) {
            if (username.isEmpty()) {
                isAccessible = false
                tilUsername.error = null
            } else if (!USERNAME_PATTERN.matcher(username).matches()) {
                isAccessible = false
                tilUsername.error = getString(R.string.invalid_username)
            } else {
                lifecycleScope.launch(Dispatchers.IO) {
                    val userExists = userDao.getUsersByUsername(username).isNotEmpty()
                    if (userExists) {
                        isAccessible = false
                    }
                    withContext(Dispatchers.Main) {
                        tilUsername.error =
                            if (userExists) getString(R.string.user_with_this_username_exists) else null
                    }
                }
            }

            if (phone.isEmpty()) {
                isAccessible = false
                tilPhoneNumber.error = null
            } else if (!PHONE_PATTERN.matcher(phone).matches()) {
                isAccessible = false
                tilPhoneNumber.error = getString(R.string.invalid_phone_number)
            } else {
                lifecycleScope.launch(Dispatchers.IO) {
                    val userExists = userDao.getUsersByPhoneNumber(phone)?.isNotEmpty()
                    println(userDao.getUsersByPhoneNumber(phone))
                    if (userExists == true) {
                        isAccessible = false
                    }
                    withContext(Dispatchers.Main) {
                        tilPhoneNumber.error =
                            if (userExists == true) getString(R.string.user_with_this_phone_number_exists) else null
                    }
                }
            }

            if (email.isEmpty()) {
                isAccessible = false
                tilEmail.error = null
            } else if (!EMAIL_PATTERN.matcher(email).matches()) {
                isAccessible = false
                tilEmail.error = getString(R.string.invalid_email)
            } else {
                lifecycleScope.launch(Dispatchers.IO) {
                    val userExists = userDao.getUsersByEmail(email).isNotEmpty()
                    if (userExists) {
                        isAccessible = false
                    }
                    withContext(Dispatchers.Main) {
                        tilEmail.error =
                            if (userExists) getString(R.string.user_with_this_email_exists) else null
                    }
                }
            }

            if (password.isEmpty()) {
                isAccessible = false
                tilPassword.error = null
            } else if (STRONG_PASSWORD_PATTERN.matcher(password).matches()) {
                tilPassword.error = null
            } else {
                isAccessible = false
                if (MEDIUM_PASSWORD_PATTERN.matcher(password).matches()) {
                    tilPassword.error = getString(R.string.one_of_the_conditions_is_not_met)
                } else {
                    tilPassword.error = getString(R.string.password_is_too_simple)
                }
            }
        }

        return isAccessible
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}