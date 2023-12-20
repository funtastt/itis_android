package ru.kpfu.itis.android.asadullin.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.data.db.dao.UserDao
import ru.kpfu.itis.android.asadullin.databinding.FragmentProfileBinding
import ru.kpfu.itis.android.asadullin.di.ServiceLocator
import ru.kpfu.itis.android.asadullin.model.UserModel
import ru.kpfu.itis.android.asadullin.ui.MainActivity
import ru.kpfu.itis.android.asadullin.utils.ApplicationRegex.EMAIL_PATTERN
import ru.kpfu.itis.android.asadullin.utils.ApplicationRegex.MEDIUM_PASSWORD_PATTERN
import ru.kpfu.itis.android.asadullin.utils.ApplicationRegex.PHONE_PATTERN
import ru.kpfu.itis.android.asadullin.utils.ApplicationRegex.STRONG_PASSWORD_PATTERN
import ru.kpfu.itis.android.asadullin.utils.ApplicationRegex.USERNAME_PATTERN
import ru.kpfu.itis.android.asadullin.utils.PasswordEncryptor

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!

    private val sharedPreferences by lazy {
        requireActivity().getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE)
    }

    private val userDao: UserDao = ServiceLocator.getDatabaseInstance().userDao
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        with(binding) {
            lifecycleScope.launch(Dispatchers.IO) {
                val currentUser = UserModel.fromUserEntity(userDao.getUserById(getUserId()))
                withContext(Dispatchers.Main) {
                    tvProfilePhoneNumber.text = currentUser.phoneNumber
                    tvProfileEmail.text = currentUser.email
                    tvProfileUsername.text = currentUser.name
                }
            }
            btnChangeProfileUsername.setOnClickListener {
                val currentUsername = binding.tvProfileUsername.text.toString()
                showChangeDialog(
                    getString(R.string.enter_username),
                    currentUsername
                ) { newUsername ->
                    checkUsernameValidity(newUsername)
                }
            }
            btnChangeProfileEmail.setOnClickListener {
                val currentEmail = binding.tvProfileEmail.text.toString()
                showChangeDialog(getString(R.string.enter_email), currentEmail) { newEmail ->
                    checkEmailValidity(newEmail)
                }
            }

            btnChangeProfilePhoneNumber.setOnClickListener {
                val currentPhoneNumber = binding.tvProfilePhoneNumber.text.toString()
                showChangeDialog(
                    getString(R.string.enter_phone_number),
                    currentPhoneNumber
                ) { newPhoneNumber ->
                    checkPhoneNumberValidity(newPhoneNumber)
                }
            }

            btnChangePassword.setOnClickListener {
                showChangePasswordDialog()
            }

            btnDeleteProfile.setOnClickListener {
                safeDeleteCurrentUser()
            }

            btnExit.setOnClickListener {
                exit()
            }
        }
    }

    private fun exit() {
        lifecycleScope.launch (Dispatchers.Main) {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            // hours of debagging have been spent,
            // but I still don't understand how navigation in nested graphs works
            val currentActivity = requireActivity()
            val intent = Intent(currentActivity, MainActivity::class.java)
            currentActivity.finish()
            currentActivity.startActivity(intent)
        }
    }

    private fun safeDeleteCurrentUser() {
        lifecycleScope.launch(Dispatchers.IO) {
            userDao.safeDeleteUser(getUserId(), System.currentTimeMillis())
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            // hours of debagging have been spent,
            // but I still don't understand how navigation in nested graphs works
            val currentActivity = requireActivity()
            val intent = Intent(currentActivity, MainActivity::class.java)
            currentActivity.finish()
            currentActivity.startActivity(intent)
        }
    }

    private fun getUserId() = sharedPreferences.getInt("userId", -1)

    private fun checkPhoneNumberValidity(newPhoneNumber: String) {
        if (PHONE_PATTERN.matcher(newPhoneNumber).matches()) {
            lifecycleScope.launch(Dispatchers.IO) {
                val usernameExists = userDao.getUsersByPhoneNumber(newPhoneNumber).isNotEmpty()

                if (usernameExists) {
                    withContext(Dispatchers.Main) {
                        Snackbar.make(
                            requireView(),
                            getString(R.string.user_with_this_phone_number_exists),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    userDao.updatePhoneNumber(getUserId(), newPhoneNumber)
                    binding.tvProfilePhoneNumber.text = newPhoneNumber
                }
            }

        } else {
            Snackbar.make(
                requireView(),
                getString(R.string.entered_phone_is_invalid),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkEmailValidity(newEmail: String) {
        if (EMAIL_PATTERN.matcher(newEmail).matches()) {
            lifecycleScope.launch(Dispatchers.IO) {
                val usernameExists = userDao.getUsersByEmail(newEmail).isNotEmpty()

                if (usernameExists) {
                    withContext(Dispatchers.Main) {
                        Snackbar.make(
                            requireView(),
                            getString(R.string.user_with_this_email_exists),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    userDao.updateEmail(getUserId(), newEmail)
                    binding.tvProfileEmail.text = newEmail
                }
            }

        } else {
            Snackbar.make(
                requireView(),
                getString(R.string.entered_email_is_invalid),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkUsernameValidity(newUsername: String) {
        if (USERNAME_PATTERN.matcher(newUsername).matches()) {
            lifecycleScope.launch(Dispatchers.IO) {
                val usernameExists = userDao.getUsersByUsername(newUsername).isNotEmpty()

                if (usernameExists) {
                    withContext(Dispatchers.Main) {
                        Snackbar.make(
                            requireView(),
                            getString(R.string.user_with_this_username_exists),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    userDao.updateUsername(getUserId(), newUsername)
                    binding.tvProfileUsername.text = newUsername
                }
            }
        } else {
            Snackbar.make(
                requireView(),
                getString(R.string.entered_username_is_invalid),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun showChangeDialog(title: String, currentValue: String, onConfirm: (String) -> Unit) {
        val editText = EditText(requireContext())
        editText.setText(currentValue)

        val padding = resources.getDimensionPixelSize(R.dimen.dialog_padding)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setView(editText)
            .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                val newValue = editText.text.toString()
                onConfirm(newValue)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .setBackgroundInsetStart(padding)
            .setBackgroundInsetEnd(padding)
            .show()
    }

    private fun showChangePasswordDialog() {
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog_change_password, null)

        val currentPasswordEditText: EditText = dialogView.findViewById(R.id.etCurrentPassword)
        val newPasswordEditText: EditText = dialogView.findViewById(R.id.etNewPassword)

        val padding = resources.getDimensionPixelSize(R.dimen.dialog_padding)

        MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                val enteredCurrentPassword = currentPasswordEditText.text.toString()
                val enteredNewPassword = newPasswordEditText.text.toString()

                lifecycleScope.launch(Dispatchers.IO) {
                    val currentPassword = userDao.getUserById(getUserId()).password

                    if (!PasswordEncryptor.checkPassword(enteredCurrentPassword, currentPassword)) {
                        withContext(Dispatchers.Main) {
                            Snackbar.make(
                                requireView(),
                                getString(R.string.wrong_password_entered),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        val message: String
                        if (STRONG_PASSWORD_PATTERN.matcher(enteredNewPassword).matches()) {
                            message = getString(R.string.successfully_changed_password)
                            userDao.updatePassword(
                                getUserId(),
                                PasswordEncryptor.encryptPassword(enteredNewPassword)
                            )
                        } else if (MEDIUM_PASSWORD_PATTERN.matcher(enteredNewPassword).matches()) {
                            message = getString(R.string.one_of_the_conditions_is_not_met)
                        } else {
                            message = getString(R.string.password_is_too_simple)
                        }

                        withContext(Dispatchers.Main) {
                            Snackbar.make(
                                requireView(),
                                message,
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .setBackgroundInsetStart(padding)
            .setBackgroundInsetEnd(padding)
            .show()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
