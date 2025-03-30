data class SniperSnapshot(
    val itemId: String,
    val lastPrice: Int,
    val lastBid: Int,
    val state: SniperState,
) {
    // Factory methods for state transitions (GOOS style)
    fun bidding(newLastPrice: Int, newBid: Int) =
        copy(lastPrice = newLastPrice, lastBid = newBid, state = SniperState.BIDDING)

    fun winning(newLastPrice: Int) =
        copy(lastPrice = newLastPrice, state = SniperState.WINNING)

    fun losing(newLastPrice: Int) =
        copy(lastPrice = newLastPrice, state = SniperState.LOSING)

    fun closed() = copy(state = state.whenAuctionClosed())
    fun failed() = copy(state = SniperState.FAILED)

    companion object {
        fun joining(itemId: String) = SniperSnapshot(itemId, 0, 0, SniperState.JOINING)
    }
}