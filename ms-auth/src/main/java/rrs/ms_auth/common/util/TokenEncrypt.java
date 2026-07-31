package rrs.ms_auth.common.util;

import org.springframework.stereotype.Component;

import rrs.ms_auth.common.exception.BusinessRuleException;



@Component
public class TokenEncrypt {

    public String hashToken(String token) {
    try {
        java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(token.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    } catch (java.security.NoSuchAlgorithmException e) {
        throw new BusinessRuleException("Error crítico al inicializar el algoritmo criptográfico");
    }
}

}
