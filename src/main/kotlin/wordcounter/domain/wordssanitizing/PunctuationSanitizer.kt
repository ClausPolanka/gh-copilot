package wordcounter.domain.wordssanitizing

import wordcounter.domain.words.*

class PunctuationSanitizer(
    private val wordsListener: WordsListener,
) : WordsListener {
    override fun onWordsAnalysed(words: List<String>) {
        val wordsWithoutPunctuation = words.map { word -> word.replace("[,;!?.]".toRegex(), "") }
        wordsListener.onWordsAnalysed(wordsWithoutPunctuation)
    }
}