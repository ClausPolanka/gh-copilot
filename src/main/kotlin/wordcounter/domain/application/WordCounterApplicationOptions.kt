package wordcounter.domain.application

private const val DICTIONARY_OPTION = "-dictionary"
private const val INDEX_OPTION = "-index"

class WordCounterApplicationOptions(
    private val args: Array<String>,
) {
    init {
        require(
            value = args.filter { it.startsWith("-") }
                .filterNot { it == INDEX_OPTION || it.startsWith(DICTIONARY_OPTION) }
                .isEmpty(),
            lazyMessage = { "wc [-index] [-dictionary=<dict.txt>] [<file>]" }
        )
    }

    fun hasIndexOption() = args.contains(INDEX_OPTION)
    fun hasDictionaryOption() = args.any { it.contains(DICTIONARY_OPTION) }
    fun getDictFileName() = args
        .find { it.startsWith(DICTIONARY_OPTION) }
        ?.split("=")?.get(1)
        ?: error("Invalid -dictionary option")

    fun hasUserFileInput(): Boolean {
        val hasDict = args.any { it.contains(DICTIONARY_OPTION) }
        val hasIndex = args.contains(INDEX_OPTION)
        val hasAll = hasIndex && hasDict && args.size == 3
        val hasIndexAndDictionary = hasIndex && hasDict && args.size == 2
        val hasDictAndFile = hasIndex.not() && hasDict && args.size == 2
        val hasIndexAndFile = hasIndex && hasDict.not() && args.size == 2
        val fileOnly = hasIndex.not() && hasDict.not() && args.size == 1
        return hasAll.or(hasIndexAndFile).or(hasDictAndFile).or(fileOnly).and(hasIndexAndDictionary.not())
    }

    fun getUserFileInput(): String {
        if (hasUserFileInput()) {
            return args.filterNot { it.contains("-") }.first()
        } else {
            error("Invalid user input file")
        }
    }
}