package wordcounter.io.presentation

import wordcounter.domain.index.api.*

class ConsoleWordsIndexPrinter : WordsIndexListener {
    override fun onWordsIndexed(words: List<String>) {
        println("Index:")
        words.forEach(::println)
    }
}