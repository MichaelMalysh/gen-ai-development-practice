package com.epam.training.gen.ai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;

/**
 * Configuration class for setting up Qdrant components.
 * <p>
 * This configuration provides a bean necessary for the interaction with
 * Qdrant services.
 */
@Configuration
public class QdrantConfiguration {

    @Value("${qdrant-clent-host}")
    private String qdrantHost;

    @Value("${qdrant-clent-port}")
    private int qdrantPort;

    @Value("${qdrant-clent-transport-layer-security}")
    private boolean qdrantTls;

    /**
     * Creates a {@link QdrantClient} bean to communicate with Qdrant.
     *
     * @return an instance of {@link QdrantClient}
     */
    @Bean
    QdrantClient qdrantClient() {
        return new QdrantClient(
                QdrantGrpcClient.newBuilder(qdrantHost, qdrantPort, qdrantTls).build());
    }

}
