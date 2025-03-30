import javafx.collections.*
import javafx.geometry.*
import javafx.scene.control.*
import javafx.scene.control.cell.*
import javafx.scene.layout.*
import kotlin.random.*

// --- Main Application Window ---
class MainWindow(private val auctionHouse: AuctionHouse) {
    private val snipers = FXCollections.observableArrayList<SniperTableData>()
    private val sniperListener: SniperListener = FXThreadSniperListener(snipers)
    fun createView(): BorderPane {
        val tableView = TableView(snipers)
        tableView.columns.addAll(
            TableColumn<SniperTableData, String>("Item ID").apply {
                setCellValueFactory(PropertyValueFactory("itemIdProperty"))
                prefWidth = 150.0
            },
            TableColumn<SniperTableData, String>("Last Price").apply {
                setCellValueFactory(PropertyValueFactory("lastPriceProperty"))
                prefWidth = 100.0
            },
            TableColumn<SniperTableData, String>("Last Bid").apply {
                setCellValueFactory(PropertyValueFactory("lastBidProperty"))
                prefWidth = 100.0
            },
            TableColumn<SniperTableData, String>("State").apply {
                setCellValueFactory(PropertyValueFactory("stateProperty"))
                prefWidth = 120.0
            }
        )
        tableView.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        val itemIdField = TextField().apply { promptText = "Item ID" }
        val stopPriceField = TextField().apply { promptText = "Stop Price" }
        val joinButton = Button("Join Auction")

        joinButton.setOnAction {
            val itemId = itemIdField.text
            val stopPrice = stopPriceField.text.toIntOrNull()

            if (itemId.isNotBlank() && stopPrice != null && stopPrice > 0) {
                joinAuction(Item(itemId, stopPrice))
                itemIdField.text = "item-${Random.nextInt(100, 999)}" // Suggest next item
                stopPriceField.clear()
            } else {
                println("Invalid Item ID or Stop Price")
                // Optionally show an alert dialog here
            }
        }
        // Suggest initial values
        itemIdField.text = "item-${Random.nextInt(100, 999)}"
        stopPriceField.text = Random.nextInt(200, 500).toString()
        val controls = HBox(10.0, itemIdField, stopPriceField, joinButton).apply {
            padding = Insets(10.0)
        }

        return BorderPane().apply {
            center = tableView
            bottom = controls
        }
    }

    private fun joinAuction(item: Item) {
        println("UI: User wants to join auction for ${item.identifier} up to ${item.stopPrice}")
        // 1. Get the auction interface from the house
        val auction: Auction = auctionHouse.auctionFor(item)
        // 2. Create the sniper domain object
        val sniper = AuctionSniper(item, auction, sniperListener)
        // 3. Connect the sniper to receive events from the auction house infrastructure
        auctionHouse.addAuctionEventListener(item, sniper)
        // 4. Add to UI table immediately (via listener callback in constructor/init)
        // The initial state notification from sniper's init block handles adding the row
        println("UI: Sniper created and listener registered for ${item.identifier}.")
    }
}