package com.xeniac.jalalidatepicker.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationResult
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

private fun <T> getItemIndexForOffset(
    selectedValue: T,
    range: List<T>,
    offset: Float,
    halfNumbersColumnHeightPx: Float
): Int {
    val indexOf = range.indexOf(selectedValue) - (offset / halfNumbersColumnHeightPx).toInt()
    return maxOf(
        a = 0,
        b = minOf(
            a = indexOf,
            b = range.count() - 1
        )
    )
}

@Composable
internal fun <T> ItemPickerWheel(
    selectedValue: T,
    range: List<T>,
    dividersHeight: Dp,
    dividersColor: Color,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    label: (T) -> String = { it.toString() },
    onSelectedValueChange: (T) -> Unit
) {
    val scope = rememberCoroutineScope()

    val minimumAlpha = 0.3f
    val verticalMargin = 8.dp
    val numbersColumnHeight = 80.dp
    val halfNumbersColumnHeight = numbersColumnHeight / 2
    val halfNumbersColumnHeightPx = with(LocalDensity.current) { halfNumbersColumnHeight.toPx() }

    val animatedOffset = remember {
        Animatable(initialValue = 0f)
    }.apply {
        val index = range.indexOf(selectedValue)

        val offsetRange = remember(selectedValue, range) {
            -((range.count() - 1) - index) * halfNumbersColumnHeightPx to
                    index * halfNumbersColumnHeightPx
        }

        updateBounds(
            lowerBound = offsetRange.first,
            upperBound = offsetRange.second
        )
    }

    val coercedAnimatedOffset = animatedOffset.value % halfNumbersColumnHeightPx

    val indexOfElement = getItemIndexForOffset(
        selectedValue = selectedValue,
        range = range,
        offset = animatedOffset.value,
        halfNumbersColumnHeightPx = halfNumbersColumnHeightPx
    )

    var dividersWidth by remember { mutableStateOf(0.dp) }

    Layout(
        modifier = modifier
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { deltaY ->
                    scope.launch {
                        animatedOffset.snapTo(targetValue = animatedOffset.value + deltaY)
                    }
                },
                onDragStopped = { velocity ->
                    scope.launch {
                        val endValue = animatedOffset.fling(
                            initialVelocity = velocity,
                            animationSpec = exponentialDecay(frictionMultiplier = 20f),
                            adjustTarget = { target ->
                                val coercedTarget = target % halfNumbersColumnHeightPx
                                val coercedAnchors = listOf(
                                    -halfNumbersColumnHeightPx,
                                    0f,
                                    halfNumbersColumnHeightPx
                                )

                                val coercedPoint = coercedAnchors.minBy {
                                    abs(x = it - coercedTarget)
                                }
                                val base = halfNumbersColumnHeightPx *
                                        (target / halfNumbersColumnHeightPx).toInt()

                                coercedPoint + base
                            }
                        ).endState.value

                        val result = range.elementAt(
                            getItemIndexForOffset(
                                selectedValue = selectedValue,
                                range = range,
                                offset = endValue,
                                halfNumbersColumnHeightPx = halfNumbersColumnHeightPx
                            )
                        )

                        onSelectedValueChange(result)
                        animatedOffset.snapTo(0f)
                    }
                }
            )
            .padding(vertical = numbersColumnHeight / 3 + verticalMargin * 2),
        content = {
            Divider(
                width = dividersWidth,
                height = dividersHeight,
                color = dividersColor,
                modifier = modifier
            )

            Box(
                modifier = Modifier
                    .padding(
                        vertical = verticalMargin,
                        horizontal = 20.dp
                    )
                    .offset {
                        IntOffset(
                            x = 0,
                            y = coercedAnimatedOffset.roundToInt()
                        )
                    }
            ) {
                val baseLabelModifier = Modifier.align(Alignment.Center)

                ProvideTextStyle(value = textStyle) {
                    if (indexOfElement > 0) {
                        Label(
                            text = label(range.elementAt(indexOfElement - 1)),
                            modifier = baseLabelModifier
                                .offset(y = -halfNumbersColumnHeight)
                                .alpha(
                                    alpha = maxOf(
                                        a = minimumAlpha,
                                        b = coercedAnimatedOffset / halfNumbersColumnHeightPx
                                    )
                                )
                        )
                    }

                    Label(
                        text = label(range.elementAt(indexOfElement)),
                        modifier = baseLabelModifier.alpha(
                            alpha = maxOf(
                                a = minimumAlpha,
                                b = 1 - abs(coercedAnimatedOffset) / halfNumbersColumnHeightPx
                            )
                        )
                    )

                    if (indexOfElement < range.count() - 1) {
                        Label(
                            text = label(range.elementAt(indexOfElement + 1)),
                            modifier = baseLabelModifier
                                .offset(y = halfNumbersColumnHeight)
                                .alpha(
                                    alpha = maxOf(
                                        a = minimumAlpha,
                                        b = -coercedAnimatedOffset / halfNumbersColumnHeightPx
                                    )
                                )
                        )
                    }
                }
            }

            Divider(
                width = dividersWidth,
                height = dividersHeight,
                color = dividersColor,
                modifier = modifier
            )
        }
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints

        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each children
            measurable.measure(constraints = constraints)
        }

        dividersWidth = placeables
            .drop(n = 1)
            .first()
            .width
            .toDp()

        // Set the size of the layout as big as it can
        layout(
            width = dividersWidth.toPx().toInt(),
            height = placeables.sumOf { placeable ->
                placeable.height
            }
        ) {
            // Track the y co-ord we have placed children up to
            var yPosition = 0

            // Place children in the parent layout
            placeables.forEach { placeable ->
                // Position item on the screen
                placeable.placeRelative(
                    x = 0,
                    y = yPosition
                )

                // Record the y co-ord placed up to
                yPosition += placeable.height
            }
        }
    }
}

@Composable
private fun Divider(
    width: Dp,
    height: Dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .background(color)
    )
}

@Composable
private fun Label(
    text: String,
    modifier: Modifier
) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

private suspend fun Animatable<Float, AnimationVector1D>.fling(
    initialVelocity: Float,
    animationSpec: DecayAnimationSpec<Float>,
    adjustTarget: ((Float) -> Float)?,
    block: (Animatable<Float, AnimationVector1D>.() -> Unit)? = null
): AnimationResult<Float, AnimationVector1D> {
    val targetValue = animationSpec.calculateTargetValue(
        initialValue = value,
        initialVelocity = initialVelocity
    )
    val adjustedTarget = adjustTarget?.invoke(targetValue)

    return if (adjustedTarget != null) {
        animateTo(
            targetValue = adjustedTarget,
            initialVelocity = initialVelocity,
            block = block
        )
    } else {
        animateDecay(
            initialVelocity = initialVelocity,
            animationSpec = animationSpec,
            block = block
        )
    }
}