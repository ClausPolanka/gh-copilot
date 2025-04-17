package wordcounter

import wordcounter.domain.application.*
import wordcounter.domain.counting.*
import wordcounter.domain.stopwords.*
import wordcounter.domain.textanalysing.api.*
import wordcounter.domain.textanalysing.impl.*
import wordcounter.domain.words.*
import wordcounter.domain.wordssanitizing.*
import wordcounter.io.files.*
import wordcounter.io.index.*
import wordcounter.io.presentation.*
import wordcounter.io.userinput.impl.*

fun main(args: Array<String> = emptyArray()) {
    val options = WordCounterApplicationOptions(args)
    val userInputSource = UserInputSources(
        userInputListener = wordCounterApplication(options),
        errorReporter = ::println,
    ).get(options)
    userInputSource?.readUserInput()
}

fun wordCounterApplication(options: WordCounterApplicationOptions) =
    WordCounterApplication(
        textAnalyser(
            wordCounterFile = stopWordsFile(),
            wordCounter = LatinAlphabeticWordCounter(wordCountListener = ConsoleWordCountPrinter()),
            wordsListener = WordsIndices().get(options)
        )
    )

fun stopWordsFile() =
    WordCounterFile(
        filePath = "stop_words.txt",
        errorReporter = ::println,
    )

fun textAnalyser(
    wordCounterFile: WordCounterFile,
    wordCounter: WordCounter,
    wordsListener: WordsListener,
): TextAnalyser =
    WhitespacesSeparatedWordsAnalyser(
        wordsListener = HyphenSanitizer(
            wordsListener = PunctuationSanitizer(
                wordsListener = StopWordsFilter(
                    stopWords = wordCounterFile.readContent(),
                    wordsListener = listOf(wordCounter, wordsListener),
                ),
            ),
        ),
    )