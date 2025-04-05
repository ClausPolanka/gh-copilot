package wordcounter.domain.index

import wordcounter.domain.index.impl.*
import wordcounter.io.presentation.*

class WordsIndices {
    fun get(args: Array<String>) =
        if (args.contains("-index").not())
            EmptyWordsIndex()
        else if (args.any { it.contains("-dictionary") }) {
            val dictFileName =
                args.find { it.startsWith("-dictionary") }?.split("=")?.get(1) ?: error("Invalid -dictionary option")
            WordsIndex(
                wordsIndexListener = FileSystemDictionaryCheckedWordsIndex(
                    wordsIndexListener = ConsoleWordsIndexListenerDictionaryChecked(),
                    errorReporter = ::println,
                    filePath = dictFileName,
                )
            )
        } else
            WordsIndex(ConsoleWordsIndexListener())
}