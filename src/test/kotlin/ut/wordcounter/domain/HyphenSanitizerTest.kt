package ut.wordcounter.domain

import wordcounter.domain.words.*
import wordcounter.domain.wordssanitizing.*
import kotlin.test.*

class HyphenSanitizerTest {
    @Test
    fun `words beginning with a hyphen are filtered out`() {
        val wordsListener = WordsListenerMock()
        val sut = HyphenSanitizer(wordsListener)

        sut.onWordsAnalysed(words = listOf("-word"))

        assertEquals(emptyList(), wordsListener.receivedWords)
    }

    @Test
    fun `words ending with a hyphen are filtered out`() {
        val wordsListener = WordsListenerMock()
        val sut = HyphenSanitizer(wordsListener)

        sut.onWordsAnalysed(words = listOf("word-"))

        assertEquals(emptyList(), wordsListener.receivedWords)
    }

    @Test
    fun `hyphen in the middle of a word is removed`() {
        val wordsListener = WordsListenerMock()
        val sut = HyphenSanitizer(wordsListener)

        sut.onWordsAnalysed(words = listOf("word-word"))

        assertEquals(listOf("wordword"), wordsListener.receivedWords)
    }

    @Test
    fun `multiple hyphens in a word are removed`() {
        val wordsListener = WordsListenerMock()
        val sut = HyphenSanitizer(wordsListener)

        sut.onWordsAnalysed(words = listOf("word-word-word"))

        assertEquals(listOf("wordwordword"), wordsListener.receivedWords)
    }

    @Test
    fun `words without hyphens remain unchanged`() {
        val wordsListener = WordsListenerMock()
        val sut = HyphenSanitizer(wordsListener)

        sut.onWordsAnalysed(words = listOf("word", "word"))

        assertEquals(listOf("word", "word"), wordsListener.receivedWords)
    }

    @Test
    fun `empty input results in empty output`() {
        val wordsListener = WordsListenerMock()
        val sut = HyphenSanitizer(wordsListener)

        sut.onWordsAnalysed(words = emptyList())

        assertEquals(emptyList(), wordsListener.receivedWords)
    }

    @Test
    fun `word consisting only of hyphen is filtered out`() {
        val wordsListener = WordsListenerMock()
        val sut = HyphenSanitizer(wordsListener)

        sut.onWordsAnalysed(words = listOf("-"))

        assertEquals(emptyList(), wordsListener.receivedWords)
    }

    @Test
    fun `word with multiple leading and trailing hyphens isn't sanitized`() {
        val wordsListener = WordsListenerMock()
        val sut = HyphenSanitizer(wordsListener)

        sut.onWordsAnalysed(words = listOf("--word--"))

        assertEquals(emptyList(), wordsListener.receivedWords)
    }

    @Test
    fun `only hyphens in the input result in empty output`() {
        val wordsListener = WordsListenerMock()
        val sut = HyphenSanitizer(wordsListener)

        sut.onWordsAnalysed(words = listOf("-", "-", "-"))

        assertEquals(emptyList(), wordsListener.receivedWords)
    }

    class WordsListenerMock : WordsListener {
        var receivedWords: List<String> = emptyList()
        override fun onWordsAnalysed(words: List<String>) {
            receivedWords = words
        }
    }
}