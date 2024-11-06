package nz.ac.uclive.nse41.cancersociety.screens

import BackButton
import CustomButton
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import nz.ac.uclive.nse41.cancersociety.CustomProgressBar
import nz.ac.uclive.nse41.cancersociety.R
import nz.ac.uclive.nse41.cancersociety.navigation.Screens
import nz.ac.uclive.nse41.cancersociety.ui.theme.Bluey
import nz.ac.uclive.nse41.cancersociety.ui.theme.CancerSocietyTheme
import nz.ac.uclive.nse41.cancersociety.utilities.getCancerInfoFromJson
import nz.ac.uclive.nse41.cancersociety.utilities.responsiveFontSize
import nz.ac.uclive.nse41.cancersociety.utilities.saveLogToFile

/**
 * The Screening support services page shows information on services available
 */
@Composable
fun ScreeningSupportServicesScreen(navController: NavController, fullSequence: Boolean, cancerType: String?) {
    val context = LocalContext.current
    val cancerInfo = getCancerInfoFromJson(context, "CancerInfo.json")
    val selectedCancer = cancerInfo?.cancers?.find { it.cancer == cancerType }

    val screeningSupportServicesSubsection = selectedCancer?.subsections?.find { it.subsection == "Screening support services" }
    Log.d("screeningSupportServicesSubsection", screeningSupportServicesSubsection.toString())

    //Times how long user spent on the screen - for internal purposes only

    var startTime by remember { mutableStateOf(System.currentTimeMillis()) }

    DisposableEffect(Unit) {
        onDispose {
            val timeSpent = System.currentTimeMillis() - startTime
            Log.d("ScreeningSupportServicesScreen", "Time spent: $timeSpent ms")
            saveLogToFile(context, "ScreeningSupportServicesScreen", timeSpent, cancerType.toString())
        }
    }

    CancerSocietyTheme(dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            contentColor = Color.Black
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                BackButton(navController)

                if (fullSequence) {
                    CustomProgressBar(
                        currentScreenIndex = 3,
                        modifier = Modifier.align(Alignment.BottomCenter).zIndex(1f)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Screening Support Services",
                        fontSize = responsiveFontSize(),
                        fontWeight = FontWeight.Bold
                    )


                    Text(
                        text = "Screening is within easy reach. We know that getting to important health checks can sometimes be hard,but there are support services available to make it easier.",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.access),
                                contentDescription = "access",
                                modifier = Modifier
                                    .size(150.dp)
                            )
                            Text(
                                text = "Access Made Easy",
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.trust),
                                contentDescription = "trust",
                                modifier = Modifier
                                    .size(150.dp)
                            )
                            Text(
                                text = "Trusted Care",
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.mindset),
                                contentDescription = "mindset",
                                modifier = Modifier
                                    .size(150.dp)
                            )
                            Text(
                                text = "Positive Mindset",
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    //Displays part 1 of screening support services info
                    screeningSupportServicesSubsection?.info?.get(0)?.let {
                        Text(
                            text = it,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    //Displays the bullet point information of the screening support services info
                    val bulletPoints = screeningSupportServicesSubsection?.info?.drop(1)?.dropLast(1) ?: listOf()
                    bulletPoints.forEach { item ->
                        Text(
                            text = "• $item",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    }

                    // Displays last part of screening support services info
                    screeningSupportServicesSubsection?.info?.lastOrNull()?.let {
                        Text(
                            text = it,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

/*
                    Spacer(modifier = Modifier.height(20.dp))
*/

                    Button(
                        onClick = {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://info.health.nz/keeping-healthy/cancer-screening/screening-support-services-in-aotearoa-new-zealand#AccordionItems-5307")
                            )
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Bluey,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Click to find your nearest screening support services!")
                    }
                }



                if (fullSequence) {

                    if (cancerType != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .align(Alignment.BottomCenter),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End

                        ) {

                            CustomButton(
                                text = "Next",
                                route = "${Screens.Quiz.route}/ScreeningSupportServices/Final",
                                navController = navController,
                                fullSequence = fullSequence,
                                cancerType = cancerType,
                                enabled = true,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                            )
                        }
                    }} else {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.BottomCenter),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End

                    ) {

                        if (cancerType != null) {
                            CustomButton(
                                text = "Next",
                                route = Screens.Final.route,
                                navController = navController,
                                fullSequence = fullSequence,
                                cancerType = cancerType,
                                enabled = true,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                            )
                        }
                    }


                }






            }
        }
    }
}
