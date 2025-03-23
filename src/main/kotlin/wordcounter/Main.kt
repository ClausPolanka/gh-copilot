package wordcounter

import wordcounter.domain.application.*
import wordcounter.domain.counting.*
import wordcounter.domain.textanalysing.api.*
import wordcounter.domain.textanalysing.impl.*
import wordcounter.domain.wordssanitizing.*
import wordcounter.io.presentation.*
import wordcounter.io.stopwords.*
import wordcounter.io.userinput.impl.*

fun main(args: Array<String> = emptyArray()) {
    val wordCounterApplication = WordCounterApplication(
        textAnalyser = textAnalyser(wordCounter()),
    )
    val userInputSource = UserInputSources(
        userInputListener = wordCounterApplication,
        errorReporter = ::println,
    ).get(args)
    userInputSource?.readUserInput()
}

/**
 * Ensure `wordCounter` is last in words listener chain.
 */
private fun textAnalyser(wordCounter: LatinAlphabeticWordCounter): TextAnalyser =
    WhiteSpacesSeparatedWordsAnalyser(
        wordsListener = HyphenSanitizer(
            wordsListener = PunctuationSanitizer(
                wordsListener = FileSystemStopWordsFilter(
                    wordsListener = wordCounter,
                    errorReporter = ::println,
                ),
            ),
        ),
    )

private fun wordCounter() = LatinAlphabeticWordCounter(wordCountListener = ConsoleWordCountListener())