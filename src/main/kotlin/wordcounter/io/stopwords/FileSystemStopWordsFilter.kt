package wordcounter.io.stopwords

import wordcounter.domain.reporting.*
import wordcounter.domain.words.*
import java.io.*

class FileSystemStopWordsFilter(
    private val wordsListener: WordsListener,
    private val errorReporter: ErrorReporter,
) : WordsListener {
    private val file = File("stop_words.txt")

    init {
        require(file.isFile) { "File $file is not a file." }
    }

    override fun onWordsAnalysed(words: List<String>) {
        val stopWords = try {
            file.readLines()
        } catch (e: Exception) {
            errorReporter.report("Error reading file $file: ${e.message}")
            return
        }
        val withoutStopWords = words.filter { it !in stopWords }
        wordsListener.onWordsAnalysed(withoutStopWords)
    }
}