fun main() {
    val repeater = StringRepeater()
    val input = listOf("A", "B")
    println(repeater.repeatAll(input, 3))
}

class StringRepeater {
    fun repeatAll(input: List<String>, times: Int): List<String> {
        val result = mutableListOf<String>()
        for (s in input) {
            repeat(times) {
                result.add(s)
            }
        }
        return result
    }
}