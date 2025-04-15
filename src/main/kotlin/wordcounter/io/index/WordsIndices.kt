package wordcounter.io.index

import wordcounter.domain.application.*
import wordcounter.domain.index.*
import wordcounter.domain.index.impl.*
import wordcounter.io.files.*
import wordcounter.io.presentation.*

class WordsIndices {
    fun get(options: WordCounterApplicationOptions) =
        when {
            options.hasDictionaryOption() -> wordsIndexCheckedAgainstDictionary(options)
            options.hasIndexOption() -> WordsIndex(wordsIndexListener = ConsoleWordsIndexPrinter())
            else -> EmptyWordsIndex()
        }

    private fun wordsIndexCheckedAgainstDictionary(options: WordCounterApplicationOptions): WordsIndex {
        val dictionaryFile = WordCounterFile(
            filePath = options.getDictionaryFileName(),
            errorReporter = ::println
        )
        return WordsIndex(
            wordsIndexListener = DictionaryCheckedWordsIndex(
                wordsIndexListener = ConsoleWordsIndexPrinterCheckedAgainstDictionary(),
                dictionary = dictionaryFile.readContent(),
            )
        )
    }
}