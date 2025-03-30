// --- Interfaces ---
interface Auction {
    fun bid(amount: Int)
    // We'll implicitly join when the sniper starts listening in this simplified version
}