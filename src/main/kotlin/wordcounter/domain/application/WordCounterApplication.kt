package wordcounter.domain.application

import wordcounter.domain.textanalysing.api.*
import wordcounter.domain.userinput.api.*

class WordCounterApplication(
    private val textAnalyser: TextAnalyser,
) : UserInputListener {
    override fun onUserInputRead(userInput: String) {
        textAnalyser.analyse(text = userInput)
    }
}