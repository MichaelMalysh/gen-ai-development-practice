package com.epam.training.gen.ai.service;

import com.google.common.util.concurrent.ListenableFuture;
import io.qdrant.client.grpc.Points;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IQdrantService {

    ListenableFuture<Points.UpdateResult> saveVector(String collectionName, String input, List<List<Float>> embeddings) throws ExecutionException, InterruptedException;

    List<String> searchVector(String collectionName, List<Float> embeddings) throws ExecutionException, InterruptedException;

    void createCollection(String collectionName) throws InterruptedException, ExecutionException;
}
