import javafx.application.*
import javafx.collections.*

// --- Adapter: Connects Sniper Domain Events to JavaFX UI Thread ---
class FXThreadSniperListener(
    private val snipers: ObservableList<SniperTableData>,
) : SniperListener {
    override fun sniperStateChanged(newSnapshot: SniperSnapshot) {
        Platform.runLater { // Ensure UI update runs on FX Application Thread
            val existing = snipers.find { it.itemIdProperty.get() == newSnapshot.itemId }
            if (existing != null) {
                existing.update(newSnapshot)
            } else {
                // Add new sniper row if it doesn't exist yet
                snipers.add(SniperTableData(newSnapshot))
            }
            println("UI Updated for ${newSnapshot.itemId} on FX thread. State: ${newSnapshot.state}")
        }
    }
}