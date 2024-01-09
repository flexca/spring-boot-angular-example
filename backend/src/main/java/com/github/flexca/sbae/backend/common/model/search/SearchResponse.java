package com.github.flexca.sbae.backend.common.model.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse<T> {

    private List<T> items;
    private int limit;
    private int offset;
    private boolean nextPage;
}
