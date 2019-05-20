package com.serverless.logging.metadata;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EntityLoggingContext {
    private String name;
    private String tags;
    private String id;
    private EntityType entityType;

}
