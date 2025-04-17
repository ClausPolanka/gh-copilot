package wordcounter.domain.counting

import wordcounter.domain.wordsfiltering.*

fun latinAlphabeticWordCounter(wordCountListener: WordCountListener): WordCounter {
    val wordFilters = WordFilters()
    return WordCounter(
        wordsFilter = listOf(wordFilters.nonEmptyStringFilter, wordFilters.latinAlphabeticStringFilter),
        wordCountListener = wordCountListener
    )
}