package wordcounter.io.userinput.impl.source.impl

import wordcounter.domain.userinput.api.*
import wordcounter.io.userinput.impl.source.api.*

class ConsoleUserInputSource(
    private val emptyUserInputBehaviour: () -> String = { "" }, // Do nothing
    private val userInputListener: UserInputListener,
) : UserInputSource {
    override fun readUserInput() {
        print("Please enter text: ")
        val userInput = readUserInputFromConsole() ?: return
        userInput.ifBlank(defaultValue = emptyUserInputBehaviour)
        userInputListener.onUserInputRead(userInput)
    }

    private fun readUserInputFromConsole(): String? {
        return readlnOrNull() ?: run {
            println()
            println("Something went wrong. Please try again.")
            null
        }
    }
}