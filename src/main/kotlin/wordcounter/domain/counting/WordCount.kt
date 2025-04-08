package wordcounter.domain.counting

data class WordCount(
    private val words: List<String>,
) {
    fun all() = words.size
    fun unique() = words.distinct().size
    fun averageWordLength() =
        if (words.isNotEmpty()) {
            words.map { it.length }.average()
        } else {
            0.0
        }
}