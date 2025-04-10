package wordcounter.domain.index.impl

import wordcounter.domain.words.*

class EmptyWordsIndex : WordsListener {
    override fun onWordsAnalysed(words: List<String>) {
        // Intended to be emtpy
    }
}