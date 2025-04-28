import ru.smak.chat.ActionCompletionHandler
import java.io.PrintWriter
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.util.*
import kotlin.concurrent.thread
import kotlin.coroutines.suspendCoroutine

class Communicator(
    private val socket: AsynchronousSocketChannel,
) {

    private var parse: ((String)->Unit)? = null
    private var isRunning = false
    //private val scanner = Scanner(socket.getInputStream())
    //private val writer = PrintWriter(socket.getOutputStream())

    private fun startMessageAccepting(){
        thread {
            while(isRunning){
                val data = scanner.nextLine()
                parse?.invoke(data)
            }
            socket.close()
        }
    }

    suspend fun sendMessage(message: String){
        val ba = message.toByteArray()
        val buf = ByteBuffer.allocate(ba.size + Int.SIZE_BYTES)
        buf.putInt(ba.size)
        buf.put(ba)
        buf.flip()

        val wrote = suspendCoroutine {
            socket.write(buf, null, ActionCompletionHandler(it))
        }
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