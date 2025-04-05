package wordcounter.io.presentation

import wordcounter.domain.counting.*

class ConsoleWordCountListener : WordCountListener {
    override fun onWordsCounted(wordCount: WordCount) {
        val output = buildString {
            append("The text contains ${wordCount.regularWordCount} word(s), ")
            append("${wordCount.uniqueWordCount} of them unique. ")
            append("The average word length is ${"%.2f".format(wordCount.averageWordLength)} characters long.")
        }
        println(output)
    }
}