package ru.kpfu.itis.android.asadullin.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.data.db.entity.UserEntity
import ru.kpfu.itis.android.asadullin.databinding.FragmentLoginBinding
import ru.kpfu.itis.android.asadullin.di.ServiceLocator
import ru.kpfu.itis.android.asadullin.utils.ApplicationRegex.EMAIL_PATTERN
import ru.kpfu.itis.android.asadullin.utils.PasswordEncryptor

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
                    val users: List<UserEntity> =
                        ServiceLocator.getDatabaseInstance().userDao.getUsersByEmail(email)
                    if (users.isEmpty()) {
                        withContext(Dispatchers.Main) {
                            tilEmail.error =
                                getString(R.string.user_with_this_email_does_not_exists)
                        }
                    } else {
                        val user = users[0]
                        val userPassword = user.password
                        val isDeleted = user.isDeleted

                        withContext(Dispatchers.Main) {
                            tilEmail.error = null

                            if (PasswordEncryptor.checkPassword(password, userPassword)) {
                                tilPassword.error = null

                                if (isDeleted) {
                                    showDeletionDialogFragment(user.userId)
                                } else {
                                    saveUserId(user.userId)
                                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                                }
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

    private fun showDeletionDialogFragment(userId: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.account_deleted))
        builder.setMessage(getString(R.string.account_deleted_message))

        builder.setPositiveButton(getString(R.string.restore_account)) { _, _ ->
            restoreAccount(userId = userId)
        }

        builder.setNegativeButton(getString(R.string.delete_account_forever)) { _, _ ->
            deleteAccountForever(userId = userId)
        }

        builder.show()
    }

    private fun deleteAccountForever(userId: Int) {
        with(binding) {
            etEmail.setText("")
            etPassword.setText("")
        }

        lifecycleScope.launch(Dispatchers.IO) {
            ServiceLocator.getDatabaseInstance().userDao
                .deleteUserForever(userId = userId)
        }

        Snackbar.make(
            requireView(),
            getString(R.string.successfully_deleted),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun restoreAccount(userId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            ServiceLocator.getDatabaseInstance().userDao
                .restoreUser(userId = userId)
        }

        saveUserId(userId)

        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        Snackbar.make(
            requireView(),
            getString(R.string.successfully_restored),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun saveUserId(userId: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("userId", userId)
        editor.apply()
    }

    private fun getUserId() = sharedPreferences.getInt("userId", -1)

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

