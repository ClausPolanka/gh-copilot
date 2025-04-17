package wordcounter.domain.counting

import wordcounter.domain.words.*

class WordCounter(
    private val wordsFilter: List<(String) -> Boolean> = emptyList(),
    private val wordCountListener: WordCountListener,
) : WordsListener {
    override fun onWordsAnalysed(words: List<String>) =
        wordCountListener.onWordsCounted(words.count())

    private fun List<String>.count() =
        WordCount(words = filtered())

    private fun List<String>.filtered(): List<String> =
        filter { word -> wordsFilter.all { f -> f(word) } }
}