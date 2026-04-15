import androidx.compose.runtime.mutableStateOf

object SystemState {

    var p1Status =mutableStateOf("Idle")
    var p2Status= mutableStateOf("Idle")

    var sharedMemoryValue= mutableStateOf(0)
    var isRaceConditionDetected= mutableStateOf(false)


    var mutexOwner =mutableStateOf("Unlocked")


    var pipeBufferVisual =mutableStateOf("[ □ □ □ ]")
    var isPipeBottleneck=mutableStateOf(false)


    var isP1Sending= mutableStateOf(false)
    var isP2Receiving =mutableStateOf(false)
    var messageQueueSize=mutableStateOf(0)


    var p1ResourceA_Status=mutableStateOf("Idle")
    var p1ResourceB_Status =mutableStateOf("Idle")

    var p2ResourceA_Status=mutableStateOf("Idle")
    var p2ResourceB_Status=mutableStateOf("Idle")

    fun reset() {
        p1Status.value="Idle"

        p2Status.value="Idle"
        sharedMemoryValue.value=0
        isRaceConditionDetected.value =false
        mutexOwner.value= "Unlocked"

        pipeBufferVisual.value="[ □ □ □ ]"

        isPipeBottleneck.value=false

        isP1Sending.value =false
        isP2Receiving.value=false


        messageQueueSize.value=0

        p1ResourceA_Status.value= "Idle"
        p1ResourceB_Status.value ="Idle"
        p2ResourceA_Status.value="Idle"

        p2ResourceB_Status.value= "Idle"
    }
}