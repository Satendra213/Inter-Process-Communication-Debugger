object PipeSimulation {
    private const val capacity= 3
    private val buffer=mutableListOf<String>()


    private fun getVisualBuffer(): String {
        val filled="■ ".repeat(buffer.size)
        val empty = "□ ".repeat(capacity- buffer.size)

        return "[ ${filled}${empty}]".trim()
    }

    suspend fun write(pname: String, data: String) {
        if (buffer.size>=capacity) {
            SystemState.isPipeBottleneck.value= true
            SystemState.pipeBufferVisual.value =getVisualBuffer()
            Debugger.log("⚠️ BOTTLENECK DETECTED: Pipe buffer is FULL! ${getVisualBuffer()} $pname cannot write and is blocked.")
            return
        }



        buffer.add(data)
        SystemState.isPipeBottleneck.value =false
        SystemState.pipeBufferVisual.value= getVisualBuffer()
        Debugger.log("$pname wrote: $data | Buffer: ${getVisualBuffer()}")
    }

    suspend fun read(pname: String) {

        if (buffer.isNotEmpty()) {
            val data=buffer.removeAt(0)
            SystemState.isPipeBottleneck.value =false
            SystemState.pipeBufferVisual.value= getVisualBuffer()
            Debugger.log("$pname read: $data | Buffer: ${getVisualBuffer()}")
        } else {
            Debugger.log("$pname waiting to read... Pipe is empty. [ □ □ □ ]")
        }
    }


    suspend fun reset() {
        buffer.clear()
    }



}