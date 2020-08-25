
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
import org.junit.Test;

public class ProblemExample065Test {

    private static final Logger log = LoggerFactory.getLogger(ProblemExample065Test.class) ;

/*
according to https://www.pdf-online.com/osa/validate.aspx it is 1b-valid
according to https://www.pdfen.com/pdf-a-validator it is 1b-valid
according to VERA online validator https://demo.verapdf.org/ it is NOT 1b-valid
according to this project it is NOT 1b-valid
*/

    @Test
    public void TEST_SHOULD_BE_VALID_BUT_ISNOT() throws PDFValidationException, FileNotFoundException { 
        PDFValidator validator = new PDFValidatorVERA() ;
        String pdfFilePath = Paths.get("src","test","resources","example_065.pdf").toFile().getAbsolutePath() ;
        Result result = new Result(pdfFilePath) ;
        validator.validate(new FileInputStream(pdfFilePath),"1b",result) ;
        assertEquals( "Failed" , result.getValue() ) ; // according to vendor pdf validator this should be Passed
    }

    @Test // searching for any compliance explicitly
    public void TEST_tryAllFlavoursGetFirstOccurence() throws PDFValidationException, FileNotFoundException { 
        PDFValidator validator = new PDFValidatorVERA() ;        
        String pdfFilePath = Paths.get("src","test","resources","example_065.pdf").toFile().getAbsolutePath() ;
        Result result = new Result(pdfFilePath) ;
        validator.tryAllFlavoursGetFirstOccurence(new FileInputStream(pdfFilePath),result) ;
        assertEquals( "Failed" , result.getValue() ) ; // no available flavour is ok
    }
    
    @Test // searching for any compliance implicitly (by VERA parser itself)
    public void TEST_decide() throws PDFValidationException, FileNotFoundException { 
        PDFValidator validator = new PDFValidatorVERA() ;        
        String pdfFilePath = Paths.get("src","test","resources","example_065.pdf").toFile().getAbsolutePath() ;
        Result result = new Result(pdfFilePath) ;
        validator.decide(new FileInputStream(pdfFilePath),result) ;
        assertEquals( "Failed" , result.getValue() ) ; // no available flavour is ok        
    }

}

