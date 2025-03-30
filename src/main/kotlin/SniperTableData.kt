import javafx.beans.property.*

// endregion Infrastructure Layer
// region UI Layer (JavaFX) and Adapters
// --- Data class for TableView ---
// Needs JavaFX properties for binding
class SniperTableData(
    snapshot: SniperSnapshot,
) {
    val itemIdProperty = SimpleStringProperty(snapshot.itemId)
    val lastPriceProperty = SimpleStringProperty(snapshot.lastPrice.toString())
    val lastBidProperty = SimpleStringProperty(snapshot.lastBid.toString())
    val stateProperty = SimpleStringProperty(snapshot.state.name)
    fun update(snapshot: SniperSnapshot) {
        // Properties are updated, TableView refreshes automatically
        lastPriceProperty.set(snapshot.lastPrice.toString())
        lastBidProperty.set(snapshot.lastBid.toString())
        stateProperty.set(snapshot.state.name)
    }
}