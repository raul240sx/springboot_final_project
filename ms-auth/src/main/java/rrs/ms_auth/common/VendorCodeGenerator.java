package rrs.ms_auth.common;

import org.springframework.stereotype.Component;

import rrs.ms_auth.common.util.Base62Encoder;



@Component
public class VendorCodeGenerator {
    private final Base62Encoder encoder;

    public VendorCodeGenerator(Base62Encoder encoder) {
        this.encoder = encoder;
    }


    public String vendorCodeGenertator(Long id) {
        String encodedId = encoder.generateCode(id);

        String encodedIdZero =  "0".repeat(Math.max(0, 6 - encodedId.length())) + encodedId;

        return "VEN-" + encodedIdZero;
    }

}
