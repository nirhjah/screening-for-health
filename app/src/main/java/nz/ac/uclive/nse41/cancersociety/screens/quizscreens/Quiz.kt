package nz.ac.uclive.nse41.cancersociety.screens.quizscreens

import QuizCheckButton
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
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
import androidx.navigation.NavController
import nz.ac.uclive.nse41.cancersociety.R
import nz.ac.uclive.nse41.cancersociety.navigation.Screens
import nz.ac.uclive.nse41.cancersociety.screens.getCancerTypeColor
import nz.ac.uclive.nse41.cancersociety.ui.theme.CancerSocietyTheme
import nz.ac.uclive.nse41.cancersociety.utilities.getQuizQuestionForCancerTypeAndSubsection
import nz.ac.uclive.nse41.cancersociety.utilities.responsiveFontSize
import nz.ac.uclive.nse41.cancersociety.utilities.saveLogToFile

/**
 * The quiz screen that is shown throughout the learn them all/full sequence part of the app
 * Shows 4 options for answers for the user to select from, question is based on content from the slide before
 */
@Composable
fun QuizScreen(
    navController: NavController,
    currentScreen: String?,
    nextScreen: String?,
    fullSequence: Boolean,
    cancerType: String?
) {
    val context = LocalContext.current

    val quizQuestion = remember(currentScreen, cancerType) {
        getQuizQuestionForCancerTypeAndSubsection(context, "Quiz.json", cancerType.toString(), currentScreen.toString())
    }

    val selectedAnswer = remember { mutableStateOf<String?>(null) }


    //Times how long user spent on the screen - for internal purposes only
    var startTime by remember { mutableStateOf(System.currentTimeMillis()) }
    DisposableEffect(Unit) {
        onDispose {

            val cancerTypeAndQuestion = cancerType.toString() + " " + (quizQuestion?.question ?: cancerType )
            val timeSpent = System.currentTimeMillis() - startTime
            Log.d("Quiz", "Time spent: $timeSpent ms")

            saveLogToFile(context, "Quiz", timeSpent, cancerTypeAndQuestion)
        }
    }


    CancerSocietyTheme(dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            contentColor = Color.Black
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (quizQuestion != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = quizQuestion.question,
                            fontSize = responsiveFontSize(),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            lineHeight = 49.sp
                        )


                    }



                    val answers = quizQuestion.answers
                    if (answers.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            val imageRes3 = if (cancerType == "Bowel Cancer") {
                                R.drawable.men1
                            } else {
                                R.drawable.women1
                            }

                            Image(
                                painter = painterResource(imageRes3),
                                contentDescription = "Woman 1",
                                modifier = Modifier
                                    .size(250.dp)
                                    .weight(0.5f)
                            )

                            Box(
                                modifier = Modifier
                                    .weight(0.7f)
                                    .padding(horizontal = 16.dp)
                                    .aspectRatio(1f)
                            ) {
                                MyGrid(answers, selectedAnswer, cancerType.toString())
                            }

                            Image(
                                painter = painterResource(id = R.drawable.women2),
                                contentDescription = "Woman 2",
                                modifier = Modifier
                                    .size(250.dp)
                                    .weight(0.5f)
                            )
                        }

                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    if (cancerType != null && quizQuestion != null) {

                        var quizCorrect: Boolean? = null


                        quizCorrect = selectedAnswer.value == quizQuestion.correctAnswer

                        val route = "${Screens.QuizAnswer.route}/$nextScreen/$fullSequence/$cancerType/${quizQuestion.response}/$quizCorrect/${quizQuestion.subsection}"

                        QuizCheckButton(
                            text = "Check",
                            route = route,
                            navController = navController,
                            fullSequence = true,
                            cancerType = cancerType,
                            quizResponse = quizQuestion.response,
                            quizSubsection = quizQuestion.subsection,
                            enabled = selectedAnswer.value != null,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

/**
 * Shows grid of cards for the 4 possible quiz answers
 */
@Composable
fun MyGrid(answers: List<String>, selectedAnswer: MutableState<String?>, cancerType: String) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(answers) { answer ->
            MyCard(text = answer, selectedAnswer = selectedAnswer, cancerType = cancerType)

        }
    }
}

/**
 * This is how each possible quiz answer is displayed
 */
@Composable
fun MyCard(text: String, selectedAnswer: MutableState<String?>, cancerType: String) {
    val isSelected = remember { mutableStateOf(false) }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (selectedAnswer.value == text)  getCancerTypeColor(cancerType) else Color.White,
            contentColor = Color.Black
        ),
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(1f)
            .clickable { selectedAnswer.value = text },
        elevation = if (isSelected.value) CardDefaults.cardElevation(defaultElevation = 6.dp) else CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        ) }
    }

}
