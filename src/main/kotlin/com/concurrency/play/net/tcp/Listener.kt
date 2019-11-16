package com.concurrency.play.net.tcp

import com.concurrency.play.net.tls.factory.SocketFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class Listener(private val serverSocket: ServerSocket) {

    private lateinit var clientSocket: Socket
    private lateinit var printWriter: PrintWriter
    private lateinit var bufferedReader: BufferedReader

    companion object {
        private const val SUCCESS_RESPONSE = "OK"
    }

    fun listen() {
        while (true) {
            clientSocket = serverSocket.accept()
            bufferedReader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))

            val received = bufferedReader.readLine()
            println("Listener: received $received")

            println("Listener: sending response OK")
            printWriter = PrintWriter(clientSocket.getOutputStream(), true)
            printWriter.println(SUCCESS_RESPONSE)
        }
    }
}

fun main(args: Array<String>) {
    val port = args[0].toInt()

    println("Listener: start listen to localhost:$port")

    val listener = Listener(SocketFactory.createServerSocket(port))
//    val listener = Listener(SocketFactory.createTlsServerSocket(port))

    listener.listen()

    println("Listener: shutting down")
}
