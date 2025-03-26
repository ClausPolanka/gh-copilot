package wordcounter.domain.index

import wordcounter.domain.index.impl.*
import wordcounter.io.presentation.*

class WordsIndices {
    fun get(args: Array<String>) =
        if (args.contains("-index").not())
            EmptyWordsIndex()
        else
            WordsIndex(ConsoleWordsWordsIndexListener())
}