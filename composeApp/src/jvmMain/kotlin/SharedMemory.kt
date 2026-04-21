import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.random.Random

object SharedMemorySimulation {
    private var sharedData= 0
    private var isWriting= false
    private val mutex= Mutex()

    suspend fun unsafeWrite(processName: String) {
        Debugger.log("$processName is attempting to write...")


        if (isWriting) {
            Debugger.log("⚠️ RACE CONDITION DETECTED: $processName collided with another process while writing!")
            SystemState.isRaceConditionDetected.value =true
        }





        isWriting =true

        val temp= sharedData
        delay(Random.nextLong(80,150))
        sharedData= temp+ 1


        SystemState.sharedMemoryValue.value= sharedData
        Debugger.log("$processName successfully wrote data. Current Value: $sharedData")

        isWriting =false
    }
    suspend fun safeWrite(processName: String) {
        Debugger.log("$processName is waiting for the lock...")

        mutex.withLock {
            SystemState.mutexOwner.value= processName
            Debugger.log("🔒 $processName acquired lock. Writing...")

            val temp= sharedData
            delay(Random.nextLong(80, 150))

            sharedData= temp +1

            SystemState.sharedMemoryValue.value =sharedData
            Debugger.log("🔓 $processName finished writing and released lock. Current Value: $sharedData")
            SystemState.mutexOwner.value="Unlocked"
        }
    }

    suspend fun reset() {
        sharedData= 0
        isWriting =false
    }


}