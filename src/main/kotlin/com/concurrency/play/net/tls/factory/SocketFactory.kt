package com.concurrency.play.net.tls.factory

import java.net.ServerSocket
import java.net.Socket
import javax.net.ssl.SSLServerSocket
import javax.net.ssl.SSLSocket

object SocketFactory {

    private val tlsServerSocketFactory: TLSServerSocketFactory = TLSServerSocketFactory()
    private val tlsClientSocketFactory: TLSClientSocketFactory = TLSClientSocketFactory()

    fun createTlsServerSocket(port: Int): SSLServerSocket {
        return tlsServerSocketFactory.createServerSocket(port)
    }

    fun createTlsClientSocket(host: String, port: Int): SSLSocket {
        return tlsClientSocketFactory.createClientSocket(host, port)
    }

    fun createServerSocket(port: Int): ServerSocket {
        return ServerSocket(port)
    }

    fun createClientSocket(host: String, port: Int): Socket {
        return Socket(host, port)
    }
}