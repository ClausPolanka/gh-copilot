package wordcounter.io.presentation

import wordcounter.domain.index.api.*

private const val UNKNOWN_MARK = "*"

class ConsoleWordsIndexListenerDictionaryChecked : WordsIndexListener {
    override fun onWordsIndexed(words: List<String>) {
        val unknownWords = words.filter { it.endsWith(UNKNOWN_MARK) }
        println("Index: (unknown: ${unknownWords.size})")
        words.forEach(::println)
    }
}