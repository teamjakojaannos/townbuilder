package jakojaannos.townbuilder.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;

/**
 * Utilities for reading and writing values on {@link PacketBuffer PacketBuffers}
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PacketBufferHelper {
    /**
     * Encodes given {@link Vec3d} to the packet buffer as three doubles.
     *
     * @param buffer buffer to encode to
     * @param value  value to encode
     */
    public static void encodeVec3d(PacketBuffer buffer, Vec3d value) {
        buffer.writeDouble(value.x);
        buffer.writeDouble(value.y);
        buffer.writeDouble(value.z);
    }

    /**
     * Reads a {@link Vec3d} from a packet buffer. Expects value to be encoded via {@link #encodeVec3d(PacketBuffer,
     * Vec3d)}
     *
     * @param buffer buffer to read the value from
     * @return the value to read
     */
    public static Vec3d decodeVec3d(PacketBuffer buffer) {
        return new Vec3d(buffer.readDouble(),
                         buffer.readDouble(),
                         buffer.readDouble());
    }
}
