package wordcounter.domain.index

import wordcounter.domain.index.api.*
import wordcounter.domain.words.*

class WordsIndex(
    private val wordsIndexListener: WordsIndexListener,
) : WordsListener {
    override fun onWordsAnalysed(words: List<String>) {
        val sortedWords = words.sortedBy { it.lowercase() }.distinct()
        wordsIndexListener.onWordsIndexed(sortedWords)
    }
}