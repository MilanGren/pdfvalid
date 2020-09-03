
package isfg.gre.pdfvalid ;

import java.io.File;
import java.util.Calendar; 
import java.util.GregorianCalendar;
import java.io.FileInputStream ;
import java.nio.file.Paths ;
import java.io.FileNotFoundException ;
import java.io.ByteArrayOutputStream;
import java.io.InputStream ;

import isfg.gre.pdfvalid.service.PDFValidatorVERA ;

// logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// junit
import static org.junit.Assert.* ;
import org.junit.Ignore ;
import org.junit.Test;

// 
import org.apache.pdfbox.pdmodel.PDDocument ;
import org.apache.pdfbox.pdmodel.PDPage ;
import org.apache.pdfbox.pdmodel.PDPageContentStream ; 
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import org.apache.pdfbox.pdmodel.font.PDType1Font ;
import org.apache.xmpbox.XMPMetadata ;
//import org.apache.jempbox.xmp.pdfa.XMPSchemaPDFAId ;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.xml.XmpSerializer;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent ;

import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font ;

public class PdfboxTest {

    private static final Logger log = LoggerFactory.getLogger(PdfboxTest.class) ;
    
    public void addInformation(PDDocument document) {
      PDDocumentInformation pdd = document.getDocumentInformation();

      //Setting the author of the document
      pdd.setAuthor("Tutorialspoint");
       
      // Setting the title of the document
      pdd.setTitle("Sample document"); 
       
      //Setting the creator of the document 
      pdd.setCreator("PDF Examples"); 
       
      //Setting the subject of the document 
      pdd.setSubject("Example document"); 
       
      //Setting the created date of the document 
      Calendar date = new GregorianCalendar();
      date.set(2015, 11, 5); 
      pdd.setCreationDate(date);
      //Setting the modified date of the document 
      date.set(2016, 6, 5); 
      pdd.setModificationDate(date); 
       
      //Setting keywords for the document 
      pdd.setKeywords("sample, first example, my pdf"); 
    }
    
//    @Test
    public void createPdf(String fontPath) throws java.io.IOException {
      PDDocument document = new PDDocument();    

      for (int i=0; i<2; i++) {
        PDPage blankPage = new PDPage();
        document.addPage( blankPage );
      }    
      
      PDFont font ;
      if (true) {
        font = PDType0Font.load(document, new File(fontPath));
      } else {
        font = PDType1Font.TIMES_ROMAN ;
      }
      
      PDPage page = document.getPage(0);
      PDPageContentStream contentStream = new PDPageContentStream(document, page);
      contentStream.beginText(); 
      contentStream.setFont(font, 22);
      contentStream.newLineAtOffset(25, 500);
      String text = "ahoj Milan Gren ahoj";
      contentStream.showText(text);      
      contentStream.endText();
      contentStream.close();  

      addInformation(document) ; 

      document.save("/home/gre/java/maven/pdfvalid/src/test/java/isfg/gre/pdfvalid/MYPDF.pdf");
      document.close() ;
      
    }


    public void createValidPDf(int flavourIdint, String startingPDFile,String fontPath) throws java.io.IOException, org.apache.xmpbox.type.BadFieldValueException, javax.xml.transform.TransformerException {
    
    
      PDDocument doc ;
      File file ;
      if (true) {//load starting pdf file 
        file = new File(startingPDFile); 
        doc = PDDocument.load(file); 
      } else { // start very new document 
        doc = new PDDocument() ;
      }

      PDPage page = new PDPage();
      doc.addPage(page);
      PDFont font = PDType0Font.load(doc, new File(fontPath));
	
      String message = "ahoj ahoj ahoj"; 
 	            // load the font as this needs to be embedded

      if (!font.isEmbedded()) {
        throw new IllegalStateException("PDF/A compliance requires that all fonts used for text rendering in rendering modes other than rendering mode 3 are embedded.");
      }

      PDPageContentStream contents = new PDPageContentStream(doc, page) ;
      contents.beginText();
      contents.setFont(font, 12);
      contents.newLineAtOffset(100, 700);
      contents.showText(message);
      contents.endText();
      contents.close();  
      
      XMPMetadata xmp = XMPMetadata.createXMPMetadata();
      PDFAIdentificationSchema id = xmp.createAndAddPFAIdentificationSchema();
      id.setPart(flavourIdint);
      id.setConformance("B");

      XmpSerializer serializer = new XmpSerializer();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      serializer.serialize(xmp, baos, true);

      PDMetadata metadata = new PDMetadata(doc);
      metadata.importXMPMetadata(baos.toByteArray());
      doc.getDocumentCatalog().setMetadata(metadata);

      InputStream colorProfile = new FileInputStream("/home/gre/Downloads/sRGB.icc"); //PdfboxTest.class.getResourceAsStream("/home/gre/Downloads/sRGB.icc") ;

      PDOutputIntent intent = new PDOutputIntent(doc, colorProfile);

      intent.setInfo("sRGB IEC61966-2.1");
      intent.setOutputCondition("sRGB IEC61966-2.1");
      intent.setOutputConditionIdentifier("sRGB IEC61966-2.1");
      intent.setRegistryName("http://www.color.org");
      doc.getDocumentCatalog().addOutputIntent(intent);

      doc.save("/home/gre/java/maven/pdfvalid/src/test/java/isfg/gre/pdfvalid/validated.pdf");

      doc.close() ;
        
    }

    @Test
    public void TEST_IS_VALID() throws PDFValidationException, FileNotFoundException, java.io.IOException, org.apache.xmpbox.type.BadFieldValueException, javax.xml.transform.TransformerException {
    
        createValidPDf(2,"/home/gre/java/maven/pdfvalid/src/test/java/isfg/gre/pdfvalid/MYPDF.pdf",
//                         "/home/gre/pdfbox/pdfbox/src/main/resources/org/apache/pdfbox/resources/ttf/LiberationSans-Regular.ttf"
                         "/home/gre/Downloads/fonts/Pacifico.ttf"
        ) ;
        
//        createValidPDf(2,"/home/gre/java/maven/pdfvalid/src/test/resources/notvalid.pdf") ;

        PDFValidator validator = new PDFValidatorVERA() ;
        String pdfFilePath = Paths.get("src","test","java","isfg","gre","pdfvalid","validated.pdf").toFile().getAbsolutePath() ;
        Result result = new Result(pdfFilePath) ;
        validator.decide(new FileInputStream(pdfFilePath),result) ;
        
        
        log.info("") ;
        log.info("") ;
        log.info(result.toString()) ;
        log.info("") ;
        log.info("") ;

    }  

}

