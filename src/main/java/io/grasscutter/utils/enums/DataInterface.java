package io.grasscutter.utils.enums;

import io.grasscutter.data.impl.MongoDBInterface;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DataInterface {
    MONGODB(MongoDBInterface.class);

    final Class<? extends io.grasscutter.data.DataInterface> interfaceClass;
}
