package wordcounter.domain.reporting

fun interface ErrorReporter {
    fun report(errorMessage: String)
}