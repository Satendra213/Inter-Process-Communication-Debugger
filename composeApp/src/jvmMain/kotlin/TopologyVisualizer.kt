import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopologyDiagram(scenario: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()

            .padding(top = 16.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .background(Color(0xFF1E293B), RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (scenario) {
            "Race Condition", "Safe Execution" ->SharedMemoryTopology(scenario)

            "Pipe Bottleneck"-> PipeTopology()
            "Message Queue" -> MessageQueueTopology()
            "Deadlock" ->DeadlockTopology()
            else-> Text("Select a scenario to view topology", color= Color.Gray)
        }
    }
}

@Composable
fun PipeTopology() {
    val pipeColor by animateColorAsState(
        targetValue= if (SystemState.isPipeBottleneck.value) Color(0xFFEF4444) else Color(0xFF38BDF8)
    )

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        ProcessNode("Producer\n(${SystemState.p1Status.value})", getStatusColor(SystemState.p1Status.value))
        Text("➡", color= Color.Gray, fontSize = 24.sp)

        Box(
            modifier = Modifier
                .background(pipeColor.copy(alpha= 0.2f), RoundedCornerShape(8.dp))
                .border(2.dp, pipeColor, RoundedCornerShape(8.dp))
                .padding(16.dp),

            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("OS Pipe Buffer", color =Color.White, fontWeight = FontWeight.Bold)
                Text(SystemState.pipeBufferVisual.value, color =pipeColor, fontSize= 20.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 4.sp)
            }
        }
        Text("➡", color= Color.Gray, fontSize = 24.sp)
        ProcessNode("Consumer\n(${SystemState.p2Status.value})", getStatusColor(SystemState.p2Status.value))
    }
}
