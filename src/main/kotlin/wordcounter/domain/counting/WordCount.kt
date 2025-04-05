package wordcounter.domain.counting

data class WordCount(
    val regularWordCount: Int,
    val uniqueWordCount: Int,
    val averageWordLength: Double,
)