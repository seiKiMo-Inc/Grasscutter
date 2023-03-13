package io.grasscutter.network.packets.notify.data;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Player;
import io.grasscutter.proto.AvatarDataNotifyOuterClass.AvatarDataNotify;
import lombok.AllArgsConstructor;

import java.util.List;

/** Avatar data packet. {@link PacketIds#AvatarDataNotify}. */
@AllArgsConstructor
public final class AvatarData extends BasePacket<Empty, AvatarDataNotify> {
    private final Player player;

    @Override
    public AvatarDataNotify preparePacket() {
        this.buildHeader();

        var teams = this.player.getTeams();
        var packet = AvatarDataNotify.newBuilder()
                .setCurAvatarTeamId(teams.getSelectedTeam())
                .setChooseAvatarGuid(teams.getSelectedAvatarGuid())
                .addAllOwnedFlycloakList(List.of(140001)) // TODO: Move this to the player.
                .addAllOwnedCostumeList(List.of());

        // Add the player's avatars.
        player.getAvatars().forEach(avatar ->
                packet.addAvatarList(avatar.toProto()));
        // Add the player's teams.
        player.getTeams().forEach((id, team) -> {
            packet.putAvatarTeamMap(id, team.toProto(player));

            // Add custom teams to the backup list.
            if (id > 4) packet.addBackupAvatarTeamOrderList(id);
        });

        // Set the player's selected main character.
        var mainCharacter = player.getAvatars().get(
                player.getMainCharacter());
        if (mainCharacter != null) {
            packet.setChooseAvatarGuid(mainCharacter.getGuid());
        }

        return packet.build();
    }
}
