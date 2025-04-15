package wordcounter.io.presentation

import wordcounter.domain.counting.*
import java.text.*
import java.util.*

class ConsoleWordCountPrinter : WordCountListener {
    override fun onWordsCounted(wordCount: WordCount) {
        println(wordCount.toConsoleOutput())
    }

    private fun WordCount.toConsoleOutput(): String =
        buildString {
            append("The text contains ${totalCount()} word(s), ")
            append("${uniqueCount()} of them unique. ")
            append("The average word length is ${averageWordLength().format()} characters long.")
        }
}

fun Double.format(
    locale: Locale = Locale.getDefault(),
    decimals: Int = 2,
): String = NumberFormat.getNumberInstance(locale).apply {
    minimumFractionDigits = decimals
    maximumFractionDigits = decimals
}.format(this)