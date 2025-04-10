package wordcounter

import wordcounter.domain.application.*
import wordcounter.domain.counting.*
import wordcounter.domain.stopwords.*
import wordcounter.domain.textanalysing.impl.*
import wordcounter.domain.wordssanitizing.*
import wordcounter.io.files.*
import wordcounter.io.index.*
import wordcounter.io.presentation.*
import wordcounter.io.userinput.impl.*

fun main(args: Array<String> = emptyArray()) {
    val options = WordCounterApplicationOptions(args)
    val wordsIndex = WordsIndices().get(options)
    val wordCounter = LatinAlphabeticWordCounter(ConsoleWordCountPrinter())
    val stopWords = WordCounterFile(
        filePath = "stop_words.txt",
        errorReporter = ::println,
    )
    val textAnalyser = WhiteSpacesSeparatedWordsAnalyser(
        wordsListener = HyphenSanitizer(
            wordsListener = PunctuationSanitizer(
                wordsListener = StopWordsFilter(
                    stopWords = stopWords.readFileContent(),
                    wordsListener = listOf(wordCounter, wordsIndex),
                ),
            ),
        ),
    )
    val userInputSource = UserInputSources(
        userInputListener = WordCounterApplication(textAnalyser),
        errorReporter = ::println,
    ).get(options)
    userInputSource?.readUserInput()
}