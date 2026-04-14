package org.example.ipc


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



}