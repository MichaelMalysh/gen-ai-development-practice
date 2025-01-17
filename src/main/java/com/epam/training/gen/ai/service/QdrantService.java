package com.epam.training.gen.ai.service;

import com.google.common.util.concurrent.ListenableFuture;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;
import io.qdrant.client.grpc.JsonWithInt;
import io.qdrant.client.grpc.Points;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static io.qdrant.client.PointIdFactory.id;
import static io.qdrant.client.VectorsFactory.vectors;
import static io.qdrant.client.WithPayloadSelectorFactory.enable;

/**
 * Service for working with Qdrant.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class QdrantService implements IQdrantService {

    private static final int DEFAULT_COLLECTION_SIZE = 1536;
    private static final Collections.Distance DEFAULT_COLLECTION_DISTANCE = Collections.Distance.Manhattan;


    private final QdrantClient qdrantClient;

    @Value("${qdrant-embedding-collection-name}")
    private String qdrantEmbeddingCollectionName;


    /**
     * Saves vector to Qdrant.
     *
     * @param collectionName collection name
     * @param input          input
     * @param embeddings     embeddings
     * @return update result
     * @throws ExecutionException   exception
     * @throws InterruptedException exception
     */
    @Override
    public ListenableFuture<Points.UpdateResult> saveVector(String collectionName,
                                                            String input,
                                                            List<List<Float>> embeddings)
            throws ExecutionException, InterruptedException {
        if (isCollectionNotExists(qdrantEmbeddingCollectionName)) {
            createCollection(qdrantEmbeddingCollectionName);
        }

        List<Points.PointStruct> pointStructs = createPointStructsBasedOnUserInput(input, embeddings);

        return qdrantClient.upsertAsync(collectionName, pointStructs);
    }

    /**
     * Searches vector in Qdrant.
     *
     * @param collectionName collection name
     * @param embeddings     embeddings
     * @return list of strings
     * @throws ExecutionException   exception
     * @throws InterruptedException exception
     */
    @Override
    public List<String> searchVector(String collectionName, List<Float> embeddings) throws ExecutionException, InterruptedException {
        List<Points.ScoredPoint> scoredPoints = qdrantClient
                .searchAsync(buildSearchPoints(collectionName, embeddings))
                .get();

        return scoredPoints.stream()
                .filter(point -> point.containsPayload("input"))
                .map(point -> point.getPayloadMap().get("input").getStringValue())
                .toList();
    }

    /**
     * Creates collection in Qdrant.
     *
     * @param collectionName collection name
     * @throws InterruptedException   exception
     * @throws ExecutionException exception
     */
    @Override
    public void createCollection(String collectionName) throws InterruptedException, ExecutionException {
        if (isCollectionNotExists(collectionName)) {
            createAndLogCollection(collectionName);
        } else {
            log.info("Collection with name '{}' already exists.", collectionName);
        }
    }

    /**
     * Creates point structs based on user input.
     *
     * @param input      input
     * @param embeddings embeddings
     * @return list of point structs
     */
    private List<Points.PointStruct> createPointStructsBasedOnUserInput(String input, List<List<Float>> embeddings) {
        return embeddings.stream()
                .map(point -> buildPointStruct(point, input))
                .collect(Collectors.toList());
    }

    /**
     * Creates and logs collection.
     *
     * @param collectionName collection name
     * @throws InterruptedException   exception
     * @throws ExecutionException exception
     */
    private void createAndLogCollection(String collectionName) throws InterruptedException, ExecutionException {
        Collections.VectorParams vectorParams = buildVectorParams();
        qdrantClient.createCollectionAsync(collectionName, vectorParams).get();
        logCollectionCreationInfo(collectionName);
    }

    /**
     * Builds vector params.
     *
     * @return vector params
     */
    private Collections.VectorParams buildVectorParams() {
        return Collections.VectorParams.newBuilder()
                .setDistance(QdrantService.DEFAULT_COLLECTION_DISTANCE)
                .setSize(QdrantService.DEFAULT_COLLECTION_SIZE)
                .build();
    }

    /**
     * Logs collection creation info.
     *
     * @param collectionName collection name
     */
    private void logCollectionCreationInfo(String collectionName) {
        log.info("Collection created with name: {}, size: {} and distance: {}",
                collectionName,
                QdrantService.DEFAULT_COLLECTION_SIZE,
                QdrantService.DEFAULT_COLLECTION_DISTANCE);
    }

    /**
     * Checks if collection not exists.
     *
     * @param collectionName collection name
     * @return boolean
     * @throws InterruptedException   exception
     * @throws ExecutionException exception
     */
    private boolean isCollectionNotExists(String collectionName) throws InterruptedException, ExecutionException {
        List<String> collections = qdrantClient.listCollectionsAsync().get();

        return collections.stream().noneMatch(collection -> collection.contains(collectionName));
    }

    /**
     * Builds point struct that is required for creating or updating a vector.
     *
     * @param points vector coordinates
     * @param input input
     * @return point struct
     */
    private Points.PointStruct buildPointStruct(List<Float> points, String input) {
        return Points.PointStruct.newBuilder().setId(id(UUID.randomUUID())).setVectors(vectors(points))
                .putPayload("input", JsonWithInt.Value.newBuilder().setStringValue(input).build()).build();
    }

    /**
     * Builds search points.
     *
     * @param collectionName collection name
     * @param embeddings     embeddings
     * @return search points
     */
    private Points.SearchPoints buildSearchPoints(String collectionName, List<Float> embeddings) {
        return Points.SearchPoints.newBuilder().setCollectionName(collectionName).addAllVector(embeddings)
                .setWithPayload(enable(true)).setLimit(1).build();
    }
}
