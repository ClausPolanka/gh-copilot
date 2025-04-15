package wordcounter.domain.stopwords

import wordcounter.domain.words.*

class StopWordsFilter(
    private val stopWords: List<String> = emptyList(),
    private val wordsListener: List<WordsListener>,
) : WordsListener {
    override fun onWordsAnalysed(words: List<String>) =
        wordsListener.forEach { it.onWordsAnalysed(words.withoutStopWords()) }

    private fun List<String>.withoutStopWords() = filter { it !in stopWords }
}