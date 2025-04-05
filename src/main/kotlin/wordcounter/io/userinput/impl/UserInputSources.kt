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
        if (isFileUserInputSource(args)) {
            return createFileInputSource(args)
        }
        return ConsoleUserInputSource(userInputListener)
    }

    private fun isFileUserInputSource(args: Array<String>): Boolean {
        val hasDict = args.any { it.contains("-dictionary") }
        val hasIndex = args.contains("-index")
        val hasAll = hasIndex && hasDict && args.size == 3
        val hasIndexAndDictionary = hasIndex && hasDict && args.size == 2
        val hasDictAndFile = hasIndex.not() && hasDict && args.size == 2
        val hasIndexAndFile = hasIndex && hasDict.not() && args.size == 2
        val fileOnly = hasIndex.not() && hasDict.not() && args.size == 1
        return hasAll.or(hasIndexAndFile).or(hasDictAndFile).or(fileOnly).and(hasIndexAndDictionary.not())
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