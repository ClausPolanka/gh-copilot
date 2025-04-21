fun main() {
    val s = Super()
    s.doSomething()
    println(s.name)
    val i = Impl()
    i.doSomething()
    println(i.name)
    val i2 = Impl2()
    i2.doSomething()
    println(i2.name)
}

open class Super {
    val name = "Super"
    fun doSomething() {
        println("Doing something with $name...")
    }
}

class Impl : Super() {
}

class Impl2 : Super() {
}