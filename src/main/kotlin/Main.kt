import java.io.*

fun main() {
    val userInputSource = ConsoleUserInputSource(
        userInputListener = WordCounterApplication(
            textAnalyser = WhiteSpacesSeparatedWordsAnalyser(
                wordsListener = FileSystemStopWordsRemover(
                    wordsListener = LatinAlphabeticWordCounter(
                        wordCountListener = ConsoleWordCountListener(),
                    )
                ),
            ),
        )
    )
    userInputSource.readUserInput()
}

class ConsoleUserInputSource(
    val userInputListener: UserInputListener,
) {
    fun readUserInput() {
        print("Please enter text: ")
        val userInput = readUserInputFromConsole() ?: return
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

interface UserInputListener {
    fun onUserInputRead(userInput: String)
}

class WordCounterApplication(
    private val textAnalyser: TextAnalyser,
) : UserInputListener {
    override fun onUserInputRead(userInput: String) {
        textAnalyser.analyse(text = userInput)
    }
}

interface TextAnalyser {
    fun analyse(text: String)
}

class WhiteSpacesSeparatedWordsAnalyser(
    private val wordsListener: WordsListener,
) : TextAnalyser {
    override fun analyse(text: String) {
        val words = text.split("\\s+".toRegex()).filter { it.isNotBlank() }
        wordsListener.onWordsAnalysed(words)
    }
}

class FileSystemStopWordsRemover(
    private val wordsListener: WordsListener,
) : WordsListener {
    override fun onWordsAnalysed(words: List<String>) {
        val stopWords = File("stop_words.txt").readLines()
        val withoutStopWords = words.filter { it !in stopWords }
        wordsListener.onWordsAnalysed(withoutStopWords)
    }
}

interface WordsListener {
    fun onWordsAnalysed(words: List<String>)
}

class LatinAlphabeticWordCounter(
    private val wordCountListener: WordCountListener,
) : WordsListener {
    override fun onWordsAnalysed(words: List<String>) {
        val wordCount = words.count { w -> w.all { c -> c.isLetter() } }
        wordCountListener.onWordsCounted(wordCount)
    }
}

interface WordCountListener {
    fun onWordsCounted(wordCount: Int)
}

class ConsoleWordCountListener : WordCountListener {
    override fun onWordsCounted(wordCount: Int) {
        println("The text contains $wordCount word(s).")
    }
}



