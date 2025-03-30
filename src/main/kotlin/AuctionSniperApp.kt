import javafx.application.*
import javafx.scene.*
import javafx.stage.*

class AuctionSniperApp : Application() {
    private val auctionHouse: AuctionHouse = InMemoryAuctionHouse() // Use our fake one
    private lateinit var mainWindow: MainWindow
    override fun start(primaryStage: Stage) {
        mainWindow = MainWindow(auctionHouse)

        primaryStage.title = "Auction Sniper (Kotlin/JavaFX)"
        primaryStage.scene = Scene(mainWindow.createView(), 500.0, 300.0)
        primaryStage.show()
    }

    // Optional: Cleanup if needed when the app closes
    override fun stop() {
        println("Application stopping.")
        // Potentially tell the auction house or auctions to clean up resources
        // For this simple version, daemon threads in FakeAuctionServer might suffice.
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(AuctionSniperApp::class.java, *args)
        }
    }
}