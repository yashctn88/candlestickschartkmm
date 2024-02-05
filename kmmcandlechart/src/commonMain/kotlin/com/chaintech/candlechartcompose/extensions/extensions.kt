package com.chaintech.candlechartcompose.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.chaintech.candlechartcompose.model.KMMRect
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }


@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

@OptIn(ExperimentalObjCRefinement::class)
@HiddenFromObjC
fun KMMRect.contains(x: Float, y: Float): Boolean {
    return x >= this.left && x <= this.right && y >= this.top && y <= this.bottom
}