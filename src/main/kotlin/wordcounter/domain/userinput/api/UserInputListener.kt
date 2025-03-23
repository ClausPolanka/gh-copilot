package wordcounter.domain.userinput.api

fun interface UserInputListener {
    fun onUserInputRead(userInput: String)
}