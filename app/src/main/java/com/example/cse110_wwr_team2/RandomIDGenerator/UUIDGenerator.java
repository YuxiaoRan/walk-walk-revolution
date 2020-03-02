package com.example.cse110_wwr_team2.RandomIDGenerator;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class UUIDGenerator {
    /**
     * from: https://stackoverflow.com/questions/772802/storing-uuid-as-base64-string/18057117#18057117
     * shorten UUID
     */
    private static final Base64.Encoder BASE64_URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    public static String uuidHexToUuid64(String uuidStr) {
        UUID uuid = UUID.fromString(uuidStr);
        byte[] bytes = uuidToBytes(uuid);
        return BASE64_URL_ENCODER.encodeToString(bytes);
    }

    public static byte[] uuidToBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
}
