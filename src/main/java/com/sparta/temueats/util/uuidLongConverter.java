package com.sparta.temueats.util;

import jakarta.persistence.AttributeConverter;

import java.util.UUID;

public class uuidLongConverter implements AttributeConverter<UUID, Long> {

    @Override
    public UUID convertToEntityAttribute(Long userId) {
        long mostSigBits = userId;
        long leastSigBits = 0L; // 이걸 0으로 하면 uuid 를 Long 으로 바꿔도 똑같지 않을까?
        return new UUID(mostSigBits, leastSigBits);
    }

    @Override
    public Long convertToDatabaseColumn(UUID uuid) {
        return uuid.getMostSignificantBits();
    }
}
