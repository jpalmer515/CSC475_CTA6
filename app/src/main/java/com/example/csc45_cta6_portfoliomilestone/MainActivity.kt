package com.example.csc45_cta6_portfoliomilestone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    val menuItems = listOf(
        "User Profile",
        "New Document",
        "Message Physician",
        "Treatment",
        "Settings",
        "Exit",
        )
}

@Composable
fun MainMenu(viewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "mainScreen") {
        composable("mainScreen") { MainScreen(viewModel, navController) }
        composable("userProfile") { UserProfileScreen(navController) }
        composable("treatmentMenu") { TreatmentMainMenuScreen(navController) }
        composable("prescriptions") { PrescriptionsScreen(navController) }
        composable("physicalTherapy") { PhysicalTherapyScreen(navController) }
        composable("settingsMenu") {SettingsMainMenuScreen(navController)}
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
                    when (item) {
                        "User Profile" -> navController.navigate("userProfile")
                        "Treatment" -> navController.navigate("treatmentMenu")
                        "Settings" -> navController.navigate("settingsMenu")
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
fun UserProfileScreen(navController: NavController) {
    val testosterone = 700f
    val thyroid = 2.3f
    val bloodPressure = 120f
    val overallHealthScore = (testosterone / 10 + thyroid * 10 + bloodPressure / 2) / 3  // Example calculation

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Profile") },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Blue)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Health Score
            HealthMetricRow("Overall Health Score", overallHealthScore, "%.2f".format(overallHealthScore), 0f, 100f)
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

            // Health Metrics
            HealthMetricRow("Testosterone", testosterone, "$testosterone ng/dL", 300f, 1000f)
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
            HealthMetricRow("Thyroid", thyroid, "$thyroid mIU/L", 0.5f, 5.0f)
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
            HealthMetricRow("Blood Pressure", bloodPressure, "$bloodPressure/80 mmHg", 90f, 140f)
        }
    }
}

@Composable
fun HealthMetricRow(metric: String, value: Float, displayValue: String, min: Float, max: Float) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(metric, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.LightGray)
            Text(displayValue, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = getHealthColor(value, min, max))
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(Color.Gray, shape = RoundedCornerShape(3.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(((value - min) / (max - min)).coerceIn(0f, 1f))
                    .background(getHealthColor(value, min, max), shape = RoundedCornerShape(3.dp))
            )
        }
    }
}

@Composable
fun getHealthColor(value: Float, min: Float = 0f, max: Float = 100f): Color {
    val normalized = ((value - min) / (max - min)).coerceIn(0f, 1f)
    return when {
        //colors setup as an example, color metrics would be applied to each health metric individually
        normalized < 0.33f -> Color.Red
        normalized < 0.66f -> Color.Yellow
        else -> Color.Green
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreatmentMainMenuScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Treatment Menu") },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Blue)
                    }
                }
            )
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
            listOf("Prescriptions",
                "Physical Therapy",
                "Doctor Notes",
                "Pay Bill")
                .forEach { item ->
                    MenuButton(item) {}
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrescriptionsScreen(navController: NavController) {
    val prescriptions = listOf(
        " Lisinopril " to "10mg by mouth, daily",
        " Metformin " to "500mg by mouth, 2x a day",
        " Atorvastatin " to "20mg by mouth, at night",
        " Lorazepam " to "1mg by mouth, 2x day "
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prescriptions") },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Blue)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            prescriptions.forEach { (name, dosage) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(name, color = Color.LightGray, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Text(dosage, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhysicalTherapyScreen(navController: NavController) {
    val therapyInstructions = listOf (
        " Left Knee Rehab " to "Leg Extension / 2.5lb / 30 Reps / 4x a week \n" +
                "Step Ups /  Bodyweight / 15 Reps / 4x a week",
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Physcial Therapy") },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Blue)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            therapyInstructions.forEach { (name, dosage) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(name, color = Color.LightGray, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Text(dosage, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMainMenuScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Blue)
                    }
                }
            )
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
            listOf(
                "External Data",
                "Add Additional User",
                "UI Settings",
                "Notifications"
            ).forEach { item ->
                MenuButton(item) {}
            }
        }
    }
}

@Composable
fun ExitApplication() {
    //Exit App Syntax
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
        UserProfileScreen(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun TreatmentMainPreview() {
    CSC45_CTA6_PortfolioMilestoneTheme {
        TreatmentMainMenuScreen(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsMainPreview() {
    CSC45_CTA6_PortfolioMilestoneTheme {
        SettingsMainMenuScreen(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun PrescriptionsPreview() {
    CSC45_CTA6_PortfolioMilestoneTheme {
        PrescriptionsScreen(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun PhysicalTherapyPreview() {
    CSC45_CTA6_PortfolioMilestoneTheme {
        PhysicalTherapyScreen(rememberNavController())
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