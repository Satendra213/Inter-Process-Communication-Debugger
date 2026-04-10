import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class ScenarioInfo(val title:String="",val reason:String="",val solution:String="")
object Debugger{
    val logs= mutableStateListOf<String>()

    var modeSlow=mutableStateOf(false)


    var currentInfo =mutableStateOf(ScenarioInfo())

    private val mutex =Mutex()
    private val formatter= DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
    suspend fun log(mess: String){

        if(modeSlow.value){
            delay(500)
        }
        val time=LocalTime.now().format(formatter)


        mutex.withLock{
            logs.add("[$time] $mess")
        }
    }


    suspend fun clear(){
        mutex.withLock{
            logs.clear()
            currentInfo.value=ScenarioInfo()
        }
    }
    fun setExplanation(title:String,reason:String,solution:String){

        currentInfo.value=ScenarioInfo(title,reason,solution)
    }
}