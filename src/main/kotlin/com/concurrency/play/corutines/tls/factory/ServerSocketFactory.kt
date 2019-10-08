package com.concurrency.play.corutines.tls.factory

import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLServerSocket
import javax.net.ssl.SSLServerSocketFactory
import javax.net.ssl.TrustManagerFactory

class ServerSocketFactory {

    private val TLS_VERSION = "TLSv1.2"

    private val sslServerFactory: SSLServerSocketFactory = createServerFactory()

    fun createServerSocket(port: Int): SSLServerSocket {
        return sslServerFactory.createServerSocket(port) as SSLServerSocket
    }

    private fun createServerFactory(): SSLServerSocketFactory {
        val context = SSLContext.getInstance(TLS_VERSION)

        val keyStore = createKeyStore()
        val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        keyManagerFactory.init(keyStore, "".toCharArray())

        val trustStore = createTrustStore()
        val trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(trustStore)

        context.init(keyManagerFactory.keyManagers, trustManagerFactory.trustManagers, null)

        return context.serverSocketFactory
    }

    private fun createKeyStore(): KeyStore {
        return KeyStore.getInstance("todo")
    }

    private fun createTrustStore(): KeyStore {
        return KeyStore.getInstance("todo")
    }
}
