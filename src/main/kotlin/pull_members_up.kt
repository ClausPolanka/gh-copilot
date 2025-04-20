fun main() {
    val cia = ConcreteImplementationA(
        name = "A",
        items = listOf("Hello", "World")
    )
    cia.doSomething()
    val cib = ConcreteImplementationB(
        name = "B",
        items = listOf("Hello", "World")
    )
    cib.doSomething()
}

class ConcreteImplementationA(
    private val name: String,
    private val items: List<String>,
) {
    fun doSomething() {
        println("Concrete Implementation: $name")
        items.forEach { println(it) }
    }
}

class ConcreteImplementationB(
    private val name: String,
    private val items: List<String>,
) {
    fun doSomething() {
        println("Concrete Implementation: $name")
        items.forEach { println(it) }
    }
}