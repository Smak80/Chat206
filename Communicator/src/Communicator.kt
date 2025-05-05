import kotlinx.coroutines.*
import ru.smak.chat.ActionCompletionHandler
import java.io.PrintWriter
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.thread
import kotlin.coroutines.suspendCoroutine

class Communicator(
    private val socket: AsynchronousSocketChannel,
) {
    private var parse: ((String)->Unit)? = null
    private var isRunning = false
    private val communicatorScope = CoroutineScope(Dispatchers.IO)
    //private val scanner = Scanner(socket.getInputStream())
    //private val writer = PrintWriter(socket.getOutputStream())

    private fun startMessageAccepting(){
        communicatorScope.launch {
            while (isRunning){
                var capacity = Int.SIZE_BYTES
                repeat(2){ i ->
                    val buf = ByteBuffer.allocate(capacity)
                    suspendCoroutine { socket.read(buf, null, ActionCompletionHandler(it)) }
                    buf.flip()
                    if (i == 0) capacity = buf.getInt()
                    else {
                        val data = Charsets.UTF_8.decode(buf).toString()
                        parse?.invoke(data)
                    }
                    buf.clear()
                }
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