package nz.ac.uclive.nse41.cancersociety.screens

import BackButton
import android.util.Log
import androidx.compose.foundation.Image
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

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

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

/**
 * The final screen which is shown at the very end of the full sequence flow only.
 */
@Composable
fun FinalScreen(navController: NavController, fullSequence: Boolean, cancerType: String?) {
    val context = LocalContext.current
    val cancerInfo = getCancerInfoFromJson(context, "CancerInfo.json")
    val selectedCancer = cancerInfo?.cancers?.find { it.cancer == cancerType }

    val screeningSupportServicesSubsection = selectedCancer?.subsections?.find { it.subsection == "Screening support services" }
    Log.d("screeningSupportServicesSubsection", screeningSupportServicesSubsection.toString())




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
                        currentScreenIndex = 4,
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
                    // Title
                    Text(
                        text = "Congratulations, you finished!",
                        fontSize = responsiveFontSize(),
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Congratulations, you've gone through all topics for $cancerType! Go back to the homepage to revisit this section or find out about the other cancer screening programmes in Aotearoa, New Zealand.",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )


                    Text(
                        text = "Finding cancer early can increase positive outcomes",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "If you are worried that you or someone you care about have signs or symptoms of cancer, or you notice something unusual for you or a loved one please talk to your doctor or trusted health professional soon.",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "This does not mean that you or a loved one has cancer but it is important to get it checked by your doctor. The Cancer Information Helpline is available on 0800 226 237 for information and support." ,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )


                    Spacer(modifier = Modifier.height(20.dp))



                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {


                        val imageRes2 = if (cancerType == "Bowel Cancer") {
                            R.drawable.men2
                        } else {
                            R.drawable.women2
                        }
                        val imageRes3 = if (cancerType == "Bowel Cancer") {
                            R.drawable.men3
                        } else {
                            R.drawable.women3
                        }

                        Image(
                            painter = painterResource(id = R.drawable.women1),
                            contentDescription = "Woman 1",
                            modifier = Modifier.size(200.dp)
                        )
                        Image(
                            painter = painterResource(imageRes2),
                            contentDescription = "Woman 2",
                            modifier = Modifier.size(200.dp)
                        )
                        Image(
                            painter = painterResource(imageRes3),
                            contentDescription = "Woman 3",
                            modifier = Modifier.size(200.dp)
                        )
                    }
                }


                BackButton(navController)

                Button(
                    onClick = { navController.navigate("${Screens.CancerHomepage.route}/$cancerType") },
                    colors = ButtonDefaults.buttonColors(containerColor = Bluey),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Text("Finish", fontSize = 40.sp, color = Color.Black)
                }

            }
        }
    }
}
