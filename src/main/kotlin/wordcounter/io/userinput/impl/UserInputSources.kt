package wordcounter.io.userinput.impl

import wordcounter.domain.application.*
import wordcounter.domain.reporting.*
import wordcounter.domain.userinput.api.*
import wordcounter.io.userinput.impl.source.api.*
import wordcounter.io.userinput.impl.source.impl.*

class UserInputSources(
    private val userInputListener: UserInputListener,
    private val errorReporter: ErrorReporter,
) {
    fun get(options: WordCounterApplicationOptions): UserInputSource? {
        if (options.hasUserFileInput()) {
            return createFileInputSource(options)
        }
        return ConsoleUserInputSource(userInputListener)
    }

    private fun createFileInputSource(args: WordCounterApplicationOptions): FileUserInputSource? {
        val fileUserInputSource = try {
            FileUserInputSource(
                userInputListener,
                errorReporter,
                filePath = args.getUserFileInput()
            )
        } catch (e: Exception) {
            errorReporter.report("Error creating FileUserInputSource: ${e.message}")
            return null
        }
        return fileUserInputSource
    }
}