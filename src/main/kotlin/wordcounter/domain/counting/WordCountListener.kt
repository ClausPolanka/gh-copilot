package wordcounter.domain.counting

interface WordCountListener {
    fun onWordsCounted(wordCount: WordCount)
}