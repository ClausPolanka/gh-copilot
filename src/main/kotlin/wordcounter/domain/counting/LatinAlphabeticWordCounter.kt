package wordcounter.domain.counting

import wordcounter.domain.words.*

class LatinAlphabeticWordCounter(
    private val wordCountListener: WordCountListener,
) : WordsListener {
    override fun onWordsAnalysed(words: List<String>) {
        val latinAlphabetWords = words.filter { w -> w.all { c -> c.isLetter() } }
        wordCountListener.onWordsCounted(
            WordCount(
                latinAlphabetWords.size,
                latinAlphabetWords.distinct().size,
                latinAlphabetWords.averageWordLength()
            )
        )
    }

    private fun List<String>.averageWordLength() =
        if (isNotEmpty()) {
            map { it.length }.average()
        } else {
            0.0
        }
}