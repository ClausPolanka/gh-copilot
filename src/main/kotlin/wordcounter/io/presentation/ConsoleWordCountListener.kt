package wordcounter.io.presentation

import wordcounter.domain.counting.*

class ConsoleWordCountListener : WordCountListener {
    override fun onWordsCounted(wordCount: Int, uniqueWordCount: Int, averageWordLength: Double) {
        println(
            "The text contains $wordCount word(s), " +
                "$uniqueWordCount of them unique. " +
                "The average word length is ${"%.2f".format(averageWordLength)} characters long."
        )
    }
}