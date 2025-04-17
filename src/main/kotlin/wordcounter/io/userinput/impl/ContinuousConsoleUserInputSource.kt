package wordcounter.io.userinput.impl

import wordcounter.io.userinput.impl.UserInputSources.*
import wordcounter.io.userinput.impl.source.api.*

class ContinuousConsoleUserInputSource(
    private val delegate: UserInputSource,
) : UserInputSource {
    override fun readUserInput() {
        while (true) {
            try {
                delegate.readUserInput()
            } catch (_: EmptyUserInputException) {
                break // Exit program
            }
        }
    }
}