package ut.wordcounter.domain.counting

import wordcounter.*
import wordcounter.domain.counting.*
import wordcounter.domain.words.*
import kotlin.test.*

class LatinAlphabeticWordCounterTestTest {
    @Test
    fun `one latin alphabet word`() {
        var actual: WordCount? = null
        val sut = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf("word"))
        assertEquals(
            WordCount(
                regularWordCount = 1,
                uniqueWordCount = 1,
                averageWordLength = 4.00,
            ),
            actual,
        )
    }

    @Test
    fun `two two latin alphabet words`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf("word", "word"))
        assertEquals(
            WordCount(
                regularWordCount = 2,
                uniqueWordCount = 1,
                averageWordLength = 4.00,
            ),
            actual,
        )
    }

    @Test
    fun `non latin alphabet words (numbers)`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf("1word", "wo1rd", "word1"))
        assertEquals(
            WordCount(
                regularWordCount = 0,
                uniqueWordCount = 0,
                averageWordLength = 0.00,
            ),
            actual,
        )
    }

    @Test
    fun `non latin alphabet words (symbols)`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf("-word", "wo?rd", "word!"))
        assertEquals(
            WordCount(
                regularWordCount = 0,
                uniqueWordCount = 0,
                averageWordLength = 0.00,
            ),
            actual,
        )
    }

    @Test
    fun `words containing white space(s)`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf(" word", " word ", "word "))
        assertEquals(
            WordCount(
                regularWordCount = 0,
                uniqueWordCount = 0,
                averageWordLength = 0.00,
            ),
            actual,
        )
    }

    @Test
    fun `empty word`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf(""))
        assertEquals(
            WordCount(
                regularWordCount = 0,
                uniqueWordCount = 0,
                averageWordLength = 0.00,
            ),
            actual,
        )
    }

    @Test
    fun `blank word`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf(" "))
        assertEquals(
            WordCount(
                regularWordCount = 0,
                uniqueWordCount = 0,
                averageWordLength = 0.00,
            ),
            actual,
        )
    }

    @Test
    fun `empty words`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = emptyList())
        assertEquals(
            WordCount(
                regularWordCount = 0,
                uniqueWordCount = 0,
                averageWordLength = 0.00,
            ),
            actual,
        )
    }
}