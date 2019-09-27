package com.concurrency.play.corutines.tcp

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class Publisher(port: Int) {

//    private val serverSocket: ServerSocket
    private val clientSocket: Socket
    private val printWriter: PrintWriter
    private val bufferedReader: BufferedReader

    init {
        clientSocket = Socket("localhost", port)

        printWriter = PrintWriter(clientSocket.getOutputStream(), true)
        bufferedReader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
    }

    fun publish(message: String): String {
        printWriter.println(message)
        val response = bufferedReader.readLine()

        bufferedReader.close()
        printWriter.close()
        clientSocket.close()
//        serverSocket.close()

        return response
    }
}

fun main(args: Array<String>) {
    val port = args[0].toInt()
    val message = args[1]

    println("Publisher: publishing $message on localhost:$port")

    val publisher = Publisher(port)
    val returnResult = publisher.publish(message)

    println("Publisher: received from listener $returnResult")
}