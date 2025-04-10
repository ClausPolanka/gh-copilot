package wordcounter.domain.words

interface WordsListener {
    fun onWordsAnalysed(words: List<String>)
}