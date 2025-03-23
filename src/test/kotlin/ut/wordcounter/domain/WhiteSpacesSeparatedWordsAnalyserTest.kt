package ut.wordcounter.domain

import wordcounter.domain.textanalysing.impl.*
import wordcounter.domain.words.*
import kotlin.test.*

class WhiteSpacesSeparatedWordsAnalyserTest {
    @Test
    fun `analyses text containing two words separated by one white space`() {
        var actual: List<String> = emptyList()
        val sut = WhiteSpacesSeparatedWordsAnalyser(
            wordsListener = object : WordsListener {
                override fun onWordsAnalysed(words: List<String>) {
                    actual = words
                }
            }
        )
        sut.analyse("word word")
        assertEquals(listOf("word", "word"), actual, "words")
    }

    @Test
    fun `analyses empty text`() {
        var actual: List<String> = emptyList()
        val sut = WhiteSpacesSeparatedWordsAnalyser(
            wordsListener = object : WordsListener {
                override fun onWordsAnalysed(words: List<String>) {
                    actual = words
                }
            }
        )
        sut.analyse(text = "")
        assertEquals(emptyList(), actual, "words")
    }

    @Test
    fun `analyses blank text`() {
        var actual: List<String> = emptyList()
        val sut = WhiteSpacesSeparatedWordsAnalyser(
            wordsListener = object : WordsListener {
                override fun onWordsAnalysed(words: List<String>) {
                    actual = words
                }
            }
        )
        sut.analyse(text = "    ")
        assertEquals(emptyList(), actual, "words")
    }
}