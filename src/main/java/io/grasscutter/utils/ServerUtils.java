package io.grasscutter.utils;

import io.grasscutter.proto.QueryCurrRegionHttpRspOuterClass.QueryCurrRegionHttpRsp;

import java.util.concurrent.atomic.AtomicReference;

/* Utility methods for accessing server info. */
public interface ServerUtils {
    // This server region.
    AtomicReference<QueryCurrRegionHttpRsp> CURRENT_REGION = new AtomicReference<>();
}
