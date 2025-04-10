package wordcounter.domain.wordssanitizing

import wordcounter.domain.words.*

class PunctuationSanitizer(
    private val wordsListener: WordsListener,
) : WordsListener {
    override fun onWordsAnalysed(words: List<String>) {
        wordsListener.onWordsAnalysed(words.withoutPunctuation())
    }

    private fun List<String>.withoutPunctuation() =
        map { word -> word.replace("[,;!?.]".toRegex(), "") }
}