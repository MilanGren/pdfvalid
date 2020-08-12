
package com.example.demo ;

import java.io.InputStream ;

public interface PDFValidator {

    public void test() ;
    
    public boolean validate(String a, String b) throws java.io.FileNotFoundException, org.verapdf.core.ModelParsingException, java.io.IOException, org.verapdf.core.EncryptedPdfException, org.verapdf.core.ValidationException ;

    public boolean validateM(InputStream a, String b) throws java.io.FileNotFoundException, org.verapdf.core.ModelParsingException, java.io.IOException, org.verapdf.core.EncryptedPdfException, org.verapdf.core.ValidationException ;
}