package it.wordcounter

import org.junit.jupiter.api.*
import wordcounter.*
import wordcounter.domain.application.*
import wordcounter.io.presentation.*
import wordcounter.io.userinput.impl.*
import wordcounter.io.userinput.impl.source.impl.*
import java.io.*
import java.lang.System.*
import kotlin.test.*
import kotlin.test.Test

class EndToEndTests {
    @Test
    fun `a user enters text containing latin alphabetic words while ignoring stop words`() {
        aUserEnters("Humpty-Dumpty sat on a wall. Humpty-Dumpty had a great fall.")
        consoleUserInputSource().readUserInput()
        assertEquals(
            "The text contains 7 word(s), 6 of them unique. The average word length is ${6.14.format()} characters long.",
            uiOutput()
        )
    }

    @Test
    fun `a user provides a file as a user input source`() {
        fileUserInputSource(args = arrayOf("my_text.txt")).readUserInput()
        assertEquals(
            "The text contains 4 word(s), 4 of them unique. The average word length is ${4.25.format()} characters long.",
            uiOutput()
        )
    }

    @Test
    fun `a user enters a word containing numbers`() {
        aUserEnters("Ma3ry")
        consoleUserInputSource().readUserInput()
        assertEquals(
            "The text contains 0 word(s), 0 of them unique. The average word length is ${0.00.format()} characters long.",
            uiOutput()
        )
    }

    @Test
    fun `a user enters a word ending with a punctuation mark`() {
        aUserEnters("Mary?")
        consoleUserInputSource().readUserInput()
        assertEquals(
            "The text contains 1 word(s), 1 of them unique. The average word length is ${4.00.format()} characters long.",
            uiOutput()
        )
    }

    @Test
    fun `a user enters a blank text`() {
        aUserEnters("    ")
        consoleUserInputSource().readUserInput()
        assertEquals(
            "The text contains 0 word(s), 0 of them unique. The average word length is ${0.00.format()} characters long.",
            uiOutput()
        )
    }

    @Test
    fun `a user enters nothing and then presses enter`() {
        aUserEnters("")
        consoleUserInputSource().readUserInput()
        assertEquals(
            "The text contains 0 word(s), 0 of them unique. The average word length is ${0.00.format()} characters long.",
            uiOutput()
        )
    }

    @Test
    fun `a user enters only stop words`() {
        aUserEnters("a on the off")
        consoleUserInputSource().readUserInput()
        assertEquals(
            "The text contains 0 word(s), 0 of them unique. The average word length is ${0.00.format()} characters long.",
            uiOutput()
        )
    }

    @Test
    fun `a user wants to see an index of the given user input`() {
        aUserEnters("Mary had a little lamb")
        consoleUserInputSource(args = arrayOf("-index")).readUserInput()
        val expected = StringBuilder()
            .appendLine("The text contains 4 word(s), 4 of them unique. The average word length is ${4.25.format()} characters long.")
            .appendLine("Index:")
            .appendLine("had")
            .appendLine("lamb")
            .appendLine("little")
            .append("Mary")
            .toString()
        assertEquals(expected, uiOutput())
    }

    @Test
    fun `a user wants to see an index of the given user input checked against a dictionary`() {
        aUserEnters("Mary had a little lamb")
        consoleUserInputSource(args = arrayOf("-index", "-dictionary=dict.txt")).readUserInput()
        val expected = StringBuilder()
            .appendLine("The text contains 4 word(s), 4 of them unique. The average word length is ${4.25.format()} characters long.")
            .appendLine("Index: (unknown: 2)")
            .appendLine("had")
            .appendLine("lamb*")
            .appendLine("little")
            .append("Mary*")
            .toString()
        assertEquals(expected, uiOutput())
    }

    @Test
    fun `a user applies invalid program option`() {
        aUserEnters("Doesn't matter")
        assertThrows<IllegalArgumentException>(
            { "Invalid program option should throw" },
            { consoleUserInputSource(args = arrayOf("-invalid")).readUserInput() }
        )
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

    private fun consoleUserInputSource(args: Array<String> = emptyArray<String>()) =
        ConsoleUserInputSource(
            userInputListener = wordCounterApplication(WordCounterApplicationOptions(args)),
        )

    private fun fileUserInputSource(args: Array<String> = emptyArray<String>()) =
        UserInputSources(
            userInputListener = wordCounterApplication(WordCounterApplicationOptions(args)),
            errorReporter = ::println,
        ).get(WordCounterApplicationOptions(args))!!

    private var outputStream = ByteArrayOutputStream()
    private lateinit var inputStream: ByteArrayInputStream
    private fun aUserEnters(userInput: String) {
        inputStream = ByteArrayInputStream("$userInput${lineSeparator()}".toByteArray())
        setIn(inputStream)
    }
}