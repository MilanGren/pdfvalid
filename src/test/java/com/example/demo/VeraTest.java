
package com.example.demo ;

import com.example.demo.service.PDFValidatorVERA ;

import java.io.FileInputStream ;
import java.nio.file.Path ;
import java.nio.file.Paths ;

import org.verapdf.pdfa.PdfBoxFoundryProvider;
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
    public void TEST_pdfvalidation() throws java.io.FileNotFoundException, org.verapdf.core.ModelParsingException, java.io.IOException, org.verapdf.core.EncryptedPdfException, org.verapdf.core.ValidationException {

        PDFValidator validator = new PDFValidatorVERA() ;
        
        assertTrue( validator.validate(Paths.get("src","test","resources","validpdf_A-1b.pdf").toFile().getAbsolutePath(),"1b") ) ; 

        assertFalse( validator.validate(Paths.get("src","test","resources","notvalid.pdf").toFile().getAbsolutePath(),"1b") ) ; 

        assertTrue( validator.validate(Paths.get("src","test","resources","example_065.pdf").toFile().getAbsolutePath(),"1b") ) ; 
        
        
        
    }

}




