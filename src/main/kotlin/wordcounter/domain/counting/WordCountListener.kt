package wordcounter.domain.counting

fun interface WordCountListener {
    fun onWordsCounted(wordCount: WordCount)
}