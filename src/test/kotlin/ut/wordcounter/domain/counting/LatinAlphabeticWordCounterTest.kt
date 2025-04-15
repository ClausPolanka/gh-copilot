package ut.wordcounter.domain.counting

import wordcounter.domain.counting.*
import wordcounter.domain.words.*
import kotlin.test.*

class LatinAlphabeticWordCounterTest {
    @Test
    fun `one latin alphabet word`() {
        var actual: WordCount? = null
        val sut = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf("word"))
        assertEquals(1, actual?.all())
    }

    @Test
    fun `two latin alphabet words`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf("word", "word"))
        assertEquals(2, actual?.all())
    }

    @Test
    fun `non latin alphabet words (numbers)`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf("1word", "wo1rd", "word1"))
        assertEquals(0, actual?.all())
    }

    @Test
    fun `non latin alphabet words (symbols)`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf("-word", "wo?rd", "word!"))
        assertEquals(0, actual?.all())
    }

    @Test
    fun `words containing white space(s)`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf(" word", " word ", "word "))
        assertEquals(0, actual?.all())
    }

    @Test
    fun `empty word`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf(""))
        assertEquals(0, actual?.all())
    }

    @Test
    fun `blank word`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf(" "))
        assertEquals(0, actual?.all())
    }

    @Test
    fun `empty words`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = emptyList())
        assertEquals(0, actual?.all())
    }
}