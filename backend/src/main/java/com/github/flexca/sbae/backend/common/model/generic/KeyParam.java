package com.github.flexca.sbae.backend.common.model.generic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum KeyParam {

    RSA_2048(KeyType.RSA, 2048, null),
    RSA_4096(KeyType.RSA, 4096, null),
    P_256(KeyType.EC, 256, "secp256r1");

    @Getter
    private final KeyType keyType;

    @Getter
    private final int size;

    @Getter
    private final String curve;
}
