package wordcounter.domain.application

class WordCounterAppOptions(
    private val args: Array<String>,
) {
    fun hasIndexOption() = args.contains("-index")
    fun hasDictionaryOption() = args.any { it.contains("-dictionary") }
    fun getDictFileName() = args
        .find { it.startsWith("-dictionary") }
        ?.split("=")?.get(1)
        ?: error("Invalid -dictionary option")

    fun hasUserFileInput(): Boolean {
        val hasDict = args.any { it.contains("-dictionary") }
        val hasIndex = args.contains("-index")
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