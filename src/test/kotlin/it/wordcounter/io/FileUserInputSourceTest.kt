package it.wordcounter.io

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.*
import wordcounter.domain.userinput.api.*
import wordcounter.io.userinput.impl.source.impl.*
import java.io.*
import kotlin.test.*

class FileUserInputSourceTest {
    @Test
    fun `correctly reads input from a file`(@TempDir tempDir: File) {
        val file = File(tempDir, "test_single_line.txt")
        val fileContent = "This is a sample text"
        file.writeText(fileContent)
        val userInputListener = UserInputListenerMock()
        val sut = FileUserInputSource(userInputListener, { }, file.absolutePath)
        sut.readUserInput()
        assertEquals(fileContent, userInputListener.receivedInput)
    }

    @Test
    fun `handles a file with multiple lines`(@TempDir tempDir: File) {
        val file = File(tempDir, "test_multiline.txt")
        val fileContent = "Line one\nLine two\nLine three"
        file.writeText(fileContent)
        val userInputListener = UserInputListenerMock()
        val sut = FileUserInputSource(userInputListener, { }, file.absolutePath)
        sut.readUserInput()
        assertEquals("Line one Line two Line three", userInputListener.receivedInput)
    }

    @Test
    fun `throws when file doesn't exist`() {
        assertThrows<IllegalArgumentException> {
            FileUserInputSource(
                userInputListener = { },
                errorReporter = { },
                filePath = "non_existent_file.txt",
            )
        }
    }

    @Test
    fun `throws when file path is a directory`(@TempDir tempDir: File) {
        assertThrows<IllegalArgumentException> {
            FileUserInputSource(
                userInputListener = { },
                errorReporter = { },
                filePath = tempDir.absolutePath,
            )
        }
    }

    class UserInputListenerMock : UserInputListener {
        var receivedInput: String? = null
        override fun onUserInputRead(userInput: String) {
            receivedInput = userInput
        }
    }
}