package com.sparta.temueats.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

public class uuidLongConverter {

    public UUID convertLongToUUID(Long userId) {
        long mostSigBits = userId;
        long leastSigBits = 0L; // 이걸 0으로 하면 uuid 를 Long 으로 바꿔도 똑같지 않을까?
        return new UUID(mostSigBits, leastSigBits);
    }

    public Long convertUUIDToLong(UUID uuid) {
        return uuid.getMostSignificantBits();
    }
}
