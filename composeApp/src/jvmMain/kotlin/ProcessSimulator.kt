import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.ipc.SharedMemorySimulation
import kotlin.random.Random

object ProcessSimulator {

    suspend fun runRaceConditionScenario() = coroutineScope {

        Debugger.log("🚀 STARTING: Race Condition Scenario")

        launch {
            SystemState.p1Status.value = "Running"
            SharedMemorySimulation.unsafeWrite("Process-1")
            SystemState.p1Status.value = "Finished"
        }


    }
    suspend fun runSafeExecutionScenario() = coroutineScope {
        SystemState.reset()
        Debugger.clear()
        SharedMemorySimulation.reset()

        Debugger.setExplanation(
            title= "Safe Execution (Mutex)",
            reason="A Mutex lock has been applied to the shared memory. Process-1 takes the lock. When Process-2 arrives, the OS forces it to sleep (block) until Process-1 finishes and releases the key.",
            solution="Working as intended. Data integrity is preserved."
        )
        Debugger.log("🚀 STARTING: Safe Execution (Mutex) Scenario")




        launch {
            SystemState.p1Status.value="Running"
            SharedMemorySimulation.safeWrite("Process-1")
            SystemState.p1Status.value= "Finished"
        }



        launch {
            delay(Random.nextLong(5, 20))

            SystemState.p2Status.value= "Running"
            SharedMemorySimulation.safeWrite("Process-2")
            SystemState.p2Status.value= "Finished"
        }
    }
    suspend fun runMessageQueueScenario() = coroutineScope {
        SystemState.reset()
        Debugger.clear()
        MessageQueue.reset()

        Debugger.setExplanation(
            title ="Message Queue",
            reason ="Processes are communicating using an asynchronous OS message queue (simulated via Kotlin Channel). Messages are preserved as discrete packets.",
            solution= "Working as intended. No collisions or blocking detected due to unlimited queue capacity."
        )
        Debugger.log("🚀 STARTING: Message Queue Scenario")

        launch {
            SystemState.p1Status.value= "Sending Messages"

            SystemState.isP1Sending.value= true
            MessageQueue.send("Process-A","Task 1: Process Image")
            SystemState.messageQueueSize.value++

            SystemState.isP1Sending.value= false

            delay(Random.nextLong(80, 120))


            SystemState.isP1Sending.value =true
            MessageQueue.send("Process-A","Task 2: Save to DB")
            SystemState.messageQueueSize.value++
            SystemState.isP1Sending.value=false

            SystemState.p1Status.value="Finished"
        }
}

