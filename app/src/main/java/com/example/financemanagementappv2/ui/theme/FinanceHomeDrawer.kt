package com.example.financemanagementappv2.ui.theme

import android.os.SystemClock
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.financemanagementappv2.data.enums.DrawerState
import com.example.financemanagementappv2.data.enums.Screen
import com.example.financemanagementappv2.ui.theme.screens.FinanceExpenseScreen
import com.example.financemanagementappv2.ui.theme.screens.FinanceHomeScreen
import com.example.financemanagementappv2.ui.theme.screens.FinanceIncomeScreen
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

// dp - density independent pixels
private val DrawerWidth = 200.dp

@Composable
fun FinanceHomeDrawer(windowSizeClass: WindowSizeClass) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        var drawerState by remember { mutableStateOf(DrawerState.Closed) }
        var screenState by remember { mutableStateOf(Screen.Home) }

        // not delegateable so no by keyword is used
        // translationX represents how far the UI element has moved on the X axis
        val translationX = remember { Animatable(0f) }
        val currentVelocity = remember { VelocityTracker() }

        // To convert from density independent to real pixels, you need the density context
        val drawerWidth = with(LocalDensity.current) { DrawerWidth.toPx() }

        // coroutine scope that is tied to the lifecycle of the Composable
        val coroutineScope = rememberCoroutineScope()

        // onDelta gets called every time the user drags
        // snapTo is a suspend function so coroutine is launched
        val draggableState = rememberDraggableState(onDelta = { dragAmount ->
            coroutineScope.launch {
                translationX.snapTo(translationX.value + dragAmount)
            }
        })

        // Used when you want to simulate natural motion, like flinging something and letting it gradually come to rest
        // predefined way of calculating how a value slows down over time
        val decay = rememberSplineBasedDecay<Float>()

        val velocityTracker = remember { VelocityTracker() }

        // This sets the minimum and maximum bounds for an animation or gesture
        translationX.updateBounds(0f, drawerWidth)

        suspend fun closeDrawer(velocity: Float = 0f) {
            translationX.animateTo(targetValue = 0f, initialVelocity = velocity)
            drawerState = DrawerState.Closed
        }

        suspend fun openDrawer(velocity: Float = 0f) {
            translationX.animateTo(targetValue = drawerWidth, initialVelocity = velocity)
            drawerState = DrawerState.Open
        }

        fun toggleDrawerState() {
            coroutineScope.launch {
                if (DrawerState.Closed == drawerState) {
                    openDrawer()
                } else if (DrawerState.Open == drawerState) {
                    closeDrawer()
                }
            }
        }

        PredictiveBackHandler(drawerState == DrawerState.Open) { progress ->
            try {
                progress.collect { backEvent ->
                    val targetSize = (drawerWidth - (drawerWidth * backEvent.progress))
                    translationX.snapTo(targetSize)
                    velocityTracker.addPosition(
                        SystemClock.uptimeMillis(),
                        Offset(backEvent.touchX, backEvent.touchY)
                    )
                }
                closeDrawer(velocityTracker.calculateVelocity().x)
            } catch (e: CancellationException) {
                openDrawer(velocityTracker.calculateVelocity().x)
            }
            velocityTracker.resetTracking()
        }


        FinanceHomeDrawerContents(
            selectedScreen = screenState,
            onScreenSelected = { screen ->
                screenState = screen
            }
        )

        ScreenContents(
            windowWidthSizeClass = windowSizeClass.widthSizeClass,
            selectedScreen = screenState,
            onDrawerClicked = ::toggleDrawerState,
            modifier = Modifier
                .graphicsLayer {
                    this.translationX = translationX.value
                    val scale = lerp(1f, 0.8f, translationX.value / drawerWidth)
                    this.scaleX = scale
                    this.scaleY = scale
                    val roundedCorners = lerp(0f, 32.dp.toPx(), translationX.value / drawerWidth)
                    this.shape = RoundedCornerShape(roundedCorners)
                    this.clip = true
                    this.shadowElevation = 32f
                }
                // This example is showing how to use draggable with custom logic on stop to snap to the edges
                // You can also use `anchoredDraggable()` to set up anchors and not need to worry about more calculations.
                .draggable(
                    draggableState, Orientation.Horizontal,
                    onDragStopped = { velocity ->
                        val targetOffsetX = decay.calculateTargetValue(
                            translationX.value,
                            velocity
                        )
                        coroutineScope.launch {
                            val actualTargetX = if (targetOffsetX > drawerWidth * 0.5) {
                                drawerWidth
                            } else {
                                0f
                            }
                            // checking if the difference between the target and actual is + or -
                            val targetDifference = (actualTargetX - targetOffsetX)
                            val canReachTargetWithDecay =
                                (targetOffsetX > actualTargetX && velocity > 0f && targetDifference > 0f) ||
                                (targetOffsetX < actualTargetX && velocity < 0 && targetDifference < 0f)
                            if (canReachTargetWithDecay) {
                                translationX.animateDecay(
                                    initialVelocity = velocity,
                                    animationSpec = decay
                                )
                            } else {
                                translationX.animateTo(actualTargetX, initialVelocity = velocity)
                            }
                            drawerState = if (actualTargetX == drawerWidth) {
                                DrawerState.Open
                            } else {
                                DrawerState.Closed
                            }
                        }
                    }
                )
        )
    }
}


@Composable
fun ScreenContents(
    windowWidthSizeClass: WindowWidthSizeClass,
    selectedScreen: Screen,
    onDrawerClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier.fillMaxSize()
    ) {
        // when is like a switch case, but more powerful
        when (selectedScreen) {
            Screen.Home ->
                FinanceHomeScreen(
                    windowSizeClass = windowWidthSizeClass,
                    modifier = Modifier,
                    onDrawerClicked = onDrawerClicked
                )
            Screen.Income ->
                FinanceIncomeScreen(
                    windowSizeClass = windowWidthSizeClass,
                    modifier = Modifier,
                    onDrawerClicked = onDrawerClicked
                )
            Screen.Expense ->
                FinanceExpenseScreen(
                    windowSizeClass = windowWidthSizeClass,
                    modifier = Modifier,
                    onDrawerClicked = onDrawerClicked
                )
            Screen.Settings ->
                Surface (
                    modifier = Modifier.fillMaxSize()
                ) {
                }
        }
    }
}


@Composable
fun FinanceHomeDrawerContents(
    selectedScreen: Screen,
    // onScreenSelected here serves a purpose of something like function pointer, to be called on a certain event happening
    // It is provided by the Parent Composable and it is called from the current Child Composable
    // this is a lambda so (Screen) is tha parameter and Unit is the return value (Unit = Void in Kotlin)
    onScreenSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Screen.entries.forEach {
            NavigationDrawerItem(
                label = { Text(it.text) },
                selected = selectedScreen == it,
                onClick = { onScreenSelected(it) },
                icon = { Icon(imageVector = it.icon, contentDescription = it.text) },
            )
        }
    }
}