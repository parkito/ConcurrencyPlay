package com.concurrency.play.corutines.tls.factory

import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory

class TLSClientSocketFactory {

    private val sslServerFactory: SSLSocketFactory = createClientFactory()

    fun createClientSocket(host: String, port: Int): SSLSocket {
        return sslServerFactory.createSocket(host, port) as SSLSocket
    }

    private fun createClientFactory(): SSLSocketFactory {
        val context = SSLContext.getInstance(TLS_VERSION)

        val keyStore = createKeyStore()
        val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        keyManagerFactory.init(keyStore, "".toCharArray())

        val trustStore = createTrustStore()
        val trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(trustStore)

        context.init(keyManagerFactory.keyManagers, trustManagerFactory.trustManagers, null)

        return context.socketFactory
    }

    private fun createKeyStore(): KeyStore {
        return KeyStore.getInstance("todo")
    }

    private fun createTrustStore(): KeyStore {
        return KeyStore.getInstance("todo")
    }
}