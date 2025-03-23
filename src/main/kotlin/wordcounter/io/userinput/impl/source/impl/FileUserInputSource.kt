package wordcounter.io.userinput.impl.source.impl

import wordcounter.domain.reporting.*
import wordcounter.domain.userinput.api.*
import wordcounter.io.userinput.impl.source.api.*
import java.io.*

class FileUserInputSource(
    private val userInputListener: UserInputListener,
    private val errorReporter: ErrorReporter,
    filePath: String,
) : UserInputSource {
    private val file = File(filePath)

    init {
        require(file.isFile) { "File $file is not a file." }
    }

    override fun readUserInput() {
        val userInput = try {
            file.readLines(Charsets.UTF_8).joinToString(separator = " ")
        } catch (e: IOException) {
            errorReporter.report("Error reading file $file: ${e.message}")
            return
        }
        userInputListener.onUserInputRead(userInput)
    }
}