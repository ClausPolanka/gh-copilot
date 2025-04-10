package ut.wordcounter.domain.counting

import wordcounter.domain.counting.*
import wordcounter.domain.words.*
import kotlin.test.*

class LatinAlphabeticWordCounterTestTest {
    @Test
    fun `one latin alphabet word`() {
        var actual: WordCount? = null
        val sut = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf("word"))
        assertEquals(WordCount(listOf("word")), actual)
    }

    @Test
    fun `two two latin alphabet words`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf("word", "word"))
        assertEquals(WordCount(listOf("word", "word")), actual)
    }

    @Test
    fun `non latin alphabet words (numbers)`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf("1word", "wo1rd", "word1"))
        assertEquals(WordCount(emptyList()), actual)
    }

    @Test
    fun `non latin alphabet words (symbols)`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf("-word", "wo?rd", "word!"))
        assertEquals(WordCount(emptyList()), actual)
    }

    @Test
    fun `words containing white space(s)`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf(" word", " word ", "word "))
        assertEquals(WordCount(emptyList()), actual)
    }

    @Test
    fun `empty word`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf(""))
        assertEquals(WordCount(emptyList()), actual)
    }

    @Test
    fun `blank word`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = listOf(" "))
        assertEquals(WordCount(emptyList()), actual)
    }

    @Test
    fun `empty words`() {
        var actual: WordCount? = null
        val sut: WordsListener = LatinAlphabeticWordCounter { wc -> actual = wc }
        sut.onWordsAnalysed(words = emptyList())
        assertEquals(WordCount(emptyList()), actual)
    }
}