package wordcounter.domain.index

import wordcounter.domain.index.api.*

class ConsoleWordsIndex : WordsIndexListener {
    override fun onWordsIndexed(words: List<String>) {
        println("Index:")
        words.forEach(::println)
    }
}