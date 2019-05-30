package com.example.demo.helpers;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;

import java.security.KeyPair;
import java.security.MessageDigest;

public final class KeypairHelper {

    private static final String MD5_HASH = "MD5";
    private static final String SIGNATURE = "Ed25519";

    /**
     * generates EdDSA keypair to sign and verify transactions randomly
     * @return KeyPair
     */
    public static KeyPair generateRandomKeys() {
        KeyPairGenerator generator = new KeyPairGenerator();
        KeyPair keyPair = generator.generateKeyPair();
        return keyPair;
    }

    /**
     * get EdDSA keypair to sign and verify transactions from input
     * @return KeyPair
     */
    public static KeyPair getKeyPairFromInput(String input) throws Exception {
        EdDSAParameterSpec ed25519 = EdDSANamedCurveTable.getByName(SIGNATURE);
        String hashInput = convertStringToHash(input, MD5_HASH);
        byte[] seed = hashInput.getBytes();

        System.out.println("Hash of input: " + hashInput);
        System.out.println("String bytes: " + input.getBytes().length);
        System.out.println("Seed: " + seed.length);

        EdDSAPrivateKeySpec privKey = new EdDSAPrivateKeySpec(seed, ed25519);
        EdDSAPublicKeySpec pubKey = new EdDSAPublicKeySpec(privKey.getA(), ed25519);

        return new KeyPair(new EdDSAPublicKey(pubKey), new EdDSAPrivateKey(privKey));
    }

    /**
     * Convert string to hash
     * @return String of hash with length of 32
     */
    private static String convertStringToHash(String src, String hashType) throws Exception {
        StringBuilder sb = new StringBuilder();
        MessageDigest digestMD5 = MessageDigest.getInstance(hashType);
        byte[] hashBytes = digestMD5.digest(src.getBytes());

        for (byte hashByte : hashBytes) {
            sb.append(String.format("%02x", hashByte));
        }
        return sb.toString();
    }
}
