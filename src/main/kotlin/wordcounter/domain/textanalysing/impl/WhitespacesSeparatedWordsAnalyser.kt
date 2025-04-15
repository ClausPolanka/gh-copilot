package wordcounter.domain.textanalysing.impl

import wordcounter.domain.textanalysing.api.*
import wordcounter.domain.words.*

class WhitespacesSeparatedWordsAnalyser(
    private val wordsListener: WordsListener,
) : TextAnalyser {
    override fun analyse(text: String) {
        val words = text.analyse()
        wordsListener.onWordsAnalysed(words)
    }

    private fun String.analyse(): List<String> =
        split("\\s+".toRegex()).filter { it.isNotBlank() }
}