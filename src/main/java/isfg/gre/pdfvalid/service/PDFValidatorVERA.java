
package isfg.gre.pdfvalid.service ;

import java.io.InputStream ;
import java.io.ByteArrayInputStream ;

import org.verapdf.pdfa.VeraGreenfieldFoundryProvider;
import org.verapdf.pdfa.Foundries;
import org.verapdf.pdfa.PDFAParser;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import org.springframework.stereotype.Service;
import org.verapdf.core.ModelParsingException ;
import org.verapdf.core.EncryptedPdfException ;
import org.verapdf.core.ValidationException ;

import isfg.gre.pdfvalid.PDFValidator ;
import isfg.gre.pdfvalid.Result ;
import isfg.gre.pdfvalid.PDFValidationException ;


@Service
public class PDFValidatorVERA implements PDFValidator {

    public void tryAllFlavoursGetFirstOccurence(InputStream istream, Result result) throws PDFValidationException {
        VeraGreenfieldFoundryProvider.initialise();    
        result.Set(false,PDFAFlavour.NO_FLAVOUR.getId(),PDFAFlavour.NO_FLAVOUR.getPart().getId()) ; // if no succes than no overwrite ..
        try {        
            InputStream copyStream ;
            byte[] buffer = new byte[istream.available()];
            istream.read(buffer);
            for(PDFAFlavour flavour: PDFAFlavour.values()) {
                if (flavour != PDFAFlavour.NO_FLAVOUR && flavour != PDFAFlavour.byFlavourId("4")) {
                    copyStream = new ByteArrayInputStream(buffer);
                    PDFAValidator validator = Foundries.defaultInstance().createValidator(flavour, false);
                    PDFAParser parser = Foundries.defaultInstance().createParser(copyStream,flavour) ;
                    ValidationResult r = validator.validate(parser); 
                    if (r.isCompliant()) {
                        result.Set(true,flavour.getId(),flavour.getPart().getId()) ;
                        break ;
                    } else {
                        result.setAskedFlavourId(flavour.getId()) ;
                    }
                }
            }
        } catch (ModelParsingException | EncryptedPdfException | ValidationException | java.io.IOException e) {
            throw new PDFValidationException("pdf validation error (is it realy a pdf file?)");
        }
    }
    
    public void validate(InputStream istream, String askedFlavourId, Result result) throws PDFValidationException {

        if (true) {  // case insensitive ask
            if (PDFAFlavour.byFlavourId(askedFlavourId) == PDFAFlavour.NO_FLAVOUR) throw new PDFValidationException(askedFlavourId + " is not available option") ;
        } else { // case sensitive ask
            if (!PDFAFlavour.getFlavourIds().contains(askedFlavourId)) throw new PDFValidationException(askedFlavourId + " is not available option") ;
        }
    
        try {

            VeraGreenfieldFoundryProvider.initialise();    
   
            PDFAFlavour flavour = PDFAFlavour.fromString(askedFlavourId);

            PDFAValidator validator = Foundries.defaultInstance().createValidator(flavour, false);

            PDFAParser parser = Foundries.defaultInstance().createParser(istream,flavour) ;

            ValidationResult r = validator.validate(parser);
        
            if (r.isCompliant()) {
                result.Set(true,askedFlavourId,flavour.getPart().getId()) ;
            } else {
                result.Set(false,askedFlavourId,PDFAFlavour.NO_FLAVOUR.getPart().getId()) ;
            }
            
        } catch (ModelParsingException | EncryptedPdfException | ValidationException e) {
            throw new PDFValidationException("pdf validation error (is it realy a pdf file?)");
        }
     


    }

}
