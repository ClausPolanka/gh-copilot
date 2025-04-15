package wordcounter.domain.index.impl

import wordcounter.domain.index.api.*

private const val UNKNOWN_MARK = "*"

class DictionaryCheckedWordsIndex(
    private val wordsIndexListener: WordsIndexListener,
    private val dictionary: List<String>,
) : WordsIndexListener {
    override fun onWordsIndexed(words: List<String>) {
        val index = words.indexUsing(dictionary)
        wordsIndexListener.onWordsIndexed(index)
    }

    private fun List<String>.indexUsing(dictionary: List<String>): List<String> =
        map { word -> word.takeIf(dictionary::contains) ?: (word + UNKNOWN_MARK) }
}