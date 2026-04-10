package org.example.ipc

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.darkColors
import androidx.compose.material3.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource

import interprocesscommunicationdebugger.composeapp.generated.resources.Res
import interprocesscommunicationdebugger.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.launch

@Composable
@Preview
fun App() {
    MaterialTheme(
        colors= darkColors(
            background= Color.Transparent,
            surface= Color(0xFF1E293B)
        )
    ) {
        val c_Scope= rememberCoroutineScope()
        var curr_scn by remember { mutableStateOf<String?>(null) }


        val bg_grad_brush =Brush.verticalGradient(
            colors= listOf(Color(0xFF0F172A), Color(0xFF020617))
        )
        Box(modifier =Modifier.fillMaxSize().background(bg_grad_brush)) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                androidx.compose.material.Text(
                    text = "IPC Debugging Simulator",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,

                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                androidx.compose.material.Text(
                    text = "Real-time Inter-Process Communication & Synchronization Visualizer",
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Card(
                    modifier= Modifier.fillMaxWidth().padding(bottom = 12.dp).shadow(8.dp, RoundedCornerShape(12.dp)),
                    shape= RoundedCornerShape(12.dp),

                    backgroundColor= Color(0xFF1E293B).copy(alpha = 0.7f),
                    elevation= 0.dp
                ) {
                    Row(
                        modifier= Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(12.dp),
                        horizontalArrangement =Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                        verticalAlignment= Alignment.CenterVertically
                    ) {

                        ScenarioButton("Race Condition", Color(0xFFEF4444), curr_scn== "Race Condition") {
                            curr_scn ="Race Condition"
                            c_Scope.launch { ProcessSimulator.runRaceConditionScenario() }
                        }
                        ScenarioButton("Safe Execution", Color(0xFF10B981), curr_scn =="Safe Execution") {
                            curr_scn= "Safe Execution"
                            c_Scope.launch { ProcessSimulator.runSafeExecutionScenario() }
                        }
                        ScenarioButton("Pipe Bottleneck", Color(0xFFF59E0B), curr_scn =="Pipe Bottleneck") {
                            curr_scn= "Pipe Bottleneck"

                            c_Scope.launch { ProcessSimulator.runPipeBottleneckScenario() }
                        }
                        ScenarioButton("Message Queue", Color(0xFF3B82F6), curr_scn== "Message Queue") {
                            curr_scn ="Message Queue"
                            c_Scope.launch { ProcessSimulator.runMessageQueueScenario() }
                        }
                        ScenarioButton("Deadlock", Color(0xFF8B5CF6), curr_scn== "Deadlock") {
                            curr_scn= "Deadlock"
                            c_Scope.launch { ProcessSimulator.runDeadlockScenario() }
                        }

                    }
                }

            }
        }
    }
}


@Composable
fun ScenarioButton(text: String, color: Color, isActive: Boolean, onClick: () -> Unit) {
    val iSrc =remember { MutableInteractionSource() }
    val isPrsd by iSrc.collectIsPressedAsState()


    val sclVal by animateFloatAsState(
        targetValue= if (isPrsd) 0.92f else 1f,
        animationSpec= tween(durationMillis = 100),
        label= "buttonScale"
    )

    androidx.compose.material.Button(
        onClick = onClick,
        interactionSource = iSrc,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isActive) color else color.copy(alpha = 0.8f)

        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        elevation = ButtonDefaults.elevation(
            defaultElevation = if (isActive) 6.dp else 2.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 8.dp
        ),
        modifier = Modifier.scale(sclVal)
    ) {
        Text(
            text = text,
            color = Color.White,

            fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Bold,
            fontSize = 13.sp,
            letterSpacing = 0.5.sp
        )
    }
}
