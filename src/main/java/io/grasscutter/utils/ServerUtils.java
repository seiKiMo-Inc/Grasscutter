package io.grasscutter.utils;

import io.grasscutter.proto.QueryCurrRegionHttpRspOuterClass.QueryCurrRegionHttpRsp;

import java.util.concurrent.atomic.AtomicReference;

/* Utility methods for accessing server info. */
public interface ServerUtils {
    // This server region.
    AtomicReference<QueryCurrRegionHttpRsp> CURRENT_REGION = new AtomicReference<>();

    /**
     * Performs a hash code operation on an ability hash.
     *
     * @param hash The hash to perform the operation on.
     * @return The hash code.
     */
    static int hashAbility(String hash) {
        var v7 = 0; var v8 = 0;
        while (v8 < hash.length()) {
            v7 = hash.charAt(v8++) + 131 * v7;
        }

        return v7;
    }
}
