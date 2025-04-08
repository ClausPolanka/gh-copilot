package wordcounter.io.presentation

import wordcounter.domain.counting.*
import java.text.*
import java.util.*

class ConsoleWordCountListener : WordCountListener {
    override fun onWordsCounted(wordCount: WordCount) {
        print(wordCount)
    }

    private fun print(wordCount: WordCount) {
        val output = buildString {
            with(wordCount) {
                append("The text contains $regularWordCount word(s), ")
                append("$uniqueWordCount of them unique. ")
                append("The average word length is ${averageWordLength.format()} characters long.")
            }
        }
        println(output)
    }
}

fun Double.format(
    locale: Locale = Locale.getDefault(),
    decimals: Int = 2,
): String =
    NumberFormat.getNumberInstance(locale).apply {
        minimumFractionDigits = decimals
        maximumFractionDigits = decimals
    }.format(this)