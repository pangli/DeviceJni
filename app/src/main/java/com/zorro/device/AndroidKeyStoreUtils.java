package com.zorro.device;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import android.util.Base64;

/**
 * @Author PangLi
 * @Date 2024/5/29 10:58
 * @Description AndroidKeyStoreUtils
 */
public class AndroidKeyStoreUtils {
    private static final String ANDROID_KEY_STORE_PROVIDER = "AndroidKeyStore";
    private static final String ANDROID_KEY_STORE_ALIAS = "AES_KEY";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private byte[] iv;
    private static final int TAG_SIZE = 128;

    public String encrypt(String content) {
        try {
            SecretKey secretKey = getSecretKey();
            if (secretKey == null) return content;
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            iv = cipher.getIV();
            byte[] encryption = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(encryption, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return content;
    }

    public String decrypt(String content) {
        try {
            SecretKey secretKey = getSecretKey();
            if (secretKey == null || iv == null) return content;
            byte[] decodedMessage = Base64.decode(content, Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_SIZE, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            byte[] decrypted = cipher.doFinal(decodedMessage);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 BadPaddingException | IllegalBlockSizeException |
                 InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return content;
    }

    private SecretKey getSecretKey() {
        SecretKey secretKey = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE_PROVIDER);
            keyStore.load(null);
            if (!keyStore.containsAlias(ANDROID_KEY_STORE_ALIAS)) {
                KeyGenParameterSpec keySpec = new KeyGenParameterSpec.Builder(
                        ANDROID_KEY_STORE_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setRandomizedEncryptionRequired(true)
                        .build();
                KeyGenerator aesKeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
                        ANDROID_KEY_STORE_PROVIDER);
                aesKeyGenerator.init(keySpec);
                secretKey = aesKeyGenerator.generateKey();
            } else {
                KeyStore.SecretKeyEntry keyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(ANDROID_KEY_STORE_ALIAS, null);
                secretKey = keyEntry.getSecretKey();
            }
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException |
                 NoSuchProviderException | InvalidAlgorithmParameterException |
                 UnrecoverableEntryException e) {
            e.printStackTrace();
        }
        return secretKey;
    }
}
