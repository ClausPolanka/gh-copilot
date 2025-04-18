data class User(val id: Int, val username: String, val email: String, val secretInfo: String)

val users = mapOf(
    1 to User(1, "alice", "alice@example.com", "Alice's secret is apples."),
    2 to User(2, "bob", "bob@example.com", "Bob's secret is bananas.")
)
val userCredentials = mapOf(
    "alice" to "password123",
    "bob" to "securepass"
)