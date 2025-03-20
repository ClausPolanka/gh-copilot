fun main() {
    print("Please enter text: ")
    val userInput = readUserInput() ?: return
    val wordCount = countLatinAlphabeticWords(userInput)
    println("The text contains $wordCount word(s).")
}

fun readUserInput(): String? {
    return readlnOrNull() ?: run {
        println()
        println("Something went wrong. Please try again.")
        null
    }
}

fun countLatinAlphabeticWords(userInput: String): Int {
    return userInput
        .takeUnless { it.isBlank() }
        ?.split("\\s+".toRegex())
        ?.filter(String::isNotBlank)
        ?.count(::isLatinAlphabeticWord)
        ?: 0
}

fun isLatinAlphabeticWord(word: String): Boolean {
    return word.all { it.isLetter() }
}