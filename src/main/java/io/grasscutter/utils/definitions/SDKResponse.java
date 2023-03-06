package io.grasscutter.utils.definitions;

import io.grasscutter.utils.EncodingUtils;
import lombok.Builder;

/* The response used for SDK (API) requests. */
@Builder
public final class SDKResponse {
    private final int retcode;
    private final String message;
    private final Object data;

    @Override
    public String toString() {
        return EncodingUtils.toJson(this);
    }
}
