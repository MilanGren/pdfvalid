
package isfg.gre.pdfvalid ;

import isfg.gre.pdfvalid.service.PDFValidatorVERA ;

import java.io.FileInputStream ;
import java.nio.file.Paths ;
import java.io.FileNotFoundException ;

import org.verapdf.pdfa.flavours.PDFAFlavour ;

// logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// junit
import static org.junit.Assert.* ;
import org.junit.Ignore ;
import org.junit.Test;

@Ignore
public class ProblemLatexForBeginnersTest {

    private static final Logger log = LoggerFactory.getLogger(ProblemLatexForBeginnersTest.class) ;

/*
I converted it to a valid pdf by https://www.pdfen.com/pdf-a-validator (os it should be 2u), however,
according to this project it is NOT 2u-valid. However, the Validation Profile (represented by flavourId) is 2u
.. see REMARKS
*/

    @Test
    public void TEST_SHOULD_BE_VALID_BUT_ISNOT() throws PDFValidationException, FileNotFoundException { 
        PDFValidator validator = new PDFValidatorVERA() ;
        String pdfFilePath = Paths.get("src","test","resources","latex_for_beginner-2u.pdf").toFile().getAbsolutePath() ;
        Result result = new Result(pdfFilePath) ;
        validator.decide(new FileInputStream(pdfFilePath),result) ;
        log.info(result.getValue()) ;
        assertEquals( "Failed" , result.getValue() ) ; 
        assertEquals( "2u", result.getFlavourId() ) ;
        assertEquals( "ISO 19005-2:2011" , result.getIso() )  ;
    }
    

}

