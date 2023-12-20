package ru.kpfu.itis.android.asadullin.utils

import android.util.Patterns
import java.util.regex.Pattern

object ApplicationRegex {

    val EMAIL_PATTERN : Pattern = Patterns.EMAIL_ADDRESS
    val STRONG_PASSWORD_PATTERN : Pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}\$")
    val MEDIUM_PASSWORD_PATTERN : Pattern = Pattern.compile("^(?:(?=.*[a-z])(?=.*[A-Z])|(?=.*[a-z])(?=.*\\d)|(?=.*[A-Z])(?=.*\\d)).{8,}\$")
    val USERNAME_PATTERN: Pattern = Pattern.compile("^[a-zA-Z0-9]{3,32}$")
    val PHONE_PATTERN: Pattern = Patterns.PHONE
}