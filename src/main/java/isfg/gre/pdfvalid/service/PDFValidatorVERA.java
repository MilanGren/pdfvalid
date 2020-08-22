
package isfg.gre.pdfvalid.service ;

import java.io.InputStream ;

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
import org.verapdf.pdfa.flavours.PDFAFlavour ;

import isfg.gre.pdfvalid.PDFValidator ;
import isfg.gre.pdfvalid.PDFValidationException ;

@Service
public class PDFValidatorVERA implements PDFValidator {

    public boolean validate(InputStream istream, String flavourStr) throws PDFValidationException {
        
        if (!PDFAFlavour.getFlavourIds().contains(flavourStr)) throw new PDFValidationException(flavourStr + " is not available option") ;
    
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
