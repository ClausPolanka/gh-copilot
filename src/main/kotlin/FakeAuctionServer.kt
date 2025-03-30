import kotlin.concurrent.*
import kotlin.random.*

// Simulates a single auction process
class FakeAuctionServer(private val itemId: String) : Auction {
    private var listener: AuctionEventListener? = null
    private var currentPrice = Random.nextInt(50, 150) // Start with some price
    private var increment = Random.nextInt(5, 25)
    private var highestBidder: String? = null // null initially, or "Sniper" or "Other"
    private var running = false
    private var stop = false
    fun addEventListener(newListener: AuctionEventListener) {
        if (listener == null) {
            listener = newListener
            println("Listener added for $itemId")
        } else {
            println("Warning: Replacing listener for $itemId")
            listener = newListener // Allow replacing for simplicity, GOOS might handle differently
        }
    }

    fun hasStarted(): Boolean = running
    fun startSellingItem() {
        if (running) return
        running = true
        println("Auction for $itemId starting. Initial price: $currentPrice")

        thread(isDaemon = true) { // Simulate auction in background thread
            try {
                // Initial price announcement
                reportPrice()
                Thread.sleep(1000) // Pause
                // Simulate a few rounds of bidding
                repeat(Random.nextInt(3, 7)) {
                    if (stop) return@thread
                    val otherBid = Random.nextBoolean() // Simulate if another bidder bids

                    if (otherBid) {
                        val oldPrice = currentPrice
                        currentPrice += increment
                        highestBidder = "Other"
                        println("[$itemId] Other bidder bids. Price: $currentPrice (was $oldPrice)")
                        reportPrice()
                        Thread.sleep(Random.nextLong(800, 2000)) // Pause
                    } else {
                        // If sniper was last bidder, just wait. If not, other bidder might win if sniper doesn't bid.
                        println("[$itemId] No other bids this round. Current price: $currentPrice")
                        Thread.sleep(Random.nextLong(800, 1500)) // Pause
                    }
                }

                if (!stop) {
                    println("[$itemId] Auction closing.")
                    listener?.auctionClosed()
                }
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                println("[$itemId] Auction thread interrupted.")
                if (!stop) listener?.auctionFailed("Auction interrupted")
            } catch (e: Exception) {
                println("[$itemId] Unexpected error in auction thread: ${e.message}")
                if (!stop) listener?.auctionFailed("Internal auction error")
            } finally {
                running = false
                println("[$itemId] Auction simulation finished.")
            }
        }
    }

    // Sniper calls this method
    override fun bid(amount: Int) {
        println("[$itemId] Received bid of $amount from Sniper.")
        if (amount > currentPrice) {
            currentPrice = amount
            highestBidder = "Sniper"
            reportPrice() // Report the new price caused by the sniper's bid
        } else {
            println("[$itemId] Sniper bid $amount ignored (not higher than $currentPrice)")
        }
    }

    private fun reportPrice() {
        val source = if (highestBidder == "Sniper") PriceSource.FromSniper else PriceSource.FromOtherBidder
        println("[$itemId] Reporting Price: $currentPrice, Inc: $increment, Source: $source")
        listener?.currentPrice(currentPrice, increment, source)
    }

    // For potential cleanup if needed
    fun stopAuction() {
        stop = true
    }
}