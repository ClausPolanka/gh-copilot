package wordcounter

import wordcounter.domain.application.*
import wordcounter.domain.counting.*
import wordcounter.domain.textanalysing.impl.*
import wordcounter.domain.wordssanitizing.*
import wordcounter.io.presentation.*
import wordcounter.io.stopwords.*
import wordcounter.io.userinput.impl.*

fun main(args: Array<String> = emptyArray()) {
    val wordCounterApplication = WordCounterApplication(
        textAnalyser = WhiteSpacesSeparatedWordsAnalyser(
            wordsListener = HyphenSanitizer(
                wordsListener = PunctuationSanitizer(
                    wordsListener = FileSystemStopWordsFilter(
                        wordsListener = LatinAlphabeticWordCounter(
                            wordCountListener = ConsoleWordCountListener(),
                        ),
                        errorReporter = ::println
                    ),
                ),
            )
        ),
    )
    val userInputSource = UserInputSources(
        userInputListener = wordCounterApplication,
        errorReporter = ::println,
    ).get(args)
    userInputSource?.readUserInput()
}