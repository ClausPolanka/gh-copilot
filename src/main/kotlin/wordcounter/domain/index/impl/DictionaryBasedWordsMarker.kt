package wordcounter.domain.index.impl

import wordcounter.domain.index.api.*

private const val UNKNOWN_MARK = "*"

class DictionaryBasedWordsMarker(
    private val dictionary: List<String>,
    private val wordsIndexListener: WordsIndexListener,
) : WordsIndexListener {
    override fun onWordsIndexed(words: List<String>) {
        val markedWords = words.checkedAgainst(dictionary)
        wordsIndexListener.onWordsIndexed(markedWords)
    }

    private fun List<String>.checkedAgainst(dictionary: List<String>): List<String> =
        map { word -> word.takeIf(dictionary::contains) ?: (word + UNKNOWN_MARK) }
}