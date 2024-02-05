import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chaintech.candlechartcompose.extensions.dpToPx
import com.chaintech.candlechartcompose.model.CandlestickData
import com.chaintech.candlechartcompose.ui.CandlestickChart

@Composable
fun App() {
    MaterialTheme {
        val data = remember { generateCandlestickData() }
        Box(modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .height(400.dp)) {
            CandlestickChart(data = data, minY = 15000f, maxY = 36000f, candleWidth = 20.dp.dpToPx())
        }
    }
}

fun generateCandlestickData(): List<CandlestickData> {
    return listOf(
        CandlestickData("23-07-2023", 80f, 90f, 70f, 85f),
        CandlestickData("24-07-2023", 85f, 95f, 75f, 90f),
        CandlestickData("25-07-2023", 90f, 100f, 80f, 95f),
        CandlestickData("26-07-2023", 80f, 89f, 80f, 78f),
        CandlestickData("27-07-2023", 80f, 89f, 80f, 78f),
        CandlestickData("28-07-2023", 80f, 89f, 80f, 78f),
        CandlestickData("29-07-2023", 80f, 89f, 80f, 78f),
        CandlestickData("30-07-2023", 80f, 90f, 70f, 85f),
        CandlestickData("31-07-2023", 80f, 89f, 80f, 78f),
        CandlestickData("01-08-2023", 80f, 90f, 70f, 85f),
        CandlestickData("02-08-2023", 80f, 89f, 80f, 78f),
        CandlestickData("03-08-2023", 80f, 90f, 70f, 85f),
        CandlestickData("04-08-2023", 80f, 90f, 70f, 85f),
        CandlestickData("05-08-2023", 60f, 80f, 50f, 78f),
        CandlestickData("06-08-2023", 80f, 89f, 80f, 78f),
        CandlestickData("07-08-2023", 80f, 89f, 80f, 78f),
        CandlestickData("07-08-2023", 80f, 89f, 80f, 78f),
        CandlestickData("07-08-2023", 80f, 89f, 80f, 78f),
    )
}

