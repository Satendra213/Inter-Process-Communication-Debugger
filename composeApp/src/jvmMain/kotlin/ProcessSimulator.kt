import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

object ProcessSimulator {

    suspend fun runRaceConditionScenario() = coroutineScope {
        SystemState.reset()
        Debugger.clear()
        SharedMemorySimulation.reset()

        Debugger.setExplanation(
            title = "Race Condition",
            reason = "Two independent processes are reading and writing to the exact same memory variable simultaneously without any synchronization. The final output is corrupted based on unpredictable CPU scheduling.",
            solution = "Implement a Mutex (Mutual Exclusion lock) to create a 'Critical Section', ensuring only one process can modify the data at a time."
        )
        Debugger.log("🚀 STARTING: Race Condition Scenario")

        launch {
            SystemState.p1Status.value = "Running"
            SharedMemorySimulation.unsafeWrite("Process-1")
            SystemState.p1Status.value = "Finished"
        }

        launch {
            delay(Random.nextLong(5, 20))
            SystemState.p2Status.value = "Running"
            SharedMemorySimulation.unsafeWrite("Process-2")

            SystemState.p2Status.value = "Finished"
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
}

