package nz.ac.uclive.nse41.cancersociety.screens

import HomepageBackButton
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import nz.ac.uclive.nse41.cancersociety.R
import nz.ac.uclive.nse41.cancersociety.navigation.Screens
import nz.ac.uclive.nse41.cancersociety.ui.theme.Bluey
import nz.ac.uclive.nse41.cancersociety.ui.theme.CancerSocietyTheme
import nz.ac.uclive.nse41.cancersociety.ui.theme.Green
import nz.ac.uclive.nse41.cancersociety.ui.theme.Orange
import nz.ac.uclive.nse41.cancersociety.ui.theme.Pink
import nz.ac.uclive.nse41.cancersociety.utilities.getQuizQuestionsForCancerType
import nz.ac.uclive.nse41.cancersociety.utilities.saveLogToFile

/**
 * This code is for the Cancer Homepage screen which allows the user to either test their knowledge on X cancer type with a quiz,
 * or explore the 4 different subtopics available.
 */
@Composable
fun CancerHomepageScreen(navController: NavController, cancerType: String) {
    Log.d("MyActivity", cancerType)
    val containerColor = getCancerTypeColor(cancerType)
    val context = LocalContext.current
    var fullSequence = false


   // A start was made on making this application mobile friendly for different sized devices, this code uses the screenwidth to determine
    // the size and positioning of different elements.
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val fontSize = when {
        screenWidthDp < 412 -> 54.sp
        else -> 84.sp
    }
    val start = when {
        screenWidthDp < 412 -> 10.dp
        else -> 66.dp
    }
    val topStart = when {
        screenWidthDp < 412 -> 70.dp
        else -> 100.dp
    }
    val topEnd = when {
        screenWidthDp < 412 -> 70.dp
        else -> 100.dp
    }
    val topPadding = when {
        screenWidthDp < 412 -> 26.dp
        else -> 0.dp
    }

    var showDialog by remember { mutableStateOf(false) }



    var startTime by remember { mutableStateOf(System.currentTimeMillis()) }

    //Records time user spent on this screen - for analysis use only
    DisposableEffect(Unit) {
        onDispose {
            val timeSpent = System.currentTimeMillis() - startTime
            Log.d("CancerHomepage", "Time spent: $timeSpent ms")

            saveLogToFile(context, "CancerHomepage", timeSpent, cancerType.toString())
        }
    }


    CancerSocietyTheme(dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        HomepageBackButton(navController)

                        //Help Button
                        Button(
                            onClick = { showDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Bluey, contentColor = Color.Black),
                            modifier = Modifier
                                .height(60.dp)
                                .width(150.dp)
                                .background(Bluey, shape = MaterialTheme.shapes.large)
                        ) {
                            Text("Help", fontSize = 35.sp)
                        }
                    }



                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Help", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold) },
                            text = { Text("Test your knowledge of $cancerType in NZ: Click this to be quizzed on 5 questions relating to content in this app about $cancerType!\n" +
                                    "\nWhat is screening?: Learn what screening is in simple terms.\n" +
                                    "\nWho can get screened?: Learn the ages of who can get screened for " + cancerType + ".\n" +
                                    "\nWhere to get screened?: Learn where you can get screened for " + cancerType + "!\n" +
                                    "\nScreening support services: Learn what support there is available!\n" +
                                    "\nOr learn them all!: Learn all 4 topics in one go, with quiz questions throughout the process to test your knowledge." ) },
                            confirmButton = {
                                Button(onClick = { showDialog = false }) {
                                    Text("Close", color = Color.Black)
                                }
                            }
                        )
                    }



                    Row(
                        modifier = Modifier
                            .padding(start = start, top = 0.dp)
                            .align(Alignment.Start)
                    ) {
                        Text(
                            text = cancerType,
                            fontSize = fontSize,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Left,
                            color = Color.Black,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .semantics { testTag = "cancerHomepageTitle" }
                        )


                        Image(
                            painter = painterResource(id = R.drawable.women1),
                            contentDescription = null,
                            modifier = Modifier
                                .size(168.dp)
                                .align(Alignment.CenterVertically)
                        )


                        val imageRes2 = if (cancerType == "Bowel Cancer") {
                            R.drawable.men2
                        } else {
                            R.drawable.women2
                        }


                        Image(
                            painter = painterResource(id = imageRes2),
                            contentDescription = null,
                            modifier = Modifier
                                .size(168.dp)
                                .align(Alignment.CenterVertically)
                        )


                        val imageRes3 = if (cancerType == "Bowel Cancer") {
                            R.drawable.men3
                        } else {
                            R.drawable.women3
                        }

                        Image(
                            painter = painterResource(id = imageRes3),
                            contentDescription = null,
                            modifier = Modifier
                                .size(168.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.95f)
                            .background(
                                Color.White,
                                shape = RoundedCornerShape(topStart = topStart, topEnd = topEnd) //100, 100
                            )
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth().padding(top = topPadding)

                        ) {
                            //This button/card is for testing your knowledge on X cancer in NZ with a quiz
                            ElevatedCard(
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = Bluey,
                                    contentColor = Color.Black
                                ),
                                modifier = Modifier
                                    .size(width = 680.dp, height = 80.dp)
                                    .clickable {
                                        val randomQuestions = getQuizQuestionsForCancerType(context, "Quiz.json", cancerType)
                                        val questionsJson = Gson().toJson(randomQuestions)
                                        navController.navigate("quizTYK/$questionsJson/0/0")
                                    }
                            ) {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.quiz_icon2),
                                        contentDescription = "Quiz Icon",
                                        tint = Color.Black,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Test your knowledge of $cancerType in NZ",
                                        fontSize = 26.sp,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }



                            Spacer(modifier = Modifier.height(20.dp))

                            Divider(thickness = 1.dp, color = Color.LightGray)
                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "Learn more about...",
                                fontSize = 26.sp,
                                modifier = Modifier
                                    .padding(start = 66.dp)
                                    .align(Alignment.Start),
                                textAlign = TextAlign.Left,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            //The row of buttons (cards) that take you to each sub topic
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.horizontalScroll(rememberScrollState())
                            ) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = containerColor,
                                        contentColor = Color.Black),
                                    modifier = Modifier
                                        .size(width = 200.dp, height = 200.dp)
                                        .clickable { navController.navigate("${Screens.WhatIsScreening.route}/$fullSequence/$cancerType") }


                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.quiz_icon),
                                                contentDescription = "What Icon",
                                                tint = Color.Black,
                                                modifier = Modifier.size(48.dp)
                                            )

                                            Text(
                                                text = "What is screening?",
                                                fontWeight = FontWeight.ExtraBold,
                                                modifier = Modifier.padding(16.dp),
                                                textAlign = TextAlign.Center,
                                            )

                                        }
                                    }
                                }

                                Card(
                                    colors = CardDefaults.cardColors(containerColor = containerColor,
                                        contentColor = Color.Black),
                                    modifier = Modifier
                                        .size(width = 200.dp, height = 200.dp)
                                        .clickable { navController.navigate("${Screens.WhoCanGetScreened.route}/$fullSequence/$cancerType") }

                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.who_icon),
                                                contentDescription = "Who Icon",
                                                tint = Color.Black,
                                                modifier = Modifier.size(48.dp)
                                            )

                                            Text(
                                                text = "Who can get screened?",
                                                fontWeight = FontWeight.ExtraBold,
                                                modifier = Modifier.padding(16.dp),
                                                textAlign = TextAlign.Center,
                                            )

                                        }
                                    }
                                }

                                Card(
                                    colors = CardDefaults.cardColors(containerColor = containerColor,
                                        contentColor = Color.Black),
                                    modifier = Modifier
                                        .size(width = 200.dp, height = 200.dp)
                                        .clickable { navController.navigate("${Screens.WhereToGetScreened.route}/$fullSequence/$cancerType") }

                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.where_icon),
                                                contentDescription = "Where Icon",
                                                tint = Color.Black,
                                                modifier = Modifier.size(48.dp)
                                            )

                                            Text(
                                                text = "Where to get screened?",
                                                fontWeight = FontWeight.ExtraBold,
                                                modifier = Modifier.padding(16.dp),
                                                textAlign = TextAlign.Center,
                                            )

                                        }
                                    }
                                }

                                Card(
                                    colors = CardDefaults.cardColors(containerColor = containerColor,
                                        contentColor = Color.Black),
                                    modifier = Modifier
                                        .size(width = 200.dp, height = 200.dp)
                                        .clickable { navController.navigate("${Screens.ScreeningSupportServices.route}/$fullSequence/$cancerType") }

                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.suppservices_icon),
                                                contentDescription = "Barrier Icon",
                                                tint = Color.Black,
                                                modifier = Modifier.size(48.dp)
                                            )

                                            Text(
                                                text = "Screening support services",
                                                fontWeight = FontWeight.ExtraBold,
                                                modifier = Modifier.padding(16.dp),
                                                textAlign = TextAlign.Center,
                                            )

                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            //This card/button is for the 'learn them all' button taking the user through all 4 subtopics in one go
                            //with some quiz questions in between
                            ElevatedCard(
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = containerColor,
                                    contentColor = Color.Black
                                ),
                                modifier = Modifier
                                    .size(width = 310.dp, height = 80.dp)
                                    .clickable {
                                        fullSequence = true
                                        navController.navigate("${Screens.WhatIsScreening.route}/$fullSequence/$cancerType")


                                    }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.quiz_icon),
                                        contentDescription = "Quiz Icon",
                                        tint = Color.Black,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Or learn them all!",
                                        fontSize = 26.sp,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Divider(thickness = 1.dp, color = Color.LightGray)
                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "Useful links",
                                fontSize = 26.sp,
                                modifier = Modifier
                                    .padding(start = 66.dp)
                                    .align(Alignment.Start),
                                textAlign = TextAlign.Left,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(20.dp))


                            val link2 = when (cancerType) {
                                "Bowel Cancer" -> "https://bowelcancernz.org.nz/about-bowel-cancer/what-is-bowel-cancer/symptoms-statistics/"
                                "Breast Cancer" -> "https://www.breastcancerfoundation.org.nz/breast-awareness/mammograms"
                                else -> "https://www.tewhatuora.govt.nz/health-services-and-programmes/ncsp-hpv-screening/"
                            }

                            val text2 = when (cancerType) {
                                "Bowel Cancer" -> "Bowel Cancer NZ"
                                "Breast Cancer" -> "Breast Cancer Foundation"
                                else -> "Te Whatu Ora"
                            }


                            //Clicking the buttons below take you to the webpages above
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link2))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                colors = ButtonDefaults.buttonColors(containerColor = Bluey, contentColor = Color.Black),

                                ) {
                                Text(text2)
                            }

                            val link3 = when (cancerType) {
                                "Bowel Cancer" -> "https://www.cancer.org.nz/cancer/types-of-cancer/bowel-cancer/"
                                "Breast Cancer" -> "https://www.cancer.org.nz/cancer/types-of-cancer/breast-cancer/"
                                else -> "https://www.cancer.org.nz/cancer/types-of-cancer/cervical-cancer/"
                            }

                            val text3 = when (cancerType) {
                                "Bowel Cancer" -> "Cancer Society"
                                "Breast Cancer" -> "Cancer Society"
                                else -> "Cancer Society"
                            }


                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link3))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                colors = ButtonDefaults.buttonColors(containerColor = Bluey, contentColor = Color.Black),

                                ) {
                                Text(text3)
                            }



                        }

                    }

                }
            }
        }
    }
}

/**
 * This function gets the colour by cancer type, the colour is used to colour the buttons/cards on this page for each cancer type
 */
fun getCancerTypeColor(cancerType: String): Color {
    return when (cancerType) {
        "Breast Cancer" -> Pink
        "Bowel Cancer" -> Green
        "Cervical Cancer" -> Orange
        else -> Bluey // if for some reason doesn't exist
    }
}