package wordcounter.domain.index.impl

import wordcounter.domain.index.api.*

private const val UNKNOWN_MARK = "*"

class WordsIndexCheckedAgainstDictionary(
    private val dictionary: List<String>,
    private val wordsIndexListener: WordsIndexListener,
) : WordsIndexListener {
    override fun onWordsIndexed(words: List<String>) {
        val index = words.indexCheckedAgainst(dictionary)
        wordsIndexListener.onWordsIndexed(index)
    }

    private fun List<String>.indexCheckedAgainst(dictionary: List<String>): List<String> =
        map { word -> word.takeIf(dictionary::contains) ?: (word + UNKNOWN_MARK) }
}