package wordcounter.domain.counting

import wordcounter.domain.wordsfiltering.*

fun LatinAlphabeticWordCounter(wordCountListener: WordCountListener): WordCounter {
    val wordsFilter = WordsFilter()
    return WordCounter(
        wordsFilter = listOf(wordsFilter.nonEmptyStringFilter, wordsFilter.latinAlphabeticStringFilter),
        wordCountListener = wordCountListener
    )
}