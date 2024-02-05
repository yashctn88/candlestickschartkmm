package com.chaintech.candlechartcompose.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chaintech.candlechartcompose.extensions.contains
import com.chaintech.candlechartcompose.extensions.dpToPx
import com.chaintech.candlechartcompose.extensions.pxToDp
import com.chaintech.candlechartcompose.model.CandlestickData
import com.chaintech.candlechartcompose.model.KMMRect
import kotlin.math.max
import kotlin.math.min
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
public fun CandlestickChart(
    data: List<CandlestickData>,
    minY: Float = 0f,
    maxY: Float,
    candleWidth: Float
) {
    val scrollState = rememberScrollState()
    val listCandleBounds = remember {
        arrayListOf<KMMRect>()
    }
    val minChartY = remember { mutableStateOf(0f) }
    val maxChartY = remember { mutableStateOf(0f) }
    val scaleRatio = remember { mutableStateOf(1f) }
    val yRange = remember { mutableStateOf(0f) }
    val startPadding = 36.dp.dpToPx()
    val endPadding = 35.dp.dpToPx()
    val textMeasurer = rememberTextMeasurer()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        minChartY.value = getMinAndMaxY(data).split(",")[0].toFloat()
        maxChartY.value = getMinAndMaxY(data).split(",")[1].toFloat()
        yRange.value = maxY - minY
        if(maxChartY.value != 0.0f && minChartY.value != 0.0f) {
            scaleRatio.value = (maxY - minY)/(maxChartY.value - minChartY.value)
            yRange.value = maxChartY.value - minChartY.value
        } else {
            yRange.value = maxY - minY
        }
        Canvas(
            modifier = Modifier
                .padding(bottom = 55.dp)
                .wrapContentWidth()
                .fillMaxHeight()
        ) {
            drawLine(
                color = Color.Black,
                start = Offset(startPadding, 0f),
                end = Offset(startPadding + 2, size.height)
            )
            for (y in 0..10) {
                val yPos = size.height - y * (size.height / 10)
                val yValue = minChartY.value + y * (yRange.value / 10)
                drawLine(
                    color = Color.Black,
                    start = Offset(startPadding - 10f, yPos),
                    end = Offset(startPadding, yPos)
                )

                drawText(
                    textMeasurer = textMeasurer,
                    text = yValue.toString(),
                    style = TextStyle(
                        fontSize = 8.sp,
                        color = Color(0xFF929AA5),
                        textAlign = TextAlign.Center
                    ),
                    topLeft = Offset(x = ((startPadding - 50.dp.toPx()) / 2 ), y = yPos - (8.sp.toPx()/2)),
                    maxLines = 1,
                    size = Size(
                        50.dp.toPx(), 8.sp.toPx()*2
                    )
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(start = 38.dp)
                .fillMaxSize()
                .background(Color.White)
                .horizontalScroll(scrollState)
        ) {
            val totalCandleWidth = candleWidth + (candleWidth / 2)
            val totalWidth =
                data.size * candleWidth + (data.size) * (candleWidth / 2) + endPadding
            val selectedCandleIndex = remember { mutableIntStateOf(-1) }
            val selectedCandleOffset = remember { mutableStateOf(Offset(0f, 0f)) }
            val infoWindowVisible = remember { mutableStateOf(false) }

            Canvas(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(
                        totalWidth
                            .toInt()
                            .pxToDp()
                    )
                    .padding(bottom = 55.dp)
                    .graphicsLayer(scaleX = 1.0f, scaleY = 1f/*scaleRatio.value*/)
                    .pointerInput(Unit) {
                        detectTapGestures(onLongPress = {
                            infoWindowVisible.value = true
                            selectedCandleOffset.value = it
                            debounce {
                                infoWindowVisible.value = false
                            }
                            for (i in listCandleBounds.indices) {
                                if (listCandleBounds[i].contains(it.x, it.y)) {
                                    selectedCandleIndex.intValue = i
                                    infoWindowVisible.value = true
                                    break
                                }
                            }
                        })
                    }
            ) {
                //draw x axis
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, size.height),
                    end = Offset(totalWidth, size.height),
                    strokeWidth = 2f
                )
                var x1 = 20f
                for (x in data.indices) {
                    val xPos = x1 + candleWidth / 2
                    drawLine(
                        color = Color.Black,
                        start = Offset(xPos, size.height),
                        end = Offset(xPos, size.height + 10f)
                    )

//                    val text = data[x].x
//                    val textPaint = TextPaint().apply {
//                        textSize = 10.sp.toPx()
//                        color = Color.Black.hashCode()
//                        textAlign = android.graphics.Paint.Align.CENTER
//                    }
//
//                    val textWidth = textPaint.measureText(text)
//                    val angle =
//                        45f

//                    drawContext.canvas.nativeCanvas.apply {
//                        save() // Save the canvas state before rotation
//                        rotate(angle, xPos + textWidth / 3, size.height + 10f + textWidth / 2)
//                        drawText(
//                            text,
//                            xPos + textWidth / 3,
//                            size.height + 10f + textWidth / 2,
//                            textPaint
//                        )
//                        restore()
//                    }
                    x1 += totalCandleWidth
                }
                var x = 20f
                for (dataPoint in data) {
                    val xCenter = x + candleWidth / 2
                    val yOpen = size.height - ((dataPoint.open - minChartY.value) * size.height / yRange.value)
                    val yClose = size.height - ((dataPoint.close - minChartY.value) * size.height / yRange.value)
                    val yHigh = size.height - ((dataPoint.high - minChartY.value) * size.height / yRange.value)
                    val yLow = size.height - ((dataPoint.low - minChartY.value) * size.height / yRange.value)

                    drawLine(
                        color = Color.Black,
                        start = Offset(xCenter, yHigh),
                        end = Offset(xCenter, yLow)
                    )

                    drawRect(
                        color = if (dataPoint.close >= dataPoint.open) Color.Green else Color.Red,
                        topLeft = Offset(x, min(yOpen, yClose)),
                        size = Size(candleWidth, max(yOpen, yClose) - min(yOpen, yClose))
                    )

                    val candleBounds = KMMRect(
                        x,
                        yHigh,
                        x + candleWidth,
                        yLow
                    )
                    if(listCandleBounds.size <= data.size - 1)
                        listCandleBounds.add(candleBounds)

                    x += totalCandleWidth
                }
            }
            if (selectedCandleIndex.intValue >= 0 && selectedCandleIndex.intValue < data.size && infoWindowVisible.value) {
                val candle = data[selectedCandleIndex.intValue]
                DrawInfoWindow(
                    position = selectedCandleOffset.value,
                    candleData = candle
                )

            }
        }
    }
}

private fun getVisibleCandles(listCandleBounds: List<KMMRect>, visibleRect: KMMRect, data: List<CandlestickData>): List<CandlestickData> {
    val visibleCandles = mutableListOf<CandlestickData>()

    for ((index, candleBounds) in listCandleBounds.withIndex()) {
        if (intersectsXAxis(candleBounds, visibleRect)) {
            visibleCandles.add(data[index])
        }
    }

    return visibleCandles
}

private fun intersectsXAxis(rect1: KMMRect, rect2: KMMRect): Boolean {
    return rect1.left < rect2.right && rect1.right > rect2.left
}

private fun getMinAndMaxY(data: List<CandlestickData>): String {
    var minY = 0f
    var maxY = 0f
    if(data.isNotEmpty()) {
        minY = data[0].low
        maxY = data[0].high
    }
    for (i in data) {
        if(minY > i.low) {
            minY = i.low
        }
        if(maxY < i.high) {
            maxY = i.high
        }
    }
    return "$minY,$maxY"
}

private fun getVisibleRect(scrollState: ScrollState, maxWidth: Float, chartHeight: Float): KMMRect {
    return KMMRect(
        scrollState.value.toFloat(),
        0f,
        scrollState.value + maxWidth,
        chartHeight
    )
}

@Composable
private fun DrawInfoWindow(position: Offset, candleData: CandlestickData) {
    val windowX = position.x + 20f // Adjust as needed`
    val windowY = position.y + 20f // Adjust as needed
    Box(
        modifier = Modifier.offset {
            IntOffset(
                x = windowX.toInt(),
                y = windowY.toInt()
            )
        },
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .background(Color(0xFFFAFAFA), shape = RoundedCornerShape(10.dp))

        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp)) {
                Text("Time: ${candleData.x}", color = Color.Black)
                Text("Open: ${candleData.open}", color = Color.Black)
                Text("High: ${candleData.high}", color = Color.Black)
                Text("Low: ${candleData.low}", color = Color.Black)
                Text("Close: ${candleData.close}", color = Color.Black)
            }
        }
    }
}

var displayJob: Job? = null
private fun debounce(onChange: () -> Unit) {
    displayJob?.cancel()
    displayJob = CoroutineScope(Dispatchers.Main).launch {
        delay(3500)
        onChange()
    }
}