interface AuctionEventListener {
    fun currentPrice(currentPrice: Int, increment: Int, priceSource: PriceSource)
    fun auctionClosed()
    fun auctionFailed(reason: String) // Added for simplicity
}