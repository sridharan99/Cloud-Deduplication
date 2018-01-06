
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

class Encryption {
	private static Cipher cipher = null;

	public static void main(String[] args) throws Exception {

		// uncomment the following line to add the Provider of choice
		//Security.addProvider(new com.sun.crypto.provider.SunJCE());

		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		

		keyGenerator.init(128);
		SecretKey secretKey = keyGenerator.generateKey();
		cipher = Cipher.getInstance("AES");

		String plainText = "Java Cryptography Extension";
		System.out.println("Plain Text Before Encryption: " + plainText);

		byte[] plainTextByte = plainText.getBytes("UTF8");
		byte[] encryptedBytes = encrypt(plainTextByte, secretKey);
		System.out.println(secretKey);

		String encryptedText = new String(encryptedBytes, "UTF8");
		System.out.println("Encrypted Text After Encryption: " + encryptedText);

		byte[] decryptedBytes = decrypt(encryptedBytes, secretKey);
		String decryptedText = new String(decryptedBytes, "UTF8");
		System.out.println("Decrypted Text After Decryption: " + decryptedText);
	}

	static byte[] encrypt(byte[] plainTextByte, SecretKey secretKey)
			throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptedBytes = cipher.doFinal(plainTextByte);
		return encryptedBytes;
	}

	static byte[] decrypt(byte[] encryptedBytes, SecretKey secretKey)
			throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
		return decryptedBytes;
	}
}