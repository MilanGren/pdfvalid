
package isfg.gre.pdfvalid.service ;

import java.io.InputStream ;
import java.io.ByteArrayInputStream ;
import java.util.NoSuchElementException ;
import java.util.NoSuchElementException ;
import java.io.IOException ;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PDFValidatorVERA implements PDFValidator {

    private static final Logger log = LoggerFactory.getLogger(PDFValidatorVERA.class) ;

    public PDFValidatorVERA() {
        VeraGreenfieldFoundryProvider.initialise();        
    }

    // if you are not sure what PDF/A specification to use you can let the software decide
    public void decide(InputStream istream, Result result) throws PDFValidationException {
        result.Set(false,PDFAFlavour.NO_FLAVOUR.getId(),PDFAFlavour.NO_FLAVOUR.getPart().getId()) ; // if no succes than no overwrite ..
        try { 
            PDFAParser parser = Foundries.defaultInstance().createParser(istream) ;
            PDFAValidator validator = Foundries.defaultInstance().createValidator(parser.getFlavour(), false);
            ValidationResult r = validator.validate(parser);
            if (r.isCompliant()) result.Set(true,parser.getFlavour().getId(),parser.getFlavour().getPart().getId()) ;
        } catch (ModelParsingException | EncryptedPdfException | ValidationException e) {
            throw new PDFValidationException("pdf validation error",e);
        }        
    }
    
    
    // this should be the same as decide(..) but its done explicitly
    public void tryAllFlavoursGetFirstOccurence(InputStream istream, Result result) throws PDFValidationException {
        
        result.Set(false,PDFAFlavour.NO_FLAVOUR.getId(),PDFAFlavour.NO_FLAVOUR.getPart().getId()) ; // if no succes than no overwrite ..

        InputStream copyStream ;
        byte[] buffer ;
        try {
            buffer = new byte[istream.available()];
            istream.read(buffer);
        } catch (IOException e) {
            throw new PDFValidationException("pdf validation error",e);
        }

        for(PDFAFlavour flavour: PDFAFlavour.values()) {
            try { 
                copyStream = new ByteArrayInputStream(buffer);
                PDFAValidator validator = Foundries.defaultInstance().createValidator(flavour, false);
                PDFAParser parser = Foundries.defaultInstance().createParser(copyStream,flavour) ;
                ValidationResult r = validator.validate(parser); 
                log.info(" .. appling flavourid " + flavour.getId()) ;
                if (r.isCompliant()) {
                    result.Set(true,flavour.getId(),flavour.getPart().getId()) ;
                    break ;
                } else {
                    result.setAskedFlavourId(flavour.getId()) ;
                }
            } catch (java.util.NoSuchElementException e) {
                // there may be flavours defined in PDFAFlavour that can not be processed by validator 
                //   - PDFAFlavour.NO_FLAVOUR representing not valid pdf by any 'no iso'
                //   - PDFAFlavour.byFlavourId("4") representing somewhat experimental ..
                // 
                // moreover, i havent found any PDFAFlavour suitable attribute saying its experimental (.byFlavourId("4")) or represienting no iso (NO_FLAVOUR)
            } catch (ModelParsingException | EncryptedPdfException | ValidationException e) {
                throw new PDFValidationException("pdf validation error",e);
            }
        }
                
                
    }
    
    public void validate(InputStream istream, String askedFlavourId, Result result) throws PDFValidationException {

        // case insensitive aks
        if (PDFAFlavour.byFlavourId(askedFlavourId) == PDFAFlavour.NO_FLAVOUR) throw new PDFValidationException(askedFlavourId + " is not available in VERA defined flavours") ;
        
        try {

            PDFAFlavour flavour = PDFAFlavour.fromString(askedFlavourId);
            PDFAValidator validator = Foundries.defaultInstance().createValidator(flavour, false);
            PDFAParser parser = Foundries.defaultInstance().createParser(istream,flavour) ;
            ValidationResult r = validator.validate(parser);
        
            if (r.isCompliant()) {
                result.Set(true,askedFlavourId,flavour.getPart().getId()) ;
            } else {
                result.Set(false,askedFlavourId,PDFAFlavour.NO_FLAVOUR.getPart().getId()) ;
            }
            
        } catch (ModelParsingException | EncryptedPdfException | ValidationException | NoSuchElementException e) {
            throw new PDFValidationException("pdf validation exception",e);
        }
     


    }

}
