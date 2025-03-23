import java.io.*

fun main(args: Array<String> = emptyArray()) {
    val wordCounterApplication = WordCounterApplication(
        textAnalyser = WhiteSpacesSeparatedWordsAnalyser(
            wordsListener = HyphenSanatizer(
                wordsListener = PunctuationSanatizer(
                    wordsListener = FileSystemStopWordsSanatizer(
                        wordsListener = LatinAlphabeticWordCounter(
                            wordCountListener = ConsoleWordCountListener(),
                        )
                    ),
                ),
            )
        ),
    )
    val userInputSource = UserInputSources(
        userInputListener = wordCounterApplication
    ).get(args)
    userInputSource.readUserInput()
}

class UserInputSources(
    private val userInputListener: UserInputListener,
) {
    fun get(args: Array<String>): UserInputSource {
        if (args.isNotEmpty()) {
            return FileUserInputSource(userInputListener, args[0])
        }
        return ConsoleUserInputSource(userInputListener)
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
    private val file: String,
) : UserInputSource {
    override fun readUserInput() {
        val userInput = File(file).readLines().joinToString(separator = " ")
        userInputListener.onUserInputRead(userInput)
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

class PunctuationSanatizer(
    private val wordsListener: WordsListener,
) : WordsListener {
    override fun onWordsAnalysed(words: List<String>) {
        val wordsWithoutPunctuation = words.map { word -> word.replace("[,;!?.]".toRegex(), "") }
        wordsListener.onWordsAnalysed(wordsWithoutPunctuation)
    }
}

class HyphenSanatizer(
    private val wordsListener: WordsListener,
) : WordsListener {
    override fun onWordsAnalysed(words: List<String>) {
        val wordsWithoutHyphen = words
            .asSequence()
            .filter { word -> word.startsWith("-").not() }
            .filter { word -> word.endsWith("-").not() }
            .map { word -> word.replace("-".toRegex(), " ").split(" ") }
            .flatten()
            .filter { it.isNotBlank() }
            .toList()
        wordsListener.onWordsAnalysed(wordsWithoutHyphen)
    }
}

class FileSystemStopWordsSanatizer(
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
        val latinAlphabetWords = words.filter { w -> w.all { c -> c.isLetter() } }
        val wordCount = latinAlphabetWords.count()
        val uniqueWordCount = latinAlphabetWords.toSet().count()
        wordCountListener.onWordsCounted(wordCount, uniqueWordCount)
    }
}

interface WordCountListener {
    fun onWordsCounted(wordCount: Int, uniqueWordCount: Int)
}

class ConsoleWordCountListener : WordCountListener {
    override fun onWordsCounted(wordCount: Int, uniqueWordCount: Int) {
        println("The text contains $wordCount word(s), $uniqueWordCount of them unique.")
    }
}