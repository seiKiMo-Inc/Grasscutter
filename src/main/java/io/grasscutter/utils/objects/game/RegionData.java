package io.grasscutter.utils.objects.game;

import io.grasscutter.proto.QueryCurrRegionHttpRspOuterClass.QueryCurrRegionHttpRsp;
import io.grasscutter.proto.RegionInfoOuterClass.RegionInfo;
import io.grasscutter.utils.EncodingUtils;
import io.grasscutter.utils.definitions.Configuration;
import io.grasscutter.utils.enums.KeyType;
import java.util.List;

/* Holds region data. */
public record RegionData(QueryCurrRegionHttpRsp regionQuery, String base64) {
    /**
     * Parses all specified config regions into {@link RegionData}s.
     *
     * @param regions The regions to parse.
     * @return A list of parsed regions.
     */
    public static List<RegionData> parse(List<Configuration.Region> regions) {
        return regions.stream().map(RegionData::parse).toList();
    }

    /**
     * Parses configuration region data into a {@link RegionData} object.
     *
     * @param regionData Configuration region data.
     * @return A {@link RegionData} object.
     */
    public static RegionData parse(Configuration.Region regionData) {
        var regionInfo =
                RegionInfo.newBuilder()
                        .setGateserverIp(regionData.address)
                        .setGateserverPort(regionData.port)
                        .setSecretKey(KeyType.DISPATCH_SEED.getProtoKey());
        var regionQuery = QueryCurrRegionHttpRsp.newBuilder().setRegionInfo(regionInfo).build();
        var encoded = EncodingUtils.toBase64(regionQuery.toByteArray());

        return new RegionData(regionQuery, new String(encoded));
    }
}
