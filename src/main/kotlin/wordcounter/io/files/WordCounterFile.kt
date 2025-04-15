package wordcounter.io.files

import wordcounter.domain.reporting.*
import java.io.*

class WordCounterFile(
    filePath: String,
    private val errorReporter: ErrorReporter,
) {
    private val file = File(filePath)

    init {
        require(file.isFile) { "File $file is not a file." }
    }

    fun readContent(): List<String> =
        try {
            file.readLines(Charsets.UTF_8)
        } catch (e: IOException) {
            errorReporter.report("Error reading file $file: ${e.message}")
            emptyList()
        }
}