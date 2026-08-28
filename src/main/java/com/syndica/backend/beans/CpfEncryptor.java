package com.syndica.backend.beans;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CpfEncryptor implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String cpf) {
        if (cpf == null) return null;
        return CryptoBeanBridge.getCipher().encrypt(cpf);
    }

    @Override
    public String convertToEntityAttribute(String cpfCriptografado) {
        if (cpfCriptografado == null) return null;
        return CryptoBeanBridge.getCipher().decrypt(cpfCriptografado);
    }
}