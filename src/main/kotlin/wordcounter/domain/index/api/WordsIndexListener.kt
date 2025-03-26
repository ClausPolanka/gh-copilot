package wordcounter.domain.index.api

fun interface WordsIndexListener {
    fun onWordsIndexed(words: List<String>)
}