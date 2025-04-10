package wordcounter.domain.stopwords

import wordcounter.domain.words.*

class StopWordsFilter(
    private val stopWords: List<String> = emptyList(),
    private val wordsListener: List<WordsListener>,
) : WordsListener {
    override fun onWordsAnalysed(words: List<String>) {
        val withoutStopWords = words.withoutStopWords()
        wordsListener.forEach { it.onWordsAnalysed(withoutStopWords) }
    }

    private fun List<String>.withoutStopWords() = filter { it !in stopWords }
}