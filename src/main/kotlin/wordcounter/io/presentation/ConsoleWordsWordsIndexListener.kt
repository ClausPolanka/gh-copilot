package wordcounter.io.presentation

import wordcounter.domain.index.api.*

class ConsoleWordsWordsIndexListener : WordsIndexListener {
    override fun onWordsIndexed(words: List<String>) {
        println("Index:")
        words.forEach(::println)
    }
}