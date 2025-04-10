package wordcounter.domain.counting

fun LatinAlphabeticWordCounter(wordCountListener: WordCountListener): WordCounter {
    val latinAlphabeticStringFilter: (String) -> Boolean = { s -> s.all { c -> c.isLetter() } }
    val nonEmptyStringFilter: (String) -> Boolean = { it.isNotEmpty() }
    return WordCounter(
        wordsFilter = listOf(nonEmptyStringFilter, latinAlphabeticStringFilter),
        wordCountListener = wordCountListener
    )
}