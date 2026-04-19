import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun SharedMemoryTopology(scenario: String) {

    val memColor by animateColorAsState(
        targetValue= if (SystemState.isRaceConditionDetected.value) Color(0xFFEF4444) else Color(0xFF38BDF8),
        animationSpec= tween(300)
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.spacedBy(100.dp)) {

            ProcessNode("Process 1\n(${SystemState.p1Status.value})", getStatusColor(SystemState.p1Status.value))
            ProcessNode("Process 2\n(${SystemState.p2Status.value})", getStatusColor(SystemState.p2Status.value))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(140.dp), modifier = Modifier.padding(vertical = 8.dp)) {
            Text("⬇", color= Color.Gray, fontSize = 24.sp)
            Text("⬇", color =Color.Gray, fontSize = 24.sp)
        }
        Box(
            modifier = Modifier
                .width(200.dp)
                .background(memColor.copy(alpha= 0.2f), RoundedCornerShape(8.dp))

                .border(2.dp, memColor, RoundedCornerShape(8.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment= Alignment.CenterHorizontally) {
                Text("Shared Memory", color= Color.White, fontWeight = FontWeight.Bold)
                Text("Value: ${SystemState.sharedMemoryValue.value}", color =memColor, fontSize= 20.sp, fontWeight= FontWeight.ExtraBold)

                if (scenario== "Safe Execution") {
                    Text(
                        text= if (SystemState.mutexOwner.value == "Unlocked") "🔓 Unlocked" else "🔒 Locked by ${SystemState.mutexOwner.value}",
                        color =if (SystemState.mutexOwner.value == "Unlocked") Color(0xFF10B981) else Color(0xFFF59E0B),
                        modifier= Modifier.padding(top = 8.dp)
                    )
                }
            }
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




@Composable
fun MessageQueueTopology() {

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        ProcessNode("Process A\n(${SystemState.p1Status.value})", getStatusColor(SystemState.p1Status.value))

        Text(if (SystemState.isP1Sending.value) "➡ ✉️" else "➡", color = if (SystemState.isP1Sending.value) Color(0xFF10B981) else Color.Gray, fontSize = 24.sp)
        Box(
            modifier = Modifier
                .background(Color(0xFF8B5CF6).copy(alpha= 0.2f), RoundedCornerShape(8.dp))
                .border(2.dp, Color(0xFF8B5CF6), RoundedCornerShape(8.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Message Queue", color= Color.White, fontWeight = FontWeight.Bold)

                Text("Messages: ${SystemState.messageQueueSize.value}", color= Color(0xFFC4B5FD), fontSize = 16.sp)
            }
        }

        Text(if (SystemState.isP2Receiving.value) "✉️ ➡" else "➡", color= if (SystemState.isP2Receiving.value) Color(0xFF10B981) else Color.Gray, fontSize = 24.sp)
        ProcessNode("Process B\n(${SystemState.p2Status.value})", getStatusColor(SystemState.p2Status.value))
    }
}


@Composable
fun DeadlockTopology() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Row(horizontalArrangement = Arrangement.spacedBy(60.dp), verticalAlignment = Alignment.CenterVertically) {
            ProcessNode("Process 1", getStatusColor(SystemState.p1Status.value))
            ResourceArrow(SystemState.p1ResourceA_Status.value, "➡")

            ResourceNode("Resource A", SystemState.p1ResourceA_Status.value, SystemState.p2ResourceA_Status.value)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(120.dp)) {
            ResourceArrow(SystemState.p1ResourceB_Status.value, "⬇")

            ResourceArrow(SystemState.p2ResourceA_Status.value, "⬇")
        }

        Row(horizontalArrangement = Arrangement.spacedBy(60.dp), verticalAlignment = Alignment.CenterVertically) {
            ResourceNode("Resource B", SystemState.p1ResourceB_Status.value, SystemState.p2ResourceB_Status.value)
            ResourceArrow(SystemState.p2ResourceB_Status.value, "⬅")
            ProcessNode("Process 2", getStatusColor(SystemState.p2Status.value))
        }
    }
}

@Composable
fun ProcessNode(text: String, color: Color) {

    Box(
        modifier = Modifier
            .width(140.dp)
            .background(color.copy(alpha= 0.2f), RoundedCornerShape(8.dp))
            .border(2.dp, color, RoundedCornerShape(8.dp))
            .padding(12.dp),

        contentAlignment = Alignment.Center
    ) {
        Text(text, color= Color.White, textAlign = TextAlign.Center, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ResourceNode(name: String, p1State: String, p2State: String) {
    val borderColor =when {
        p1State== "Acquired" || p2State == "Acquired" ->Color(0xFF10B981)
        p1State== "Requesting" || p2State == "Requesting" ->Color(0xFFF59E0B)
        else-> Color(0xFF64748B)

    }
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color(0xFF0F172A), CircleShape)
            .border(2.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(name, color =Color.White, fontSize= 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ResourceArrow(state: String, symbol: String) {

    val color= when (state) {
        "Acquired"-> Color(0xFF10B981)
        "Requesting" ->Color(0xFFEF4444)

        else-> Color(0xFF334155)
    }
    Text(symbol, color= color, fontSize = 28.sp, fontWeight= FontWeight.ExtraBold)
}