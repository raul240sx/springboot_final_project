package rrs.ms_api_gateway.config;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

@Configuration
public class GatewayMtlsConfig {

    @Value("${eureka.client.tls.key-store}")
    private Resource keyStore;

    @Value("${eureka.client.tls.key-store-password}")
    private String keyStorePassword;

    @Value("${eureka.client.tls.trust-store}")
    private Resource trustStore;

    @Value("${eureka.client.tls.trust-store-password}")
    private String trustStorePassword;

    @Bean
    public HttpClientCustomizer mtlsHttpClientCustomizer() {
        return httpClient -> {
            try {
                KeyStore ks = KeyStore.getInstance("PKCS12");
                try (InputStream in = this.keyStore.getInputStream()) {
                    ks.load(in, this.keyStorePassword.toCharArray());
                }
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(ks, this.keyStorePassword.toCharArray());

                KeyStore ts = KeyStore.getInstance("PKCS12");
                try (InputStream in = this.trustStore.getInputStream()) {
                    ts.load(in, this.trustStorePassword.toCharArray());
                }
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(ts);
                
                SslContext sslContext = SslContextBuilder.forClient()
                    .keyManager(kmf)
                    .trustManager(tmf)
                    .build();

                return httpClient.secure(sslSpec -> sslSpec.sslContext(sslContext));
                
            } catch (Exception e) {
                throw new IllegalStateException("No se pudo configurar mTLS para el HttpClientdel gateway", e);
            }
            
        };
    }
}