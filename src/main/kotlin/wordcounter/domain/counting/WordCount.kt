package wordcounter.domain.counting

data class WordCount(
    private val words: List<String>,
) {
    fun totalCount() = words.size
    fun uniqueCount() = words.distinct().size
    fun averageWordLength() =
        if (words.isNotEmpty()) {
            words.map { it.length }.average()
        } else {
            0.0
        }
}