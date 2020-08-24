
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

public class VeraTest {

    private static final Logger log = LoggerFactory.getLogger(VeraTest.class) ;

    @Test
    public void TEST_FLAVOURINFO() {
        log.info("will print info of available flavours") ;
        for(PDFAFlavour flavour: PDFAFlavour.values()) {
            log.info( String.format( "%-5s %-10s %-20s %-10s %-55s %-10s", flavour.getId(), 
                                                                           flavour.getPart().getName(), 
                                                                           flavour.getPart().getId(), 
                                                                           flavour.getPart().getYear(), 
                                                                           flavour.getPart().getDescription(), 
                                                                           flavour.getLevel().getName()
                                                                           ));
        }
    }
  
    @Test
    public void TEST_tryAllFlavoursGetFirstOccurence_SUCCESS() throws PDFValidationException, FileNotFoundException { 
        PDFValidator validator = new PDFValidatorVERA() ;        
        String pdfFilePath = Paths.get("src","test","resources","validpdf_A-2b.pdf").toFile().getAbsolutePath() ;
        Result result = new Result(pdfFilePath) ;
        validator.tryAllFlavoursGetFirstOccurence(new FileInputStream(pdfFilePath),result) ;
        assertEquals( "Passed" , result.getValue() ) ;
        assertEquals( "2b" , result.getAskedFlavourId() ) ;
        assertEquals( "ISO 19005-2:2011" , result.getIso() )  ;
    }
    
    @Test
    public void TEST_tryAllFlavoursGetFirstOccurence_NOTSUCCES() throws PDFValidationException, FileNotFoundException { 
        PDFValidator validator = new PDFValidatorVERA() ;        
        String pdfFilePath = Paths.get("src","test","resources","latex_for_beginner.pdf").toFile().getAbsolutePath() ;
        Result result = new Result(pdfFilePath) ;
        validator.tryAllFlavoursGetFirstOccurence(new FileInputStream(pdfFilePath),result) ;
        assertEquals( "Failed" , result.getValue() ) ;
        assertEquals( "ISO 0-0:" , result.getIso() )  ;
        assertEquals( "3u", result.getAskedFlavourId() ) ; // last asked!
    }
    
    @Test
    public void TEST_FLAVOUR_BASICS_1() {
        assertTrue(PDFAFlavour.getFlavourIds().contains("1a")) ;
        assertFalse(PDFAFlavour.getFlavourIds().contains("1A")) ;  // checking existence - case sensitive
        assertEquals(PDFAFlavour.byFlavourId("1A").getId(),"1a") ; // is case insensitive
        assertTrue(PDFAFlavour.byFlavourId("1A") != null) ; // existence - case insensitive
        assertTrue(PDFAFlavour.byFlavourId("1A") == PDFAFlavour.PDFA_1_A) ; // has PDFA_1_A flavour
        assertTrue(PDFAFlavour.byFlavourId("1Aa") == PDFAFlavour.NO_FLAVOUR) ; // .. has no flavour ..
        assertEquals("ISO 19005-4:3000",PDFAFlavour.byFlavourId("4").getPart().getId()) ;
    }
    
    @Test
    public void TEST_FLAVOUR_BASICS_2() {
        PDFAFlavour f = PDFAFlavour.PDFA_1_B;
        assertEquals("ISO 19005-1:2005",f.getPart().getId()) ;
        assertEquals("ISO 0-0:",PDFAFlavour.PDFA_1_B.NO_FLAVOUR.getPart().getId()) ;
        assertEquals("ISO 19005-1:2005",PDFAFlavour.Specification.ISO_19005_1.getId()) ;
    }    

    @Test
    public void TEST_IS_VALID_1() throws PDFValidationException, FileNotFoundException { 
        PDFValidator validator = new PDFValidatorVERA() ;
        String pdfFilePath = Paths.get("src","test","resources","validpdf_A-1b.pdf").toFile().getAbsolutePath() ;
        Result result = new Result(pdfFilePath) ;
        validator.validate(new FileInputStream(pdfFilePath),"1b",result) ;
        assertEquals( "Passed" , result.getValue() ) ;
        assertEquals( "1b" , result.getAskedFlavourId() ) ;
        assertEquals( "ISO 19005-1:2005" , result.getIso() )  ;
    }
    
    @Test
    public void TEST_IS_NOT_VALID_1() throws PDFValidationException, FileNotFoundException { 
        PDFValidator validator = new PDFValidatorVERA() ;
        String pdfFilePath = Paths.get("src","test","resources","notvalid.pdf").toFile().getAbsolutePath() ;
        Result result = new Result(pdfFilePath) ;
        validator.validate(new FileInputStream(pdfFilePath),"1b",result) ;
        assertEquals( "Failed" , result.getValue() ) ;
        assertEquals( "1b" , result.getAskedFlavourId() ) ;
        assertEquals( "ISO 0-0:" , result.getIso() )  ;
    }

    @Test
    public void TEST_IS_VALID_2() throws PDFValidationException, FileNotFoundException { 
        PDFValidator validator = new PDFValidatorVERA() ;
        String pdfFilePath = Paths.get("src","test","resources","validpdf_A-2b.pdf").toFile().getAbsolutePath() ;
        Result result = new Result(pdfFilePath) ;
        validator.validate(new FileInputStream(pdfFilePath),"2b",result) ;
        assertEquals( "Passed" , result.getValue() ) ;
        assertEquals( "2b" , result.getAskedFlavourId() ) ;
        assertEquals( "ISO 19005-2:2011" , result.getIso() )  ;
    }

    @Test(expected = PDFValidationException.class)
    public void TEST_PDFValidationException() throws PDFValidationException, FileNotFoundException { 
        PDFValidator validator = new PDFValidatorVERA() ;
        String pdfFilePath = Paths.get("src","test","resources","validpdf_A-2b.pdf").toFile().getAbsolutePath() ;
        Result result = new Result(pdfFilePath) ;
        validator.validate(new FileInputStream(pdfFilePath),"2bx",result) ;    
    }
    

/*
according to https://www.pdf-online.com/osa/validate.aspx it is 1b-valid
according to https://www.pdfen.com/pdf-a-validator it is 1b-valid
according to VERA online validator https://demo.verapdf.org/ it is NOT 1b-valid
according to this project it is NOT 1b-valid
*/

    @Test
    public void TEST_example065_SHOULD_BE_VALID_BUT_ISNOT() throws PDFValidationException, FileNotFoundException { 
        PDFValidator validator = new PDFValidatorVERA() ;
        String pdfFilePath = Paths.get("src","test","resources","example_065.pdf").toFile().getAbsolutePath() ;
        Result result = new Result(pdfFilePath) ;
        validator.validate(new FileInputStream(pdfFilePath),"1b",result) ;
        assertEquals( "Failed" , result.getValue() ) ; 
    }

    @Test
    public void TEST_example065_SEARCHING_ANY_COMPLYING_FLAVOUR() throws PDFValidationException, FileNotFoundException { 
        PDFValidator validator = new PDFValidatorVERA() ;        
        String pdfFilePath = Paths.get("src","test","resources","example_065.pdf").toFile().getAbsolutePath() ;
        Result result = new Result(pdfFilePath) ;
        validator.tryAllFlavoursGetFirstOccurence(new FileInputStream(pdfFilePath),result) ;
        assertEquals( "Failed" , result.getValue() ) ; // no available flavour is ok
    }

    

}

