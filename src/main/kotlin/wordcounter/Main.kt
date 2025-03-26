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
    val wordsIndex =
        WordsIndices().get(args)

    fun wordCounter() =
        LatinAlphabeticWordCounter(ConsoleWordCountListener())

    fun textAnalyser() = WhiteSpacesSeparatedWordsAnalyser(
        wordsListener = HyphenSanitizer(
            wordsListener = PunctuationSanitizer(
                wordsListener = FileSystemStopWordsFilter(
                    wordsListener = listOf(wordCounter(), wordsIndex),
                    errorReporter = ::println,
                ),
            ),
        ),
    )

    val userInputSource = UserInputSources(
        userInputListener = WordCounterApplication(textAnalyser()),
        errorReporter = ::println,
    ).get(args)
    userInputSource?.readUserInput()
}