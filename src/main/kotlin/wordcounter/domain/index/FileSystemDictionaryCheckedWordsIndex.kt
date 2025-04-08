package wordcounter.domain.index

import wordcounter.domain.index.api.*
import wordcounter.domain.reporting.*
import java.io.*

class FileSystemDictionaryCheckedWordsIndex(
    private val wordsIndexListener: WordsIndexListener,
    private val errorReporter: ErrorReporter,
    filePath: String,
) : WordsIndexListener {
    private val file = File(filePath)

    init {
        require(file.isFile) { "File $file is not a file." }
    }

    override fun onWordsIndexed(words: List<String>) {
        val dictionary = createDictionaryFrom(file)
        val index = words.indexUsing(dictionary)
        wordsIndexListener.onWordsIndexed(index)
    }

    private fun createDictionaryFrom(f: File): List<String> {
        return try {
            f.readLines(Charsets.UTF_8)
        } catch (e: IOException) {
            errorReporter.report("Error reading file $f: ${e.message}")
            emptyList()
        }
    }

    private fun List<String>.indexUsing(
        dictionary: List<String>,
    ) = map { if (dictionary.contains(it)) it else "$it*" }
}