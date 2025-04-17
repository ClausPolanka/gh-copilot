package wordcounter.domain.wordsfiltering

class WordFilters {
    val latinAlphabeticStringFilter: (String) -> Boolean = { s -> s.all { c -> c.isLetter() } }
    val nonEmptyStringFilter: (String) -> Boolean = { it.isNotEmpty() }
}