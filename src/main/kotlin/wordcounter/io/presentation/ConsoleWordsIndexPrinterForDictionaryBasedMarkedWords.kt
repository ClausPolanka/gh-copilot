package wordcounter.io.presentation

import wordcounter.domain.index.api.*

private const val UNKNOWN_MARK = "*"

class ConsoleWordsIndexPrinterForDictionaryBasedMarkedWords : WordsIndexListener {
    override fun onWordsIndexed(words: List<String>) {
        println("Index: (unknown: ${words.unknowns()})")
        words.forEach(::println)
    }

    private fun List<String>.unknowns() =
        filter { it.endsWith(UNKNOWN_MARK) }.size
}