package wordcounter.io.index.impl

import wordcounter.domain.index.api.*

class DictionaryCheckedWordsIndex(
    private val wordsIndexListener: WordsIndexListener,
    private val dictionary: List<String>,
) : WordsIndexListener {
    override fun onWordsIndexed(words: List<String>) {
        val index = words.indexUsing(dictionary)
        wordsIndexListener.onWordsIndexed(index)
    }

    private fun List<String>.indexUsing(
        dictionary: List<String>,
    ) = map { if (dictionary.contains(it)) it else "$it*" }
}