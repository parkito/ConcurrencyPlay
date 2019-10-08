package com.concurrency.play.corutines.tls

import com.concurrency.play.corutines.tls.factory.SocketFactory
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.IOException
import javax.net.ssl.SSLServerSocket
import javax.net.ssl.SSLSocket

class Server(val port: Int) {
    private val serverSocket: SSLServerSocket = SocketFactory.createTlsServerSocket(port)

    fun start() {
        try {
            (serverSocket.accept() as SSLSocket).use { socket ->
                println("accepted")
                val inStream = BufferedInputStream(socket.inputStream)
                val os = BufferedOutputStream(socket.outputStream)
                val data = ByteArray(2048)
                val len = inStream.read(data)
                if (len <= 0) {
                    throw IOException("no data received")
                }

                System.out.printf("server received %d bytes: %s%n",
                        len, String(data, 0, len))
                os.write(data, 0, len)
                os.flush()
            }
        } catch (e: Exception) {
            System.out.printf("exception: %s%n", e.message)
        }

    }

}