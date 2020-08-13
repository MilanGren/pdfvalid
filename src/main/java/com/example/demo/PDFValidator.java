
package com.example.demo ;

import java.io.InputStream ;

public interface PDFValidator {

    public boolean validate(InputStream a, String b) throws java.io.FileNotFoundException, org.verapdf.core.ModelParsingException, java.io.IOException, org.verapdf.core.EncryptedPdfException, org.verapdf.core.ValidationException ;
}