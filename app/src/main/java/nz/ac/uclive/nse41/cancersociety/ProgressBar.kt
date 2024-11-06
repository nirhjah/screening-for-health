package nz.ac.uclive.nse41.cancersociety
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nz.ac.uclive.nse41.cancersociety.ui.theme.Purple40
import nz.ac.uclive.nse41.cancersociety.ui.theme.Purple80

/**
 * Progress bar adapated from tutorial https://www.youtube.com/watch?v=SrSk8jErjPU
 * This progress bar is used in all of the screens if the user goes through the full sequence, to show their progress.
 */
@Composable
fun CustomProgressBar(
    currentScreenIndex: Int,
    modifier: Modifier = Modifier
) {
    var progress by remember { mutableStateOf(0f) }

    val targetProgress = when (currentScreenIndex) {
        0 -> 0.0f
        1 -> 0.25f
        2 -> 0.50f
        3 -> 0.75f
        4 -> 1.0f
        else -> 0.0f
    }

    LaunchedEffect(key1 = currentScreenIndex) {
        progress = targetProgress
    }

    val size by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearOutSlowInEasing
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth(0.7f) // adjust for size of progress bar
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Progress",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 1.dp)
                .align(Alignment.Start)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(47.dp)
        ) {
            Row(
                modifier = Modifier
                    .widthIn(min = 30.dp)
                    .fillMaxWidth(size),
                horizontalArrangement = Arrangement.End
            ) {
                Text(text = "${(progress * 100).toInt()}%")
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(17.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(9.dp))
                        .background(Purple80)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(size)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(9.dp))
                        .background(Purple40)
                        .animateContentSize()
                )
            }
        }
    }
}

