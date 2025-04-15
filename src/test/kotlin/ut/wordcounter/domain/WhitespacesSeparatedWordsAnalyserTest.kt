package ut.wordcounter.domain

import wordcounter.domain.textanalysing.impl.*
import kotlin.test.*

class WhitespacesSeparatedWordsAnalyserTest {
    @Test
    fun `analyses text containing two words separated by one whitespace`() {
        var actual: List<String> = emptyList()
        val sut = WhitespacesSeparatedWordsAnalyser(
            wordsListener = { words -> actual = words }
        )
        sut.analyse("word word")
        assertEquals(listOf("word", "word"), actual, "words")
    }

    @Test
    fun `analyses empty text`() {
        var actual: List<String> = emptyList()
        val sut = WhitespacesSeparatedWordsAnalyser(
            wordsListener = { words -> actual = words }
        )
        sut.analyse(text = "")
        assertEquals(emptyList(), actual, "words")
    }

    @Test
    fun `analyses blank text`() {
        var actual: List<String> = emptyList()
        val sut = WhitespacesSeparatedWordsAnalyser(
            wordsListener = { words -> actual = words }
        )
        sut.analyse(text = "    ")
        assertEquals(emptyList(), actual, "words")
    }
}