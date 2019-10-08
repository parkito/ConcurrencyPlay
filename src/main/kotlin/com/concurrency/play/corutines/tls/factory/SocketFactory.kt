package com.concurrency.play.corutines.tls.factory

import javax.net.ssl.SSLServerSocket
import javax.net.ssl.SSLServerSocketFactory
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

object SocketFactory {

    private val protocols = arrayOf("TLSv1.2") //"TLSv1.3"
    private val ciphers = arrayOf("TLS_AES_128_GCM_SHA256")

    fun createServerSocket(port: Int): SSLServerSocket {
        val serverSocket = SSLServerSocketFactory.getDefault()
                .createServerSocket(port) as SSLServerSocket

        serverSocket.enabledProtocols = protocols
        serverSocket.enabledCipherSuites = ciphers

        return serverSocket
    }

    fun createClientSocket(host: String, port: Int): SSLSocket {
        val clientSocket = SSLSocketFactory.getDefault()
                .createSocket(host, port) as SSLSocket

        clientSocket.enabledProtocols = protocols
        clientSocket.enabledCipherSuites = ciphers

        return clientSocket
    }
}