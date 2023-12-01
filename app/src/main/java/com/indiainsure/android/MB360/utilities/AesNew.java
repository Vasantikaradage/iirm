package com.indiainsure.android.MB360.utilities;


import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Base64;


public class AesNew extends AbstractAes256
{
    /**
     * Encrypt text with the passphrase
     * @param input Input text to encrypt
     * @param passphrase The passphrase
     * @return A base64 encoded string containing the encrypted data
     * @throws Exception Throws exceptions
     */
    public static String encrypt(String input, String passphrase) throws Exception {
        return Base64.getEncoder().encodeToString(_encrypt(input.getBytes(UTF_8), passphrase.getBytes(UTF_8)));
    }

    /**
     * Encrypt text in bytes with the passphrase
     * @param input Input data in bytes to encrypt
     * @param passphrase The passphrase in bytes
     * @return A base64 encoded bytes containing the encrypted data
     * @throws Exception Throws exceptions
     */
    public static byte[] encrypt(byte[] input, byte[] passphrase) throws Exception {
        return Base64.getEncoder().encode(_encrypt(input, passphrase));
    }

    /**
     * Decrypt encrypted base64 encoded text in bytes
     * @param crypted Text in bytes to decrypt
     * @param passphrase The passphrase in bytes
     * @return Decrypted data in bytes
     * @throws Exception Throws exceptions
     */
    public static String decrypt(String crypted, String passphrase) throws Exception {
        return new String(_decrypt(Base64.getDecoder().decode(crypted), passphrase.getBytes(UTF_8)), UTF_8);
    }

    /**
     * Decrypt encrypted base64 encoded text in bytes
     * @param crypted Text in bytes to decrypt
     * @param passphrase The passphrase in bytes
     * @return Decrypted data in bytes
     * @throws Exception Throws exceptions
     */
    public static byte[] decrypt(byte[] crypted, byte[] passphrase) throws Exception {
        return _decrypt(Base64.getDecoder().decode(crypted), passphrase);
    }
}
