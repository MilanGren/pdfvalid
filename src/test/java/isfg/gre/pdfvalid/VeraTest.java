
package isfg.gre.pdfvalid ;

import isfg.gre.pdfvalid.service.PDFValidatorVERA ;

import java.io.FileInputStream ;
import java.nio.file.Paths ;
import java.io.FileNotFoundException ;
import java.util.Iterator ;

import org.verapdf.pdfa.flavours.PDFAFlavour ;

// junit
import static org.junit.Assert.* ;
import org.junit.Test;

public class VeraTest {

    
    @Test
    public void TEST_1() throws PDFValidationException, FileNotFoundException { 

        PDFValidator validator = new PDFValidatorVERA() ;
        
        assertTrue( validator.validate(new FileInputStream(Paths.get("src","test","resources","validpdf_A-1b.pdf").toFile().getAbsolutePath()),"1b") ) ; 
       
    }
    
    @Test
    public void TEST_2() throws PDFValidationException, FileNotFoundException { 

        PDFValidator validator = new PDFValidatorVERA() ;
        
        assertTrue( validator.validate(new FileInputStream(Paths.get("src","test","resources","validpdf_A-2b.pdf").toFile().getAbsolutePath()),"2b") ) ; 
       
    }

    @Test
    public void TEST_3() throws PDFValidationException, FileNotFoundException { 

        PDFValidator validator = new PDFValidatorVERA() ;
        
        assertFalse( validator.validate(new FileInputStream(Paths.get("src","test","resources","notvalid.pdf").toFile().getAbsolutePath()),"1b") ) ; 
        
    }
    
    
    @Test
    public void TEST_WRONG_FLAVOUR() throws PDFValidationException, FileNotFoundException { 

        PDFValidator validator = new PDFValidatorVERA() ;
        
        assertFalse( validator.validate(new FileInputStream(Paths.get("src","test","resources","notvalid.pdf").toFile().getAbsolutePath()),"abcdef") ) ; 
        
    }
    
    
    

/*
according to https://www.pdf-online.com/osa/validate.aspx the compliance is 'pdf1.5' which is not supported by VERA
according to this project it does not any available compliance 
*/
    @Test
    public void TEST_flavour_info() throws PDFValidationException, FileNotFoundException { 

        PDFValidator validator = new PDFValidatorVERA() ;

        assertTrue( PDFAFlavour.Specification.ISO_19005_1.getId().equals("ISO 19005-1:2005") ) ;
  
        String[] flavourIds = {"0","4","1a","2b","1b","3u","2u","3a","2a","3b"};  
        
        for(int i=0; i < flavourIds.length; i++) {
        
            if (PDFAFlavour.getFlavourIds().contains(flavourIds[i])) {
                assertTrue(true) ;
            } else {
                assertTrue(flavourIds[i]+" not included in available flavours",false) ;
            }    
            
            if (!flavourIds[i].equals("0") && !flavourIds[i].equals("4")) { // WHAT DOES 0 and 4 MEAN?
                assertFalse( validator.validate(new FileInputStream(Paths.get("src","test","resources","latex_for_beginner.pdf").toFile().getAbsolutePath()),flavourIds[i]) ) ;
            }
        
        }

/*
        Iterator value = PDFAFlavour.getFlavourIds().iterator(); 
        System.out.println("The iterator values are: "); 
        while (value.hasNext()) { 
            System.out.println(value.next()); 
        } 
*/
        
    }



/*
according to https://www.pdf-online.com/osa/validate.aspx it is 1b-valid
according to https://www.pdfen.com/pdf-a-validator it is 1b-valid
according to VERA online validator https://demo.verapdf.org/ it is NOT 1b-valid
*/

    @Test
    public void TEST_SHOULD_BE_VALID_BUT_ISNOT() throws PDFValidationException, FileNotFoundException { 

        PDFValidator validator = new PDFValidatorVERA() ;
        
        assertFalse( validator.validate(new FileInputStream(Paths.get("src","test","resources","example_065.pdf").toFile().getAbsolutePath()),"1b") ) ; 
        
    }


}




