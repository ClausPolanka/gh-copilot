package wordcounter.domain.index

import wordcounter.domain.index.api.*
import wordcounter.domain.words.*

class WordsIndex(
    private val wordsIndexListener: WordsIndexListener,
) : WordsListener {
    override fun onWordsAnalysed(words: List<String>) =
        wordsIndexListener.onWordsIndexed(words.index())

    private fun List<String>.index(): List<String> =
        sortedBy { it.lowercase() }.distinct()
}