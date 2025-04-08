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

    private fun count(words: List<String>): WordCount {
        val filteredWords = words.filter { word -> wordsFilter.all { f -> f(word) } }
        return WordCount(
            regularWordCount = filteredWords.size,
            uniqueWordCount = filteredWords.distinct().size,
            averageWordLength = filteredWords.averageWordLength()
        )
    }

    private fun List<String>.averageWordLength() =
        if (isNotEmpty()) {
            map { it.length }.average()
        } else {
            0.0
        }
}