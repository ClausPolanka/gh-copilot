package wordcounter.domain.index

import wordcounter.domain.application.*
import wordcounter.domain.index.impl.*
import wordcounter.io.files.*
import wordcounter.io.index.impl.*
import wordcounter.io.presentation.*

class WordsIndices {
    fun get(options: WordCounterApplicationOptions) = when {
        options.hasIndexOption().not() -> EmptyWordsIndex()
        options.hasDictionaryOption() -> wordsIndexCheckedAgainstDictionary(options)
        else -> WordsIndex(ConsoleWordsIndexPrinter())
    }

    private fun wordsIndexCheckedAgainstDictionary(options: WordCounterApplicationOptions): WordsIndex {
        val file = WordCounterFile(
            filePath = options.getDictFileName(),
            errorReporter = ::println
        )
        val fileContent: List<String> = file.readFileContent()
        return WordsIndex(
            wordsIndexListener = DictionaryCheckedWordsIndex(
                wordsIndexListener = ConsoleWordsIndexPrinterCheckedAgainstDictionary(),
                dictionary = fileContent,
            )
        )
    }
}