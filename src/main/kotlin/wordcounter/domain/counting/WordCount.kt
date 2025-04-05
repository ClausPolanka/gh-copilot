package wordcounter.domain.counting

data class WordCount(
    val wordCount: Int,
    val uniqueWordCount: Int,
    val averageWordLength: Double,
)