package wordcounter.domain.textanalysing.impl

import wordcounter.domain.textanalysing.api.*
import wordcounter.domain.words.*

class WhiteSpacesSeparatedWordsAnalyser(
    private val wordsListener: WordsListener,
) : TextAnalyser {
    override fun analyse(text: String) {
        val words = text.split("\\s+".toRegex()).filter { it.isNotBlank() }
        wordsListener.onWordsAnalysed(words)
    }
}