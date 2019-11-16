package com.concurrency.play.net.tls.factory

import java.io.FileInputStream
import java.security.KeyStore
import javax.net.ssl.*

class TLSClientSocketFactory {

    private val sslServerFactory: SSLSocketFactory = createClientFactory()

    fun createClientSocket(host: String, port: Int): SSLSocket {
        return sslServerFactory.createSocket(host, port) as SSLSocket
    }

    private fun createClientFactory(): SSLSocketFactory {
        val context = SSLContext.getInstance(FactoryConstants.TLS_VERSION)

        val keyStore = createKeyStore()
        val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        keyManagerFactory.init(keyStore, "password".toCharArray())

        val trustStore = createKeyStore()
        val trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(trustStore)

        context.init(keyManagerFactory.keyManagers, trustManagerFactory.trustManagers, null)

        return context.socketFactory
    }

    private fun createKeyStore(): KeyStore {
        val store = KeyStore.getInstance("PKCS12")
        val fis = FileInputStream("/home/artyom.karnov/work/bids/ConcurrencyPlay/src/main/resources/keystore.jks")
        store.load(fis, "password".toCharArray())

        return store
    }

    private fun createTrustStore(): KeyStore {
        return KeyStore.getInstance("todo")
    }
}