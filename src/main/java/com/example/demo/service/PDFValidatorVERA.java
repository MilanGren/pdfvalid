
package com.example.demo.service ;

import java.io.InputStream ;

import org.verapdf.pdfa.VeraGreenfieldFoundryProvider;
import org.verapdf.pdfa.Foundries;
import org.verapdf.pdfa.PDFAParser;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException ;
import org.verapdf.core.ModelParsingException ;
import java.io.IOException ;
import org.verapdf.core.EncryptedPdfException ;
import org.verapdf.core.ValidationException ;

import com.example.demo.PDFValidator ;

import com.example.demo.PDFValidationException ;

@Service
public class PDFValidatorVERA implements PDFValidator {

    public boolean validate(InputStream istream, String flavourStr) throws PDFValidationException {
    
        try {
            VeraGreenfieldFoundryProvider.initialise();
    
            PDFAFlavour flavour = PDFAFlavour.fromString(flavourStr);

            PDFAValidator validator = Foundries.defaultInstance().createValidator(flavour, false);

            PDFAParser parser = Foundries.defaultInstance().createParser(istream,flavour) ;

            ValidationResult result = validator.validate(parser);
        
            return result.isCompliant() ;
            
        } catch (ModelParsingException | EncryptedPdfException | ValidationException e) {
            throw new PDFValidationException("pdf validation error (is it realy a pdf file?)");
        }
     


    }

}
