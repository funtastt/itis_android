package ru.kpfu.itis.android.asadullin.util

import ru.kpfu.itis.android.asadullin.model.Answer
import ru.kpfu.itis.android.asadullin.model.Question
import java.util.Random

object QuestionGenerator {
    private val rng = Random()

    fun generateQuestions(questionsNumber: Int): List<Question> {
        val list = mutableListOf<Question>()
        for (i in 0 until questionsNumber) {
            list.add(generateQuestion())
        }
        return list
    }

    private fun generateQuestion(): Question {
        val n = rng.nextInt(6) + 5
        val country = CountryRepository.list.random()

        val questionText = "The capital of ${country.first} is ..."

        val answersList = generateAnswersList(country, n)

        val answerIndex = rng.nextInt(n)

        return Question(
            questionText,
            answersList,
            answerIndex
        )
    }

    private fun generateAnswersList(correctCountry: Pair<String, String>, n: Int): List<Answer> {
        val answersList = mutableListOf(Answer(correctCountry.second))
        val countries = CountryRepository.list.shuffled()
        for (i in 1 until n) {
            val randomCountry = countries[i]
            answersList.add(Answer(randomCountry.second))
        }
        return answersList.shuffled()
    }
}