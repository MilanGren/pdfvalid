
package isfg.gre.pdfvalid ;

import java.io.*;
import java.nio.file.Paths ;

import isfg.gre.pdfvalid.service.PDFValidatorVERA ;

// logger
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.*; // TODO HACK hnus hnus
import org.apache.xmpbox.type.BadFieldValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// junit
import org.junit.Test;

// 
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode ;
import org.apache.pdfbox.pdmodel.font.PDType1Font ;
import org.apache.xmpbox.XMPMetadata ;
//import org.apache.jempbox.xmp.pdfa.XMPSchemaPDFAId ;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.xml.XmpSerializer;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent ;

import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font ;

import javax.xml.transform.TransformerException;

public class PdfboxTest {

    private static final Logger log = LoggerFactory.getLogger(PdfboxTest.class) ;

    private void getInformation(PDDocument document) {
        PDDocumentInformation pdd = document.getDocumentInformation();
        log.info(pdd.getAuthor()) ;
        log.info(pdd.getTitle()) ;
        log.info(pdd.getKeywords());
        log.info(pdd.getCreator());
        log.info(pdd.getProducer()) ;
    }

    private void setInformation(PDDocument document) {
      PDDocumentInformation pdd = document.getDocumentInformation();

      pdd.setAuthor("Rumburak");
//      pdd.setTitle("Sample document");
//      pdd.setCreator("PDF Examples");
//      pdd.setSubject("Example document");
       
      //Setting the created date of the document 
//      Calendar date = new GregorianCalendar();
//      date.set(2015, 11, 5);
//      pdd.setCreationDate(date);
      //Setting the modified date of the document 
 //     date.set(2016, 6, 5);
 //     pdd.setModificationDate(date);
       
      //Setting keywords for the document 
 //     pdd.setKeywords("sample, first example, my pdf");
    }



    public void createPdf(String fontPath) throws java.io.IOException {
      PDDocument document = new PDDocument();    

      for (int i=0; i<1; i++) {
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

      //setInformation(document) ;

      String pdfFilePath = Paths.get("src","test","tmp","MYPDF.pdf").toFile().getAbsolutePath() ;

      document.save(pdfFilePath);


      document.close() ;
      
    }


    private void makePdfValid(PDDocument doc, int flavourIdint) throws BadFieldValueException, TransformerException, IOException {
        XMPMetadata xmp = XMPMetadata.createXMPMetadata();
        PDFAIdentificationSchema id = xmp.createAndAddPFAIdentificationSchema();
        id.setPart(flavourIdint);
        id.setConformance("B");
        //AdobePDFSchema schema = xmp.getAdobePDFSchema() ;
        //log.info( "schema.getPDFVersion().toString()" + schema.getPDFVersion().toString() );

        XmpSerializer serializer = new XmpSerializer();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.serialize(xmp, baos, true);

        PDMetadata metadata = new PDMetadata(doc);
        metadata.importXMPMetadata(baos.toByteArray());
        doc.getDocumentCatalog().setMetadata(metadata);

        InputStream colorProfile = new FileInputStream(

        Paths.get("src","test","resources","Adobe_ICC_Profiles_end-user","RGB","AdobeRGB1998.icc").toFile().getAbsolutePath()) ;

        PDOutputIntent intent = new PDOutputIntent(doc, colorProfile);

        intent.setInfo("sRGB IEC61966-2.1");
        intent.setOutputCondition("sRGB IEC61966-2.1");
        intent.setOutputConditionIdentifier("sRGB IEC61966-2.1");
        intent.setRegistryName("http://www.color.org");
        doc.getDocumentCatalog().addOutputIntent(intent);
    }

    @Test
    public void makeValidOnePDF() throws IOException, BadFieldValueException, TransformerException {
        // input
        int flavourIdint = 1 ;
        String startingPDFile = Paths.get("src","test","resources","fonts-1a_valid.pdf").toFile().getAbsolutePath() ;
        String fontPath = Paths.get("src","test","resources","ttf","LiberationSerif-Regular.ttf").toFile().getAbsolutePath() ;
        // --

        String flavourId = Integer.toString(flavourIdint) + "b";

        PDDocument doc ;

        File file = new File(startingPDFile);
        doc = PDDocument.load(file);

        PDFValidator validator = new PDFValidatorVERA() ;
        Result result = new Result(startingPDFile) ;
        validator.validate(new FileInputStream(startingPDFile),flavourId,result) ;
        log.info("") ;
        log.info("") ;
        log.info(result.toString()) ;
        log.info("") ;
        log.info("") ;

        getInformation(doc) ;

        PDFont font = PDType0Font.load(doc, new File(fontPath)); // loading font into doc?? opravdu??

        if (!font.isEmbedded()) {
            throw new IllegalStateException("PDF/A compliance requires that all fonts used for text rendering in rendering modes other than rendering mode 3 are embedded.");
        }

        makePdfValid(doc,flavourIdint) ;

        String validatedPath = Paths.get("src","test","tmp","validated.pdf").toFile().getAbsolutePath() ;

        doc.save(validatedPath) ;
        //setInformation(doc);
        getInformation(doc);
        doc.close() ;

        validator = new PDFValidatorVERA() ;
        result = new Result(validatedPath) ;
        validator.validate(new FileInputStream(validatedPath),flavourId,result) ;
        log.info("") ;
        log.info("") ;
        log.info(result.toString()) ;
        log.info("") ;
        log.info("") ;

    }

    public void mergeTwoPDFsANDmakeThemValid(int flavourIdint, String startingPDFile, String fontPath) throws java.io.IOException, org.apache.xmpbox.type.BadFieldValueException, javax.xml.transform.TransformerException {

        PDDocument doc ;

        File file ;
        if (true) {// load starting pdf file
            file = new File(startingPDFile);
            doc = PDDocument.load(file);
            getInformation(doc) ;
        } else { // start a very new document
            doc = new PDDocument() ;
        }

        PDFont font = PDType0Font.load(doc, new File(fontPath));

      // upravy na strane 0 v loadovanem PDF
        PDPage page0 = doc.getPage(0);

        PDResources resources = page0.getResources();

        for(COSName key : resources.getFontNames()) {
            PDFont fnt = resources.getFont(key);
            log.info(fnt.getFontDescriptor().getFontName());
            //resources.put(key, font) ;
        }

        PDPageContentStream contents0 = new PDPageContentStream(doc, page0, AppendMode.APPEND, true, true) ;
        contents0.beginText();
        contents0.setFont(font, 18);
        contents0.newLineAtOffset(55, 450);
        String text = "PRIDANO" ;
        contents0.showText(text);
        contents0.endText();
        contents0.close();

        PDPage page = new PDPage();
        doc.addPage(page);

        if (!font.isEmbedded()) {
            throw new IllegalStateException("PDF/A compliance requires that all fonts used for text rendering in rendering modes other than rendering mode 3 are embedded.");
        }

        PDPageContentStream contents = new PDPageContentStream(doc, page) ;
        contents.beginText();
        contents.setFont(font, 12);
        contents.newLineAtOffset(100, 700);
        contents.showText("ahoj ahoj ahoj");
        contents.endText();
        contents.close();

        makePdfValid(doc,flavourIdint);

        doc.save(Paths.get("src","test","tmp","validated-merge.pdf").toFile().getAbsolutePath()) ;
        setInformation(doc);
        getInformation(doc);

        doc.close() ;
        
    }

    @Test
    public void createPdf_PacificoTTF_Test() throws java.io.IOException {
        createPdf(Paths.get("src","test","resources","ttf","Pacifico.ttf").toFile().getAbsolutePath()) ;
    }

    @Test
    public void TEST_MERGE_PDFs_AND_VALIDATE() throws PDFValidationException, FileNotFoundException, java.io.IOException, org.apache.xmpbox.type.BadFieldValueException, javax.xml.transform.TransformerException {
    
        mergeTwoPDFsANDmakeThemValid(2,Paths.get("src","test","resources","notvalid.pdf").toFile().getAbsolutePath(),
                                    Paths.get("src","test","resources","ttf","LiberationSerif-Regular.ttf").toFile().getAbsolutePath()
        ) ;
        
//        createValidPDf(2,"/home/gre/java/maven/pdfvalid/src/test/resources/notvalid.pdf") ;

        PDFValidator validator = new PDFValidatorVERA() ;
        String pdfFilePath = Paths.get("src","test","tmp","validated-merge.pdf").toFile().getAbsolutePath() ;
        Result result = new Result(pdfFilePath) ;
        //validator.decide(new FileInputStream(pdfFilePath),result) ;
        validator.validate(new FileInputStream(pdfFilePath),"2b",result) ;
        
        
        log.info("") ;
        log.info("") ;
        log.info(result.toString()) ;
        log.info("") ;
        log.info("") ;

    }  

}

