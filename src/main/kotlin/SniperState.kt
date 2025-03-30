enum class SniperState {
    JOINING,
    BIDDING,
    WINNING,
    LOSING,
    LOST,
    WON,
    FAILED; // Added for simplicity

    fun whenAuctionClosed(): SniperState = when (this) {
        WINNING -> WON
        JOINING, BIDDING, LOSING -> LOST
        LOST, WON, FAILED -> this // Already terminal
    }
}