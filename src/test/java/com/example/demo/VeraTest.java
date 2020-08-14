
package com.example.demo ;

import com.example.demo.service.PDFValidatorVERA ;

import java.io.FileInputStream ;
import java.nio.file.Path ;
import java.nio.file.Paths ;

import org.verapdf.pdfa.VeraGreenfieldFoundryProvider;
import org.verapdf.pdfa.Foundries;
import org.verapdf.pdfa.PDFAParser;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;

// junit knihovny
import static org.junit.Assert.* ;
import org.junit.Test;

public class VeraTest {



    public static <T> void LOG(T t)  {
        System.out.println(t) ;
    }
    
    @Test
    public void TEST_1() throws java.io.FileNotFoundException, org.verapdf.core.ModelParsingException, java.io.IOException, org.verapdf.core.EncryptedPdfException, org.verapdf.core.ValidationException {

        PDFValidator validator = new PDFValidatorVERA() ;
        
        assertTrue( validator.validate(new FileInputStream(Paths.get("src","test","resources","validpdf_A-1b.pdf").toFile().getAbsolutePath()),"1b") ) ; 
       
    }
    
    @Test
    public void TEST_2() throws java.io.FileNotFoundException, org.verapdf.core.ModelParsingException, java.io.IOException, org.verapdf.core.EncryptedPdfException, org.verapdf.core.ValidationException {

        PDFValidator validator = new PDFValidatorVERA() ;
        
        assertTrue( validator.validate(new FileInputStream(Paths.get("src","test","resources","validpdf_A-2b.pdf").toFile().getAbsolutePath()),"2b") ) ; 
       
    }
    


    @Test
    public void TEST_3() throws java.io.FileNotFoundException, org.verapdf.core.ModelParsingException, java.io.IOException, org.verapdf.core.EncryptedPdfException, org.verapdf.core.ValidationException {

        PDFValidator validator = new PDFValidatorVERA() ;
        
        assertFalse( validator.validate(new FileInputStream(Paths.get("src","test","resources","notvalid.pdf").toFile().getAbsolutePath()),"1b") ) ; 
        
    }


/*

podle https://www.pdf-online.com/osa/validate.aspx prochazi jako 1b
podle https://www.pdfen.com/pdf-a-validator prochazi jako 1b
podle VERA online https://demo.verapdf.org/ neprochazi jako 1b
    @Test
    public void TEST_4() throws java.io.FileNotFoundException, org.verapdf.core.ModelParsingException, java.io.IOException, org.verapdf.core.EncryptedPdfException, org.verapdf.core.ValidationException {

        PDFValidator validator = new PDFValidatorVERA() ;
        
        assertTrue( validator.validate(new FileInputStream(Paths.get("src","test","resources","example_065.pdf").toFile().getAbsolutePath()),"1b") ) ; 
        
    }
*/

}




