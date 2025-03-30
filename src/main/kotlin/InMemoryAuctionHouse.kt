class InMemoryAuctionHouse : AuctionHouse {
    private val auctions = mutableMapOf<String, FakeAuctionServer>()
    override fun auctionFor(item: Item): Auction {
        return auctions.getOrPut(item.identifier) {
            println("Creating fake auction for ${item.identifier}")
            FakeAuctionServer(item.identifier)
        }
    }

    override fun addAuctionEventListener(item: Item, listener: AuctionEventListener) {
        val auction = auctions[item.identifier]
        if (auction != null) {
            auction.addEventListener(listener)
            // Start the auction simulation when the first listener joins
            if (!auction.hasStarted()) {
                auction.startSellingItem()
            }
        } else {
            // Handle error: auction not found? For simplicity, maybe fail listener
            println("Error: Tried to add listener to non-existent auction ${item.identifier}")
            listener.auctionFailed("Auction ${item.identifier} not found.")
        }
    }
}