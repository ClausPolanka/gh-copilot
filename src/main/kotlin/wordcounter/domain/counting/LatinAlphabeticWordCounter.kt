package wordcounter.domain.counting

import wordcounter.domain.words.*

class LatinAlphabeticWordCounter(
    private val wordCountListener: WordCountListener,
) : WordsListener {
    override fun onWordsAnalysed(words: List<String>) {
        val wordCount = count(words)
        wordCountListener.onWordsCounted(wordCount)
    }

    private fun count(words: List<String>): WordCount {
        val latinAlphabetWords = filterLatinAlphabeticWords(words)
        return WordCount(
            regularWordCount = latinAlphabetWords.size,
            uniqueWordCount = latinAlphabetWords.distinct().size,
            averageWordLength = latinAlphabetWords.averageWordLength()
        )
    }

    private fun filterLatinAlphabeticWords(words: List<String>): List<String> =
        words
            .filter { it.isNotEmpty() }
            .filter { w -> w.all { c -> c.isLetter() } }

    private fun List<String>.averageWordLength() =
        if (isNotEmpty()) {
            map { it.length }.average()
        } else {
            0.0
        }
}