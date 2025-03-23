package it.wordcounter

import org.junit.jupiter.api.*
import wordcounter.*
import java.io.*
import kotlin.test.*
import kotlin.test.Test

class EndToEndTests {
    @Test
    fun `a user enters text containing latin alphabetic words while ignoring stop words`() {
        aUserEnters("Humpty-Dumpty sat on a wall. Humpty-Dumpty had a great fall.")
        main()
        assertContains(
            uiOutput(),
            "The text contains 7 word(s), 6 of them unique. The average word length is 6,14 characters long."
        )
    }

    @Test
    fun `a user provides a file as a user input source`() {
        main(arrayOf("my_text.txt"))
        assertContains(
            uiOutput(),
            "The text contains 4 word(s), 4 of them unique. The average word length is 4,25 characters long."
        )
    }

    @Test
    fun `a user enters a word containing numbers`() {
        aUserEnters("Ma3ry")
        main()
        assertContains(
            uiOutput(),
            "The text contains 0 word(s), 0 of them unique. The average word length is 0,00 characters long."
        )
    }

    @Test
    fun `a user enters a word ending with a punctuation mark`() {
        aUserEnters("Mary?")
        main()
        assertContains(
            uiOutput(),
            "The text contains 1 word(s), 1 of them unique. The average word length is 4,00 characters long."
        )
    }

    @Test
    fun `a user enters a blank text`() {
        aUserEnters("    ")
        main()
        assertContains(
            uiOutput(),
            "The text contains 0 word(s), 0 of them unique. The average word length is 0,00 characters long."
        )
    }

    @Test
    fun `a user enters nothing and then presses enter`() {
        aUserEnters("")
        main()
        assertContains(
            uiOutput(),
            "The text contains 0 word(s), 0 of them unique. The average word length is 0,00 characters long."
        )
    }

    @Test
    fun `a user enters only stop words`() {
        aUserEnters("a on the off")
        main()
        assertContains(
            uiOutput(),
            "The text contains 0 word(s), 0 of them unique. The average word length is 0,00 characters long."
        )
    }

    private fun uiOutput() = outputStream.toString().trim()

    @BeforeEach
    fun setUp() {
        System.setOut(PrintStream(outputStream))
    }

    @AfterEach
    fun tearDown() {
        System.setOut(System.out)
        System.setIn(System.`in`)
    }

    private var outputStream = ByteArrayOutputStream()
    private lateinit var inputStream: ByteArrayInputStream
    private fun aUserEnters(userInput: String) {
        inputStream = ByteArrayInputStream("$userInput${System.lineSeparator()}".toByteArray())
        System.setIn(inputStream)
    }
}