import kotlinx.coroutines.channels.Channel

object MessageQueue{
    var channel=Channel<String>(Channel.UNLIMITED)

    suspend fun send(name:String,text:String){
        Debugger.log("✉️ $name enqueued message: '$text'")
        channel.send(text)
    }


    suspend fun receive(processName:String){

        val message =channel.receive()
        Debugger.log("📥 $processName processed message: '$message'")

    }
    fun reset(){
        channel.cancel()

        channel=Channel(Channel.UNLIMITED)
    }
}