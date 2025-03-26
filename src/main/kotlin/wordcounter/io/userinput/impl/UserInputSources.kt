package wordcounter.io.userinput.impl

import wordcounter.domain.reporting.*
import wordcounter.domain.userinput.api.*
import wordcounter.io.userinput.impl.source.api.*
import wordcounter.io.userinput.impl.source.impl.*

class UserInputSources(
    private val userInputListener: UserInputListener,
    private val errorReporter: ErrorReporter,
) {
    fun get(args: Array<String>): UserInputSource? {
        if ((args.contains("-index") && args.size == 2).or(args.contains("-index").not() && args.size == 1)) {
            return createFileInputSource(args)
        }
        return ConsoleUserInputSource(userInputListener)
    }

    private fun createFileInputSource(args: Array<String>): FileUserInputSource? {
        val fileUserInputSource = try {
            FileUserInputSource(
                userInputListener,
                errorReporter,
                filePath = args[0]
            )
        } catch (e: Exception) {
            errorReporter.report("Error creating FileUserInputSource: ${e.message}")
            return null
        }
        return fileUserInputSource
    }
}