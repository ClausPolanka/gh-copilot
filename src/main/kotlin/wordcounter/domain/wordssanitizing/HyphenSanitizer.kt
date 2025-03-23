package wordcounter.domain.wordssanitizing

import wordcounter.domain.words.*

class HyphenSanitizer(
    private val wordsListener: WordsListener,
) : WordsListener {
    companion object {
        private const val HYPHEN = "-"
    }

    override fun onWordsAnalysed(words: List<String>) {
        val sanitizedWords = words
            .asSequence()
            .filterNot { it.startsWith(HYPHEN) || it.endsWith(HYPHEN) }
            .map { it.replace(HYPHEN, "") }
            .filter { it.isNotBlank() }
            .toList()

        wordsListener.onWordsAnalysed(sanitizedWords)
    }
}