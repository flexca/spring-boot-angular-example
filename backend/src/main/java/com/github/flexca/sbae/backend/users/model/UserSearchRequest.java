package com.github.flexca.sbae.backend.users.model;

import com.github.flexca.sbae.backend.common.model.search.OrderDirection;
import lombok.Data;

@Data
public class UserSearchRequest {

    private String organizationId;
    private String id;
    private String email;
    private String name;
    private String createdFrom;
    private String createdTo;
    private String status;

    private int limit = 20;
    private int offset = 0;

    private String orderBy;
    private OrderDirection orderDirection = OrderDirection.DESC;
}
