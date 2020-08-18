
package com.example.demo ;

import java.io.InputStream ;

public interface PDFValidator {
    public boolean validate(InputStream a, String b) throws PDFValidationException ;
}