import kotlin.test.*

class FooTest {
    @Test
    fun `foo bar baz`() {
        assertEquals(
            expected = true,
            actual = true,
            message = "sample message"
        )
    }
}