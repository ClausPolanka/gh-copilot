// --- Core Domain Logic ---
class AuctionSniper(
    private val item: Item,
    private val auction: Auction,
    private val listener: SniperListener,
) : AuctionEventListener {
    private var snapshot = SniperSnapshot.joining(item.identifier)

    init {
        notifyChange() // Notify initial state
    }

    override fun currentPrice(currentPrice: Int, increment: Int, priceSource: PriceSource) {
        val newSnapshot = when (priceSource) {
            PriceSource.FromSniper -> {
                // Our bid is leading
                snapshot.winning(currentPrice)
            }

            PriceSource.FromOtherBidder -> {
                val bidAmount = currentPrice + increment
                if (bidAmount <= item.stopPrice) {
                    // Price is below limit, let's bid
                    auction.bid(bidAmount)
                    snapshot.bidding(currentPrice, bidAmount)
                } else {
                    // Price is too high
                    snapshot.losing(currentPrice)
                }
            }
        }
        updateSnapshot(newSnapshot)
    }

    override fun auctionClosed() {
        updateSnapshot(snapshot.closed())
    }

    override fun auctionFailed(reason: String) {
        println("Sniper ${item.identifier} reporting auction failure: $reason")
        updateSnapshot(snapshot.failed())
    }

    private fun updateSnapshot(newSnapshot: SniperSnapshot) {
        snapshot = newSnapshot
        notifyChange()
    }

    private fun notifyChange() {
        listener.sniperStateChanged(snapshot)
    }
}