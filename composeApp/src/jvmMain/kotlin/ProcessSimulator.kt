import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
    suspend fun runPipeBottleneckScenario() = coroutineScope {
        SystemState.reset()
        Debugger.clear()
        PipeSimulation.reset()
        Debugger.setExplanation(
            title= "Pipe Bottleneck (Buffer Overflow)",
            reason= "The Producer process is writing data significantly faster than the Consumer process can read it. The limited OS pipe buffer hits maximum capacity.",
            solution ="The OS automatically blocks the Producer to prevent data loss. Optimization requires increasing consumer speed or increasing buffer size."
        )
        Debugger.log("🚀 STARTING: Pipe Bottleneck Scenario")

        launch {
            SystemState.p1Status.value="Producing (Fast)"
            for (i in 1..5) {
                if (SystemState.isPipeBottleneck.value) SystemState.p1Status.value= "Blocked by OS"
                PipeSimulation. write("Producer","DataChunk-$i")
                delay(Random.nextLong(30, 70))
            }

            SystemState.p1Status.value="Finished"
        }

        launch {
            SystemState.p2Status.value="Consuming (Slow)"
            for (i in 1..5) {

                delay(Random.nextLong(250, 350))
                PipeSimulation.read("Consumer")
            }
            SystemState.p2Status.value ="Finished"
        }
    }
    suspend fun runMessageQueueScenario() = coroutineScope {
        SystemState.reset()
        Debugger.clear()
        MessageQueue.reset()

        Debugger.setExplanation(
            title = "Message Queue",
            reason = "Processes are communicating using an asynchronous OS message queue (simulated via Kotlin Channel). Messages are preserved as discrete packets.",
            solution = "Working as intended. No collisions or blocking detected due to unlimited queue capacity."
        )
        Debugger.log("🚀 STARTING: Message Queue Scenario")

        launch {
            SystemState.p1Status.value = "Sending Messages"

            SystemState.isP1Sending.value = true
            MessageQueue.send("Process-A", "Task 1: Process Image")
            SystemState.messageQueueSize.value++

            SystemState.isP1Sending.value = false

            delay(Random.nextLong(80, 120))


            SystemState.isP1Sending.value = true
            MessageQueue.send("Process-A", "Task 2: Save to DB")
            SystemState.messageQueueSize.value++
            SystemState.isP1Sending.value = false
            SystemState.p1Status.value = "Finished"
        }

        launch {
            SystemState.p2Status.value ="Awaiting Messages"
            delay(Random.nextLong(40, 60))


            SystemState.p2Status.value="Processing Message"
            SystemState.isP2Receiving.value= true
            MessageQueue.receive("Process-B")
            SystemState.messageQueueSize.value = maxOf(0, SystemState.messageQueueSize.value- 1)
            SystemState.isP2Receiving.value= false

            delay(Random.nextLong(80,120))


            SystemState.isP2Receiving.value= true
            MessageQueue.receive("Process-B")
            SystemState.messageQueueSize.value= maxOf(0, SystemState.messageQueueSize.value - 1)
            SystemState.isP2Receiving.value= false

            SystemState.p2Status.value= "Finished"
        }


    }

    suspend fun runDeadlockScenario() = coroutineScope {
        SystemState.reset()
        Debugger.clear()

        Debugger.setExplanation(
            title = "Deadlock",
            reason = "Process-1 holds Resource A and waits for Resource B. Process-2 holds Resource B and waits for Resource A. Both are stuck in an infinite circular wait.",
            solution = "Implement 'Resource Ordering'. Force all processes to request Resource A before they are allowed to request Resource B to prevent circular dependencies."
        )
        Debugger.log("🚀 STARTING: Deadlock Scenario")

        val resourceA = Mutex()
        val resourceB = Mutex()


        launch {
            SystemState.p1Status.value ="Running"
            Debugger.log("Process-1 attempting to lock Resource A...")

            SystemState.p1ResourceA_Status.value="Requesting"
            resourceA.withLock {
                SystemState.p1ResourceA_Status.value="Acquired"
                Debugger.log("🔒 Process-1 locked Resource A. Waiting for B...")

                SystemState.p1Status.value="Holding A, Waiting for B"
                delay(Random.nextLong(80, 120))
                Debugger.log("Process-1 attempting to lock Resource B...")
                SystemState.p1ResourceB_Status.value ="Requesting"

                resourceB.withLock {
                    SystemState.p1ResourceB_Status.value="Acquired"
                    Debugger.log("Process-1 locked both resources!")
                }
            }
        }

        launch {
            delay(Random.nextLong(5,15))
            SystemState.p2Status.value ="Running"
            Debugger.log("Process-2 attempting to lock Resource B...")

            SystemState.p2ResourceB_Status.value= "Requesting"
            resourceB.withLock {
                SystemState.p2ResourceB_Status.value= "Acquired"
                Debugger.log("🔒 Process-2 locked Resource B. Waiting for A...")
                SystemState.p2Status.value= "Holding B, Waiting for A"

                delay(Random.nextLong(80, 120))
                Debugger.log("Process-2 attempting to lock Resource A...")
                SystemState.p2ResourceA_Status.value="Requesting"
                resourceA.withLock {

                    SystemState.p2ResourceA_Status.value ="Acquired"
                    Debugger.log("Process-2 locked both resources!")
                }
            }
        }


    }


}

