package com.epam.training.gen.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * A DTO class for the response format.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseFormat {

    private String input;
    private List<String> books;
}
