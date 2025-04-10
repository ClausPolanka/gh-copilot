package wordcounter

import wordcounter.domain.application.*
import wordcounter.domain.counting.*
import wordcounter.domain.index.*
import wordcounter.domain.textanalysing.impl.*
import wordcounter.domain.wordssanitizing.*
import wordcounter.io.presentation.*
import wordcounter.io.stopwords.*
import wordcounter.io.userinput.impl.*

fun main(args: Array<String> = emptyArray()) {
    val options = WordCounterApplicationOptions(args)
    val wordsIndex = WordsIndices().get(options)
    val wordCounter = LatinAlphabeticWordCounter(ConsoleWordCountPrinter())
    fun textAnalyser() = WhiteSpacesSeparatedWordsAnalyser(
        wordsListener = HyphenSanitizer(
            wordsListener = PunctuationSanitizer(
                wordsListener = FileSystemStopWordsFilter(
                    wordsListener = listOf(wordCounter, wordsIndex),
                    errorReporter = ::println,
                ),
            ),
        ),
    )

    val userInputSource = UserInputSources(
        userInputListener = WordCounterApplication(textAnalyser()),
        errorReporter = ::println,
    ).get(options)
    userInputSource?.readUserInput()
}

fun LatinAlphabeticWordCounter(wordCountListener: WordCountListener): WordCounter {
    val latinAlphabeticStringFilter: (String) -> Boolean = { s -> s.all { c -> c.isLetter() } }
    val nonEmptyStringFilter: (String) -> Boolean = { it.isNotEmpty() }
    return WordCounter(
        wordsFilter = listOf(nonEmptyStringFilter, latinAlphabeticStringFilter),
        wordCountListener = wordCountListener
    )
}