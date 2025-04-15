package wordcounter.domain.words

fun interface WordsListener {
    fun onWordsAnalysed(words: List<String>)
}