package com.serverless.logging.metadata;

import lombok.Data;

@Data
public class EntityInfo {
    private int index=-1;
    private String[] tags;
    private EntityType type;
    private String name;
}
