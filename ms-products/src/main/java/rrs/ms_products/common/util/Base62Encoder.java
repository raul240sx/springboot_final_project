package rrs.ms_products.common.util;

import org.springframework.stereotype.Component;


@Component
public class Base62Encoder {
    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateCode(Long id) {

        if (id == 0) return "0";

        StringBuilder encodedId = new StringBuilder();
        while (id > 0) {
            int remainder = (int) (id % 62);
            encodedId.append(BASE62_CHARS.charAt(remainder));
            id = id / 62;
        }

        return encodedId.reverse().toString();
    }
}
