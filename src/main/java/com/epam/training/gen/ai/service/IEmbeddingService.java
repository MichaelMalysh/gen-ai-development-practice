package com.epam.training.gen.ai.service;

import com.azure.ai.openai.models.Embeddings;
import com.google.common.util.concurrent.ListenableFuture;
import io.qdrant.client.grpc.Points;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IEmbeddingService {

    Mono<Embeddings> retrieveEmbeddings(String text);

    ListenableFuture<Points.UpdateResult> createEmbeddings(String text) throws ExecutionException, InterruptedException;

    List<String> searchClosest(String text) throws ExecutionException, InterruptedException;


}
