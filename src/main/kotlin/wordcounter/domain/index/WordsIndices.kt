package wordcounter.domain.index

import wordcounter.domain.application.*
import wordcounter.domain.index.impl.*
import wordcounter.io.presentation.*

class WordsIndices {
    fun get(options: WordCounterAppOptions) =
        if (options.hasIndexOption().not())
            EmptyWordsIndex()
        else if (options.hasDictionaryOption()) {
            WordsIndex(
                wordsIndexListener = FileSystemDictionaryCheckedWordsIndex(
                    wordsIndexListener = ConsoleWordsIndexListenerDictionaryChecked(),
                    errorReporter = ::println,
                    filePath = options.getDictFileName(),
                )
            )
        } else
            WordsIndex(ConsoleWordsIndex())
}