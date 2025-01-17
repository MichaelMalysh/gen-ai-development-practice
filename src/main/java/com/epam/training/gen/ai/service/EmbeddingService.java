package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.EmbeddingItem;
import com.azure.ai.openai.models.Embeddings;
import com.azure.ai.openai.models.EmbeddingsOptions;
import com.google.common.util.concurrent.ListenableFuture;
import io.qdrant.client.grpc.Points;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Service for working with embeddings.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmbeddingService implements IEmbeddingService {

    private final QdrantService qdrantService;
    private final OpenAIAsyncClient openAIClient;

    @Value("${azureopenai-embedding-deployment-name}")
    private String azureOpenAIEmbeddingDeploymentName;
    @Value("${qdrant-embedding-collection-name}")
    private String qdrantEmbeddingCollectionName;

    /**
     * Retrieves embeddings from Azure OpenAI.
     *
     * @param text text
     * @return embeddings
     */
    @Override
    public Mono<Embeddings> retrieveEmbeddings(String text) {
        return openAIClient.getEmbeddings(azureOpenAIEmbeddingDeploymentName, new EmbeddingsOptions(List.of(text)));
    }

    /**
     * Creates embeddings in Qdrant.
     *
     * @param text text
     * @return update result
     * @throws ExecutionException   exception
     * @throws InterruptedException exception
     */
    @Override
    public ListenableFuture<Points.UpdateResult> createEmbeddings(String text) throws ExecutionException, InterruptedException {
        return qdrantService.saveVector(qdrantEmbeddingCollectionName, text, retrieveEmbeddingsBlock(text));
    }

    /**
     * Searches for the closest embeddings.
     *
     * @param text text
     * @return closest embeddings
     * @throws ExecutionException   exception
     * @throws InterruptedException exception
     */
    @Override
    public List<String> searchClosest(String text) throws ExecutionException, InterruptedException {
        List<Float> embeddings = retrieveEmbeddingsBlock(text).stream().flatMap(Collection::stream).toList();
        return qdrantService.searchVector(qdrantEmbeddingCollectionName, embeddings);
    }

    @Override
    public String createDocument(String fileContent) {
        Mono<Embeddings> embeddingsMono = retrieveEmbeddings(fileContent);
        Disposable disposable = embeddingsMono.subscribe(embeddings -> {
            List<List<Float>> embeddingsBlock = embeddings.getData().stream()
                    .map(EmbeddingItem::getEmbedding)
                    .toList();
            try {
                qdrantService.saveVector(qdrantEmbeddingCollectionName, fileContent, embeddingsBlock);
            }catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return "File processing " + String.valueOf(disposable.isDisposed() ? "completed" : "yet to be completed");
    }

    /**
     * Retrieves embeddings block.
     *
     * @param input input
     * @return list of lists of floats
     */
    private List<List<Float>> retrieveEmbeddingsBlock(String input) {
        List<EmbeddingItem> embeddingsItems = getEmbeddingsItems(input);
        return convertToFloatList(embeddingsItems);
    }

    /**
     * Retrieves embeddings.
     *
     * @param input input
     * @return list of embedding items
     */
    private List<EmbeddingItem> getEmbeddingsItems(String input) {
        try {
            var embeddings = retrieveEmbeddings(input);
            return embeddings.block().getData();
        } catch (Exception e) {
            log.error("Error retrieving embeddings for input: {}", input, e);
            return Collections.emptyList();
        }
    }

    /**
     * Converts list of embedding items to list of lists of floats.
     *
     * @param embeddingItems list of embedding items
     * @return list of lists of floats
     */
    private List<List<Float>> convertToFloatList(List<EmbeddingItem> embeddingItems) {
        return embeddingItems.stream()
                .map(EmbeddingItem::getEmbedding)
                .collect(Collectors.toList());
    }
}
