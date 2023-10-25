package ru.kpfu.itis.android.asadullin.model

import java.io.Serializable

data class Question(
    val question: String,
    val answersList: List<Answer>,
    val answerIndex: Int
) : Serializable{
    private val answer: String get() = answersList[answerIndex].answer

    override fun toString(): String {
        return "Question(question='$question', answersList=$answersList, answerIndex=$answerIndex, answer=$answer)"
    }
}