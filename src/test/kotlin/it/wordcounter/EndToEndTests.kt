package it.wordcounter

import main
import org.hamcrest.*
import org.junit.*
import java.io.*

class EndToEndTests {
    @Test
    fun `a user enters text containing latin alphabetic words while ignoring stop words`() {
        aUserEnters("Humpty-Dumpty sat on a wall. Humpty-Dumpty had a great fall.")
        main()
        MatcherAssert.assertThat(
            uiOutput(),
            CoreMatchers.containsString("The text contains 9 word(s), 7 of them unique.")
        )
    }

    @Test
    fun `a user provides a file as a user input source`() {
        main(arrayOf("my_text.txt"))
        MatcherAssert.assertThat(
            uiOutput(),
            CoreMatchers.containsString("The text contains 4 word(s), 4 of them unique.")
        )
    }

    @Test
    fun `a user enters a word containing numbers`() {
        aUserEnters("Ma3ry")
        main()
        MatcherAssert.assertThat(
            uiOutput(),
            CoreMatchers.containsString("The text contains 0 word(s), 0 of them unique.")
        )
    }

    @Test
    fun `a user enters a word ending with a punctuation mark`() {
        aUserEnters("Mary?")
        main()
        MatcherAssert.assertThat(
            uiOutput(),
            CoreMatchers.containsString("The text contains 1 word(s), 1 of them unique.")
        )
    }

    @Test
    fun `a user enters a blank text`() {
        aUserEnters("    ")
        main()
        MatcherAssert.assertThat(
            uiOutput(),
            CoreMatchers.containsString("The text contains 0 word(s), 0 of them unique.")
        )
    }

    @Test
    fun `a user enters nothing and then presses enter`() {
        aUserEnters("")
        main()
        MatcherAssert.assertThat(
            uiOutput(),
            CoreMatchers.containsString("The text contains 0 word(s), 0 of them unique.")
        )
    }

    private fun uiOutput() = outputStream.toString().trim()

    @Before
    fun setUp() {
        System.setOut(PrintStream(outputStream))
    }

    @After
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