// endregion Domain Layer
// region Infrastructure Layer (In-Memory Auction Simulation)
interface AuctionHouse {
    fun auctionFor(item: Item): Auction // Returns the bidding interface
    fun addAuctionEventListener(item: Item, listener: AuctionEventListener) // Connects sniper to auction events
}