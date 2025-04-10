package wordcounter.io.stopwords

import wordcounter.domain.words.*

class StopWordsFilter(
    private val stopWords: List<String> = emptyList(),
    private val wordsListener: List<WordsListener>,
) : WordsListener {
    override fun onWordsAnalysed(words: List<String>) {
        val withoutStopWords = words.getWithoutStopWords()
        wordsListener.forEach { it.onWordsAnalysed(withoutStopWords) }
    }

    private fun List<String>.getWithoutStopWords() = filter { it !in stopWords }
}