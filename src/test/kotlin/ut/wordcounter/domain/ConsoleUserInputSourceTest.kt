package ut.wordcounter.domain

import wordcounter.domain.userinput.api.*
import wordcounter.io.userinput.impl.source.impl.*
import kotlin.test.*

class ConsoleUserInputSourceTest {
    @Test
    fun `missing user input`() {
        var actual: String? = null
        val sut = ConsoleUserInputSource(
            userInputListener = object : UserInputListener {
                override fun onUserInputRead(userInput: String) {
                    actual = userInput
                }
            }
        )
        sut.readUserInput()
        assertNull(actual, "user input")
    }
}