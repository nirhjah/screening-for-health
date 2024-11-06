package nz.ac.uclive.nse41.cancersociety.screens

import BackButton
import CustomButton
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import nz.ac.uclive.nse41.cancersociety.CustomProgressBar
import nz.ac.uclive.nse41.cancersociety.R
import nz.ac.uclive.nse41.cancersociety.navigation.Screens
import nz.ac.uclive.nse41.cancersociety.ui.theme.Bluey
import nz.ac.uclive.nse41.cancersociety.ui.theme.CancerSocietyTheme
import nz.ac.uclive.nse41.cancersociety.utilities.getCancerInfoFromJson
import nz.ac.uclive.nse41.cancersociety.utilities.responsiveFontSize
import nz.ac.uclive.nse41.cancersociety.utilities.responsiveHospitalImage
import nz.ac.uclive.nse41.cancersociety.utilities.saveLogToFile

/**
 * The where to get screened screen is slightly different for all 3 cancer types - it will show some information on where to get screened and show some
 * external links
 */
@Composable
fun WhereToGetScreenedScreen(navController: NavController, fullSequence: Boolean, cancerType: String?) {
    var isVisible by remember { mutableStateOf(false) }
    var isButtonEnabled by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val cancerInfo = getCancerInfoFromJson(context, "CancerInfo.json")
    val selectedCancer = cancerInfo?.cancers?.find { it.cancer == cancerType }
    val whereToGetScreenedSubSection = selectedCancer?.subsections?.find { it.subsection == "Where to get screened" }

    val targetOffsetX: Dp = 200.dp
    val initialOffsetX: Dp = (-300).dp
    val offsetX = remember { Animatable(initialOffsetX.value) }
    var showCard by remember { mutableStateOf(false) }

    val showOrderTestKitModal = remember { mutableStateOf(false) }

    //Times how long user spent on the screen - for internal purposes only
    var startTime by remember { mutableStateOf(System.currentTimeMillis()) }

    DisposableEffect(Unit) {
        onDispose {
            val timeSpent = System.currentTimeMillis() - startTime
            Log.d("WhereToGetScreenedScreen", "Time spent: $timeSpent ms")

            saveLogToFile(context, "WhereToGetScreenedScreen", timeSpent, cancerType.toString())
        }
    }


    LaunchedEffect(Unit) {
        delay(100)
        offsetX.animateTo(
            targetValue = targetOffsetX.value,
            animationSpec = tween(durationMillis = 3000)
        )
        isVisible = true
        showCard = true
        delay(500)

        isButtonEnabled = true
    }

    CancerSocietyTheme(dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            contentColor = Color(red = 0, green = 0, blue = 0)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (fullSequence) {
                    CustomProgressBar(
                        currentScreenIndex = 2,
                        modifier = Modifier.align(Alignment.TopCenter).zIndex(1f)
                    )                             }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(50.dp),
                    verticalArrangement = Arrangement.spacedBy(50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Where to get screened",
                        fontSize = responsiveFontSize(),
                        fontWeight = FontWeight.Bold
                    )

                }

                Spacer(modifier = Modifier.height(400.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .height(120.dp)
                ) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(0.dp)

                    ) {
                        items(5) {
                            Image(
                                painter = painterResource(id = R.drawable.road),
                                contentDescription = "road",
                                modifier = Modifier.size(290.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                //shows a man if it is on the bowel cancer screen, woman if on the other cancer types
                val imageRes1 = if (cancerType == "Bowel Cancer") {
                    R.drawable.men3
                } else {
                    R.drawable.women3
                }

                //Shows the mail box image if on the bowel cancer screen - to show that you can post your bowel testing kit. And can tap on it to find out more information
                if (cancerType == "Bowel Cancer") {
                    Image(
                        painter = painterResource(id = R.drawable.mailbox),
                        contentDescription = "mailbox",
                        modifier = Modifier
                            .size(180.dp)
                            .align(Alignment.Center)
                            .offset(x = (-450).dp, y = 160.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(whereToGetScreenedSubSection?.info?.get(4)))
                                context.startActivity(intent)
                            }
                    )
                }

                //Shows a doctor image (instead of mailbox) for cervical cancer, clicking on it takes you to more information on how to self-test
                if (cancerType == "Cervical Cancer") {
                    Image(
                        painter = painterResource(id = R.drawable.doctor),
                        contentDescription = "doctor",
                        modifier = Modifier
                            .size(470.dp)
                            .align(Alignment.Center)
                            .offset(x = (-450).dp, y = 170.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(whereToGetScreenedSubSection?.info?.get(1)))
                                context.startActivity(intent)
                            }
                    )
                }

                //Shows a van/bus (instead of the above) for breast cancer, clicking on it shows more info for booking a mammogram
                if (cancerType == "Breast Cancer") {
                    Image(
                        painter = painterResource(id = R.drawable.van),
                        contentDescription = "van",
                        modifier = Modifier
                            .size(310.dp)
                            .align(Alignment.Center)
                            .offset(x = (-460).dp, y = 130.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(whereToGetScreenedSubSection?.info?.get(2)))
                                context.startActivity(intent)
                            }
                    )
                }



                Image(
                    painter = painterResource(id = imageRes1),
                    contentDescription = "woman1",
                    modifier = Modifier
                        .size(350.dp)
                        .offset(x = Dp(offsetX.value), y = 280.dp)
                )

                //If the cancer type is bowel cancer, shows a house, otherwise it will show a hospital image in the center
                if (cancerType == "Bowel Cancer") {

                    Image(
                        painter = painterResource(id = R.drawable.house),
                        contentDescription = "house",
                        modifier = Modifier
                            .size(responsiveHospitalImage())
                            .align(Alignment.Center)
                            .offset(x = 0.dp, y = 90.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.hospital),
                        contentDescription = "hospital",
                        modifier = Modifier
                            .size(responsiveHospitalImage())
                            .align(Alignment.Center)
                            .offset(x = 0.dp, y = 70.dp)
                    )
                }

                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn() + slideInHorizontally(
                        animationSpec = tween(durationMillis = 2000),
                        initialOffsetX = { 700 }
                    ),

                    modifier = Modifier.offset(900.dp, 260.dp)

                ) {
                    //This card appears on the right hand side of the screen showing information on where to get screened for x cancer
                    Card(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(start = 6.dp)

                            .size(300.dp, 300.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            whereToGetScreenedSubSection?.info?.get(0)?.let {
                                Text(
                                    text = it,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = {


                                    if (cancerType == "Bowel Cancer") {
                                        showOrderTestKitModal.value = true


                                    } else {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(whereToGetScreenedSubSection?.info?.get(1))
                                        )
                                        context.startActivity(intent)
                                    }


                                },
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Bluey,
                                    contentColor = Black
                                ),

                                ) {
                                if (cancerType == "Bowel Cancer") {
                                    Text("Click to order a test kit!")

                                } else {
                                    Text("Click to find your closest clinic!")
                                }
                            }
                        }
                    }




                }


                //animations for the mailbox images for bowel cancer
                if (cancerType == "Bowel Cancer") {


                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn() + slideInHorizontally(
                        animationSpec = tween(durationMillis = 2500),
                        initialOffsetX = { 0 }
                    ),

                    modifier = Modifier.offset(105.dp, 250.dp)

                ) {
                    Card(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(start = 6.dp)
                            .size(150.dp, 150.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Tap the mailbox to find out how to do the test!",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                }


            }

                //Animations for the doctor image for cervical cancer
                if (cancerType == "Cervical Cancer") {


                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn() + slideInHorizontally(
                            animationSpec = tween(durationMillis = 2500),
                            initialOffsetX = { 0 }
                        ),

                        modifier = Modifier.offset(105.dp, 250.dp)

                    ) {
                        Card(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(start = 6.dp)
                                .size(210.dp, 105.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Tap the doctor to find out how to do a self-test!",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                    }


                }


                //animation for the bus/van image for breast cancer
                if (cancerType == "Breast Cancer") {


                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn() + slideInHorizontally(
                            animationSpec = tween(durationMillis = 2500),
                            initialOffsetX = { 0 }
                        ),

                        modifier = Modifier.offset(75.dp, 190.dp)

                    ) {
                        Card(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(start = 6.dp)
                                .size(210.dp, 145.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Tap the van book your free mammogram if you're 45-69!",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                    }


                }

                //Opens the modal if you click order test kit button (for bowel cancer)
                if (showOrderTestKitModal.value) {
                    val infoList = whereToGetScreenedSubSection?.info
                    val bulletPoints = infoList?.drop(1)?.dropLast(1) ?: listOf()
                    //the modal displayed when clicking the order test kit button
                    AlertDialog(
                        onDismissRequest = { showOrderTestKitModal.value = false },
                        title = { Text("Order a test kit through...") },
                        text = {
                            Column {
                                bulletPoints.forEachIndexed { index, bulletPoint ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = when (index) {
                                                0 -> Icons.Default.Phone
                                                1 -> Icons.Default.Email
                                                2 -> Icons.Default.Person
                                                else -> Icons.Default.ArrowBack
                                            },
                                            contentDescription = null,
                                            modifier = Modifier.size(30.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(bulletPoint)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        },
                        confirmButton = {
                            Button(onClick = { showOrderTestKitModal.value = false }) {
                                Text("Close")
                            }
                        }
                    )

                }

                    Box(modifier = Modifier.fillMaxSize()) {
                        if (fullSequence) {

                            if (cancerType != null) {
                            CustomButton(
                                text = "Next",
                                route = "${Screens.Quiz.route}/WhereToGetScreened/ScreeningSupportServices",
                                navController = navController,
                                fullSequence = fullSequence,
                                cancerType = cancerType,
                                enabled = isButtonEnabled,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(16.dp)
                            )
                        } }
                    }
                    BackButton(navController)

            }
        }
    }
}
