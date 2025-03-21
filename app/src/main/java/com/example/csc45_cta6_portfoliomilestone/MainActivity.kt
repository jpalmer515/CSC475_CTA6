package com.example.csc45_cta6_portfoliomilestone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.csc45_cta6_portfoliomilestone.ui.theme.CSC45_CTA6_PortfolioMilestoneTheme
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CSC45_CTA6_PortfolioMilestoneTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        MainMenu(viewModel = MainViewModel())
                    }
                }
            }
        }
    }
}

class MainViewModel : ViewModel() {
    val menuItems = listOf("User Profile", "New Document", "Message Physician", "Treatment", "Settings")
}

@Composable
fun MainMenu(viewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "mainScreen") {
        composable("mainScreen") { MainScreen(viewModel, navController) }
        composable("userProfile") { UserProfileScreen() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Health Data Aggregation App") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            viewModel.menuItems.forEach { item ->
                MenuButton(item) {
                    if (item == "User Profile") {
                        navController.navigate("userProfile")
                    }
                }
            }
        }
    }
}

@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors()
    ) {
        Text(text, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen() {
    val testosterone = 700f
    val thyroid = 2.3f
    val bloodPressure = 120f
    val overallHealthScore = (testosterone / 10 + thyroid * 10 + bloodPressure / 2) / 3  // Example calculation

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("User Profile") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Health Score
            HealthMetricRow("Overall Health Score", overallHealthScore, "%.2f".format(overallHealthScore), 0f, 100f)
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

            // Health Metrics
            HealthMetricRow("Testosterone", testosterone, "$testosterone ng/dL", 300f, 1000f)
            HealthMetricRow("Thyroid", thyroid, "$thyroid mIU/L", 0.5f, 5.0f)
            HealthMetricRow("Blood Pressure", bloodPressure, "$bloodPressure/80 mmHg", 90f, 140f)
        }
    }
}

@Composable
fun HealthMetricRow(metric: String, value: Float, displayValue: String, min: Float, max: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(metric, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.LightGray)
        Text(displayValue, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = getHealthColor(value, min, max))
    }
}

@Composable
fun getHealthColor(value: Float, min: Float = 0f, max: Float = 100f): Color {
    val normalized = ((value - min) / (max - min)).coerceIn(0f, 1f)
    return when {
        normalized < 0.33f -> Color.Red
        normalized < 0.66f -> Color.Yellow
        else -> Color.Green
    }
}


@Preview(showBackground = true)
@Composable
fun MenuPreview() {
    CSC45_CTA6_PortfolioMilestoneTheme {
        MainMenu(viewModel = MainViewModel())
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfilePreview() {
    CSC45_CTA6_PortfolioMilestoneTheme {
        UserProfileScreen()
    }
}

/*
user's profile (gamified scoring)
    overall health score (dependent on a formula (tbd))
    line items for specific tests/conditions
        hormone levels
        mineral levels
        thyroid condition
        allergies
        ..etc
new document
    document in profile
        can be sent to doctor or not
        links to treatment, after physician gives treatment
message physician
    message provider
        can link document
    make new appointment
        can link document
treatment
    prescription
    physical therapy
    notes from doctor
        links to document
    pay bill
settings
    UI settings
    linking to another app
        medical provider (UCHealth, other)
        fitness app / device (fitbit, woot)
    Notifications
        annual / bi-annual / age dependent test, etc
        on/off
    Add Additional User
        Hospice
            What can they access
        Disabled (blind / deaf / mute)
            What can they access
 */