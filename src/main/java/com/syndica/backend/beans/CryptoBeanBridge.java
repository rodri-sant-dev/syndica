package com.syndica.backend.beans;

import org.springframework.stereotype.Component;

@Component
public class CryptoBeanBridge {

    private static AesGcmCipher cipherInstance;

    public CryptoBeanBridge(AesGcmCipher cipher) {
        CryptoBeanBridge.cipherInstance = cipher;
    }

    public static AesGcmCipher getCipher() {
        return cipherInstance;
    }
}