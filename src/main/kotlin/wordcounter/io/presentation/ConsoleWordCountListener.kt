package wordcounter.io.presentation

import wordcounter.domain.counting.*

class ConsoleWordCountListener : WordCountListener {
    override fun onWordsCounted(wordCount: WordCount) {
        val output = buildString {
            with(wordCount) {
                append("The text contains $regularWordCount word(s), ")
                append("$uniqueWordCount of them unique. ")
                append("The average word length is ${"%.2f".format(averageWordLength)} characters long.")
            }
        }
        println(output)
    }
}