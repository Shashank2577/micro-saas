package com.crosscutting.starter.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EncryptionServiceTest {

    private EncryptionService encryptionService;

    // 64 hex chars = 32 bytes = AES-256
    private static final String TEST_KEY = "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef";

    @BeforeEach
    void setUp() {
        encryptionService = new EncryptionService(TEST_KEY);
    }

    @Test
    void encryptThenDecrypt_returnsOriginalPlaintext() {
        String plaintext = "Hello, World! This is a secret message.";

        String encrypted = encryptionService.encrypt(plaintext);
        String decrypted = encryptionService.decrypt(encrypted);

        assertThat(decrypted).isEqualTo(plaintext);
    }

    @Test
    void differentPlaintexts_produceDifferentCiphertexts() {
        String plaintext1 = "message one";
        String plaintext2 = "message two";

        String encrypted1 = encryptionService.encrypt(plaintext1);
        String encrypted2 = encryptionService.encrypt(plaintext2);

        assertThat(encrypted1).isNotEqualTo(encrypted2);
    }

    @Test
    void samePlaintext_producesDifferentCiphertexts_dueToRandomIV() {
        String plaintext = "same message";

        String encrypted1 = encryptionService.encrypt(plaintext);
        String encrypted2 = encryptionService.encrypt(plaintext);

        assertThat(encrypted1).isNotEqualTo(encrypted2);

        // But both decrypt to the same plaintext
        assertThat(encryptionService.decrypt(encrypted1)).isEqualTo(plaintext);
        assertThat(encryptionService.decrypt(encrypted2)).isEqualTo(plaintext);
    }

    @Test
    void handlesEmptyString() {
        String encrypted = encryptionService.encrypt("");
        String decrypted = encryptionService.decrypt(encrypted);

        assertThat(decrypted).isEmpty();
    }

    @Test
    void handlesUnicodeCharacters() {
        String plaintext = "Hello, \u4e16\u754c! \ud83c\udf0d";

        String encrypted = encryptionService.encrypt(plaintext);
        String decrypted = encryptionService.decrypt(encrypted);

        assertThat(decrypted).isEqualTo(plaintext);
    }
}
