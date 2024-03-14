## CandlestickChart Composable

The `CandlestickChart` composable function is a customizable UI component designed to display financial candlestick chart data. It provides a visual representation of the open, close, high, and low prices of a financial instrument over a specified time period.

### Usage

To integrate the `CandlestickChart` into your Jetpack Compose UI, follow these steps:

1. Import the required libraries and ensure your project is set up to use Jetpack Compose.

    ```kotlin
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.unit.dp
    ```

2. Create a list of `CandlestickData` objects representing the financial data you want to display on the chart. Each `CandlestickData` object should contain information about the open, close, high, and low prices, as well as the corresponding time period.

    ```kotlin
    val candlestickDataList = listOf(
        CandlestickData(time = "2024-01-01", open = 100f, close = 110f, high = 120f, low = 90f),
        CandlestickData(time = "2024-01-02", open = 110f, close = 115f, high = 118f, low = 105f),
        // Add more CandlestickData objects as needed
    )
    ```

3. Use the `CandlestickChart` composable within your Composable function, passing in the list of `CandlestickData` objects and specifying optional parameters for customization.

    ```kotlin
    @Composable
    fun MyCandlestickChart() {
        CandlestickChart(
            data = candlestickDataList,
            minY = 80f, // Optional: Minimum Y-axis value (default: 0f)
            maxY = 130f, // Maximum Y-axis value
            candleWidth = 10f // Width of each candlestick
        )
    }
    ```

### Customization

The `CandlestickChart` composable provides several parameters for customization:

- `data`: List of `CandlestickData` objects representing the financial data to be displayed on the chart.

- `minY`: Optional parameter specifying the minimum value on the Y-axis. Defaults to 0f if not provided.

- `maxY`: Required parameter specifying the maximum value on the Y-axis.

- `candleWidth`: Width of each candlestick on the chart.

### Example

Below is an example of how to use the `CandlestickChart` composable within a Composable function:

```kotlin
@Composable
fun MyCandlestickChart() {
    val candlestickDataList = listOf(
        CandlestickData(time = "2024-01-01", open = 100f, close = 110f, high = 120f, low = 90f),
        CandlestickData(time = "2024-01-02", open = 110f, close = 115f, high = 118f, low = 105f),
        // Add more CandlestickData objects as needed
    )

    CandlestickChart(
        data = candlestickDataList,
        minY = 80f,
        maxY = 130f,
        candleWidth = 10f
    )
}