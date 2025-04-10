package wordcounter.domain.index

import wordcounter.domain.application.*
import wordcounter.domain.index.impl.*
import wordcounter.io.index.impl.*
import wordcounter.io.presentation.*

class WordsIndices {
    fun get(options: WordCounterApplicationOptions) =
        when {
            options.hasIndexOption().not() -> EmptyWordsIndex()
            options.hasDictionaryOption() -> wordsIndexCheckedAgainstDictionary(options)
            else -> WordsIndex(ConsoleWordsIndexPrinter())
        }

    private fun wordsIndexCheckedAgainstDictionary(options: WordCounterApplicationOptions) = WordsIndex(
        wordsIndexListener = FileSystemDictionaryCheckedWordsIndex(
            wordsIndexListener = ConsoleWordsIndexPrinterCheckedAgainstDictionary(),
            errorReporter = ::println,
            filePath = options.getDictFileName(),
        )
    )
}