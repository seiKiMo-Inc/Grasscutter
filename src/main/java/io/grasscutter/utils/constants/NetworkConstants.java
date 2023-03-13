package io.grasscutter.utils.constants;

import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.utils.enums.KeyType;
import kcp.highway.ChannelConfig;

import java.util.Set;

/* Constants seen in server networking. */
public interface NetworkConstants {
    // Magic bytes for the start and end of a packet.
    int MAGIC_1 =  17767; // 0x4567
    int MAGIC_2 = -30293; // 0x89ab

    // Packets which shouldn't be logged.
    Set<Integer> LOG_BLACKLIST = Set.of(
            PacketIds.PingReq, PacketIds.PingRsp
    );

    // Default & constant values for the dispatch server.
    String DEFAULT_REGION_DATA = "CAESGE5vdCBGb3VuZCB2ZXJzaW9uIGNvbmZpZw==";
    String REGION_QUERY_SIGNATURE = "TW9yZSBsb3ZlIGZvciBVQSBQYXRjaCBwbGF5ZXJz";
    String REGION_TYPE = "DEV_PUBLIC";
    byte[] CLIENT_CONFIG =
            KeyType.DISPATCH.xor(
                    "{\"sdkenv\":\"2\",\"checkdevice\":\"false\",\"loadPatch\":\"false\",\"showexception\":\"false\",\"regionConfig\":\"pm|fk|add\",\"downloadMode\":\"0\"}"
                            .getBytes());

    // KCP channel configuration.
    ChannelConfig KCP_CONFIG = new ChannelConfig();
}
