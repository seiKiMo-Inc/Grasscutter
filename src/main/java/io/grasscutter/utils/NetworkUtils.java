package io.grasscutter.utils;

import io.grasscutter.network.packets.GenericPacket;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.Packet;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.utils.definitions.Configuration;
import io.grasscutter.utils.objects.Triplet;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/* Utility methods seen in server networking. */
public interface NetworkUtils {
    Map<Integer, String> PACKET_NAMES = NetworkUtils.loadPacketNames();
    Map<Class<? extends BasePacket<?, ?>>, Integer> PACKET_IDS = NetworkUtils.loadPacketIds();

    /**
     * Generates bindings from packet IDs -> packet names.
     * @return A map of packet IDs -> packet names.
     */
    static Map<Integer, String> loadPacketNames() {
        var map = new HashMap<Integer, String>();

        // Read the packet IDs from the PacketIds class.
        var constants = PacketIds.class.getFields();
        for (var constant : constants) {
            try {
                // Read the value and the name.
                var value = constant.getInt(null);
                var name = constant.getName();
                map.put(value, name);
            } catch (IllegalAccessException ignored) { }
        }

        return map;
    }

    /**
     * Generates bindings from packet classes -> packet IDs.
     * @return A map of packet classes -> packet IDs.
     */
    static Map<Class<? extends BasePacket<?, ?>>, Integer> loadPacketIds() {
        var map = new HashMap<Class<? extends BasePacket<?, ?>>, Integer>();

        // Read the packet IDs from the PacketIds class.
        var constants = PacketIds.class.getFields();
        for (var constant : constants) {
            try {
                // Read the value and the name.
                var value = constant.getInt(null);
                var name = constant.getName();

                // Clean the name.
                name = name.replace("Req", "")
                        .replace("Rsp", "")
                        .replace("Notify", "");
                var packet = Packet.fromPacketName(name).getPacket();
                map.put(packet, value);
            } catch (IllegalAccessException | IllegalArgumentException ignored) { }
        }

        return map;
    }

    /**
     * Identifies the packet ID for sending the packet.
     *
     * @param packet The packet instance.
     * @return A packet ID in {@link PacketIds}.
     */
    @SuppressWarnings("unchecked")
    static int getIdOf(BasePacket<?, ?> packet) {
        // Try to fetch the packet ID via the packet instance.
        if (packet instanceof GenericPacket generic) {
            return generic.getPacketId();
        } else {
            // Try to fetch the packet ID via the class.
            var packetClass = (Class<? extends BasePacket<?, ?>>) packet.getClass();
            return PACKET_IDS.getOrDefault(packetClass, -1);
        }
    }

    /**
     * Identifies the packet name from a packet ID.
     *
     * @param packetId The packet ID.
     * @return The packet name.
     */
    static String getNameOf(int packetId) {
        return PACKET_NAMES.getOrDefault(packetId, "unknown");
    }

    /**
     * Creates an {@link InetSocketAddress} from the given host and port.
     *
     * @param address The address to parse.
     * @param port The port to parse.
     * @return The parsed address.
     */
    static InetSocketAddress createFrom(String address, int port) {
        return new InetSocketAddress(address, port);
    }

    /**
     * Validates a given host and port.
     *
     * @param address The address to validate.
     * @param port The port to validate.
     * @return Whether the address and port are valid.
     */
    static boolean validate(String address, int port) {
        return address != null && !address.isEmpty() && port > 0 && port < 65536;
    }

    /**
     * Formulates a valid URL based on the dispatch configuration.
     *
     * @param dispatchData The dispatch configuration.
     * @param httpData The HTTP configuration.
     * @return A URL.
     */
    static String formulateUrl(Configuration.Dispatch dispatchData, Configuration.Http httpData) {
        var address = dispatchData.routingAddress;
        var port = dispatchData.routingPort;

        if (!NetworkUtils.validate(address, port)) {
            // Set fallbacks.
            address = "127.0.0.1";
            port = httpData.bindPort;
        }

        return String.format(
                "%s://%s:%d",
                dispatchData.routingEncryption ? "https" : "http",
                address.isEmpty() ? "127.0.0.1" : address, // Fallback to localhost.
                dispatchData.routingPort == -1 ? httpData.bindPort : port);
    }

    /**
     * Extracts the protocol version from a version number.
     *
     * @param versionName The version code.
     * @return The protocol version.
     */
    static Triplet<Short, Short, Short> getProtocolVersion(String versionName) {
        var versionCode =
                versionName.replaceAll(Pattern.compile("[a-zA-Z]").pattern(), "").split("\\.");
        var versionMajor = Integer.parseInt(versionCode[0]);
        var versionMinor = Integer.parseInt(versionCode[1]);
        var versionFix = Integer.parseInt(versionCode[2]);

        return new Triplet<>((short) versionMajor, (short) versionMinor, (short) versionFix);
    }

    /**
     * Checks if the version requires RSA signing.
     *
     * @param version The version to check.
     * @return Whether the version requires RSA signing.
     */
    static boolean requiresSigning(Triplet<Short, Short, Short> version) {
        // Get the version numbers.
        var versionMajor = version.a();
        var versionMinor = version.b();
        var versionFix = version.c();

        return versionMajor >= 3
                || (versionMajor == 2 && versionMinor == 7 && versionFix >= 50)
                || (versionMajor == 2 && versionMinor == 8);
    }
}
