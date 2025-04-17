package wordcounter.domain.counting

import wordcounter.domain.words.*

class WordCounter(
    private val wordsFilter: List<(String) -> Boolean> = emptyList(),
    private val wordCountListener: WordCountListener,
) : WordsListener {
    override fun onWordsAnalysed(words: List<String>) {
        val wordCount = count(words)
        wordCountListener.onWordsCounted(wordCount)
    }

    private fun count(words: List<String>): WordCount =
        WordCount(words = words.filter { word -> wordsFilter.all { f -> f(word) } })
}