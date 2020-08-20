
package isfg.gre.pdfvalid ;

import isfg.gre.pdfvalid.service.PDFValidatorVERA ;

import java.io.FileInputStream ;
import java.nio.file.Paths ;
import java.io.FileNotFoundException ;

import static org.junit.Assert.* ;
import org.junit.Test;

public class VeraTest {

    public static <T> void LOG(T t)  {
        System.out.println(t) ;
    }
    
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


/*
according to https://www.pdf-online.com/osa/validate.aspx it is 1b-valid
according to https://www.pdfen.com/pdf-a-validator it is 1b-valid
according to VERA online validator https://demo.verapdf.org/ it is NOT 1b-valid
*/

    @Test
    public void TEST_4() throws PDFValidationException, FileNotFoundException { 

        PDFValidator validator = new PDFValidatorVERA() ;
        
        assertFalse( validator.validate(new FileInputStream(Paths.get("src","test","resources","example_065.pdf").toFile().getAbsolutePath()),"1b") ) ; 
        
    }


}




