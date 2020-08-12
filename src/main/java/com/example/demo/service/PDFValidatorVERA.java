
package com.example.demo.service ;

import java.io.FileInputStream ;
import java.io.InputStream ;

import org.verapdf.pdfa.PdfBoxFoundryProvider;
import org.verapdf.pdfa.Foundries;
import org.verapdf.pdfa.PDFAParser;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import com.example.demo.PDFValidator ;
import org.springframework.stereotype.Service;


@Service
public class PDFValidatorVERA implements PDFValidator {

    public static <T> void LOG(T t) {
        System.out.println(t) ;
    }
    
    public void test() {
        System.out.println("PDFValidatorVERA") ;
    }


    public boolean validate(String fname, String flavourStr) throws java.io.FileNotFoundException, org.verapdf.core.ModelParsingException, java.io.IOException, org.verapdf.core.EncryptedPdfException, org.verapdf.core.ValidationException {

        PdfBoxFoundryProvider.initialise();
    
        PDFAFlavour flavour = PDFAFlavour.fromString(flavourStr);

        PDFAValidator validator = Foundries.defaultInstance().createValidator(flavour, false);

        PDFAParser parser = Foundries.defaultInstance().createParser(new FileInputStream(fname),flavour) ;

        ValidationResult result = validator.validate(parser);
     
        return result.isCompliant() ;
     
    }

    
    public boolean validateM(InputStream istream, String flavourStr) throws java.io.FileNotFoundException, 
                                                                            org.verapdf.core.ModelParsingException, 
                                                                            java.io.IOException, 
                                                                            org.verapdf.core.EncryptedPdfException, 
                                                                            org.verapdf.core.ValidationException {

        PdfBoxFoundryProvider.initialise();
    
        PDFAFlavour flavour = PDFAFlavour.fromString(flavourStr);

        PDFAValidator validator = Foundries.defaultInstance().createValidator(flavour, false);

        PDFAParser parser = Foundries.defaultInstance().createParser(istream,flavour) ;

        ValidationResult result = validator.validate(parser);
     
        return result.isCompliant() ;

    }

}
