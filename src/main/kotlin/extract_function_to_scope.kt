fun main() {
    val service = Service(name = "Service")
    service.doSomething()
}

class Service(val name: String) {
    fun doSomething() {
        println("Doing something...")
        val param = "index"
        for (i in 1..10) {
            println("$name $param $i")
        }
    }
}