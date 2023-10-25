package ru.kpfu.itis.android.asadullin.model

import java.io.Serializable

data class Answer(
    val answer: String,
    var checked: Boolean = false
) : Serializable{
    override fun toString(): String {
        return answer
    }
}