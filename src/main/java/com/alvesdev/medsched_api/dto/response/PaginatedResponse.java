package com.alvesdev.medsched_api.dto.response;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;

public record PaginatedResponse<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) implements Serializable {
    
    public PaginatedResponse(Page<T> page) {
        this(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
}
