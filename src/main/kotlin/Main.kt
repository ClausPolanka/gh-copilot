import java.io.*

fun main(args: Array<String> = emptyArray()) {
    val wordCounterApplication = WordCounterApplication(
        textAnalyser = WhiteSpacesSeparatedWordsAnalyser(
            wordsListener = HyphenSanitizer(
                wordsListener = PunctuationSanitizer(
                    wordsListener = StopWordsFileSanitizer(
                        wordsListener = LatinAlphabeticWordCounter(
                            wordCountListener = ConsoleWordCountListener(),
                        ),
                        errorReporter = ::println
                    ),
                ),
            )
        ),
    )
    val userInputSource = UserInputSources(
        userInputListener = wordCounterApplication,
        errorReporter = ::println,
    ).get(args)
    userInputSource?.readUserInput()
}

class UserInputSources(
    private val userInputListener: UserInputListener,
    private val errorReporter: ErrorReporter,
) {
    fun get(args: Array<String>): UserInputSource? {
        if (args.isNotEmpty()) {
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

interface UserInputSource {
    fun readUserInput()
}

class ConsoleUserInputSource(
    private val userInputListener: UserInputListener,
) : UserInputSource {
    override fun readUserInput() {
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

fun interface ErrorReporter {
    fun report(errorMessage: String)
}

fun interface UserInputListener {
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

class PunctuationSanitizer(
    private val wordsListener: WordsListener,
) : WordsListener {
    override fun onWordsAnalysed(words: List<String>) {
        val wordsWithoutPunctuation = words.map { word -> word.replace("[,;!?.]".toRegex(), "") }
        wordsListener.onWordsAnalysed(wordsWithoutPunctuation)
    }
}

class HyphenSanitizer(
    private val wordsListener: WordsListener,
) : WordsListener {
    companion object {
        private const val HYPHEN = "-"
    }

    override fun onWordsAnalysed(words: List<String>) {
        val sanitizedWords = words
            .asSequence()
            .filterNot { it.startsWith(HYPHEN) || it.endsWith(HYPHEN) }
            .map { it.replace(HYPHEN, "") }
            .filter { it.isNotBlank() }
            .toList()

        wordsListener.onWordsAnalysed(sanitizedWords)
    }
}

class StopWordsFileSanitizer(
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

interface WordsListener {
    fun onWordsAnalysed(words: List<String>)
}

class LatinAlphabeticWordCounter(
    private val wordCountListener: WordCountListener,
) : WordsListener {
    override fun onWordsAnalysed(words: List<String>) {
        val latinAlphabetWords = words.filter { w -> w.all { c -> c.isLetter() } }
        val wordCount = latinAlphabetWords.count()
        val uniqueWordCount = latinAlphabetWords.toSet().count()
        val averageWordLength = latinAlphabetWords.averageWordLength()
        wordCountListener.onWordsCounted(wordCount, uniqueWordCount, averageWordLength)
    }

    private fun List<String>.averageWordLength() =
        if (isNotEmpty()) {
            sumOf { it.length }.toDouble() / count()
        } else {
            0.0
        }
}

interface WordCountListener {
    fun onWordsCounted(wordCount: Int, uniqueWordCount: Int, averageWordLength: Double = 0.0)
}

class ConsoleWordCountListener : WordCountListener {
    override fun onWordsCounted(wordCount: Int, uniqueWordCount: Int, averageWordLength: Double) {
        println(
            "The text contains $wordCount word(s), " +
                "$uniqueWordCount of them unique. " +
                "The average word length is ${"%.2f".format(averageWordLength)} characters long."
        )
    }
}