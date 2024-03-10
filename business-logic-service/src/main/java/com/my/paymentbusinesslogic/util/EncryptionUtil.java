package com.my.paymentbusinesslogic.util;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class for encryption-related functionality.
 */
@Component
public class EncryptionUtil {
  @Value("${encryption.secret-key}")
  private String secretKey;

  /**
   * Encrypts the provided plain text using the configured secret key.
   *
   * @param plainText The text to be encrypted.
   * @return The encrypted text.
   */
  public String encrypt(String plainText) {
    BasicTextEncryptor encryptor = new BasicTextEncryptor();
    encryptor.setPassword(secretKey);
    return encryptor.encrypt(plainText);
  }

  /**
   * Decrypts the provided encrypted text using the configured secret key.
   *
   * @param encryptedText The text to be decrypted.
   * @return The decrypted text.
   */
  public String decrypt(String encryptedText) {
    BasicTextEncryptor encryptor = new BasicTextEncryptor();
    encryptor.setPassword(secretKey);
    return encryptor.decrypt(encryptedText);
  }
}
