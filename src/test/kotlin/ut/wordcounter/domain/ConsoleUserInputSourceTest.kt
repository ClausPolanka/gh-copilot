package ut.wordcounter.domain

import ConsoleUserInputSource
import UserInputListener
import org.junit.*
import org.junit.Assert.*

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
        assertNull("user input", actual)
    }
}