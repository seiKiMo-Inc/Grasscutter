package io.grasscutter.network.packets.notify.scene;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Player;
import io.grasscutter.proto.SceneAreaWeatherNotifyOuterClass.SceneAreaWeatherNotify;
import lombok.AllArgsConstructor;

/** The weather data of the scene. {@link PacketIds#SceneAreaWeatherNotify}. */
@AllArgsConstructor
public final class SceneAreaWeather extends BasePacket<Empty, SceneAreaWeatherNotify> {
    private final Player player;

    @Override
    public SceneAreaWeatherNotify preparePacket() {
        return SceneAreaWeatherNotify.newBuilder()
                .setWeatherAreaId(this.player.getWeatherId())
                .setClimateType(this.player.getClimate().getValue())
                .build();
    }
}
