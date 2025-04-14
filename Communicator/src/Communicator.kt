import java.io.PrintWriter
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread

class Communicator(
    private val socket: Socket,
) {

    private var parse: ((String)->Unit)? = null
    private var isRunning = false
    private val scanner = Scanner(socket.getInputStream())
    private val writer = PrintWriter(socket.getOutputStream())

    private fun startMessageAccepting(){
        thread {
            while(isRunning){
                val data = scanner.nextLine()
                parse?.invoke(data)
            }
            socket.close()
        }
    }

    fun sendMessage(message: String){
        writer.println(message)
        writer.flush()
    }

    fun start(parser: (String)->Unit) {
        if (!isRunning) {
            parse = parser
            isRunning = true
            startMessageAccepting()
        }
    }

    fun stop(){
        isRunning = false
    }
}