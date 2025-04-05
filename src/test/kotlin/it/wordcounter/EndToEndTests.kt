package it.wordcounter

import org.junit.jupiter.api.*
import wordcounter.*
import wordcounter.io.presentation.*
import java.io.*
import java.lang.System.*
import kotlin.test.*
import kotlin.test.Test

class EndToEndTests {
    @Test
    fun `a user enters text containing latin alphabetic words while ignoring stop words`() {
        aUserEnters("Humpty-Dumpty sat on a wall. Humpty-Dumpty had a great fall.")
        main()
        assertEquals(
            uiOutput(),
            "The text contains 7 word(s), 6 of them unique. The average word length is ${6.14.format()} characters long."
        )
    }

    @Test
    fun `a user provides a file as a user input source`() {
        main(arrayOf("my_text.txt"))
        assertEquals(
            uiOutput(),
            "The text contains 4 word(s), 4 of them unique. The average word length is ${4.25.format()} characters long."
        )
    }

    @Test
    fun `a user enters a word containing numbers`() {
        aUserEnters("Ma3ry")
        main()
        assertEquals(
            uiOutput(),
            "The text contains 0 word(s), 0 of them unique. The average word length is ${0.00.format()} characters long."
        )
    }

    @Test
    fun `a user enters a word ending with a punctuation mark`() {
        aUserEnters("Mary?")
        main()
        assertEquals(
            uiOutput(),
            "The text contains 1 word(s), 1 of them unique. The average word length is ${4.00.format()} characters long."
        )
    }

    @Test
    fun `a user enters a blank text`() {
        aUserEnters("    ")
        main()
        assertEquals(
            uiOutput(),
            "The text contains 0 word(s), 0 of them unique. The average word length is ${0.00.format()} characters long."
        )
    }

    @Test
    fun `a user enters nothing and then presses enter`() {
        aUserEnters("")
        main()
        assertEquals(
            uiOutput(),
            "The text contains 0 word(s), 0 of them unique. The average word length is ${0.00.format()} characters long."
        )
    }

    @Test
    fun `a user enters only stop words`() {
        aUserEnters("a on the off")
        main()
        assertEquals(
            uiOutput(),
            "The text contains 0 word(s), 0 of them unique. The average word length is ${0.00.format()} characters long."
        )
    }

    @Test
    fun `a user wants to see an index of the given user input`() {
        aUserEnters("Mary had a little lamb")
        main(args = arrayOf("-index"))
        val expected = StringBuilder()
            .appendLine("The text contains 4 word(s), 4 of them unique. The average word length is ${4.25.format()} characters long.")
            .appendLine("Index:")
            .appendLine("had")
            .appendLine("lamb")
            .appendLine("little")
            .append("Mary")
            .toString()
        assertEquals(uiOutput(), expected)
    }

    private fun uiOutput() = outputStream.toString().trim().removePrefix("Please enter text: ")

    @BeforeEach
    fun setUp() {
        setOut(PrintStream(outputStream))
    }

    @AfterEach
    fun tearDown() {
        setOut(out)
        setIn(`in`)
    }

    private var outputStream = ByteArrayOutputStream()
    private lateinit var inputStream: ByteArrayInputStream
    private fun aUserEnters(userInput: String) {
        inputStream = ByteArrayInputStream("$userInput${lineSeparator()}".toByteArray())
        setIn(inputStream)
    }
}