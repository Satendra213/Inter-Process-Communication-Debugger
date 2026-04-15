import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
