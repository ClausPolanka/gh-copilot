package wordcounter.domain.counting

interface WordCountListener {
    fun onWordsCounted(wordCount: Int, uniqueWordCount: Int, averageWordLength: Double)
}