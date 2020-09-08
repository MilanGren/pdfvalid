
package isfg.gre.pdfvalid ;

import java.io.*;
import java.nio.file.Paths ;

import isfg.gre.pdfvalid.service.PDFValidatorVERA ;
import isfg.gre.pdfvalid.service.PDFConvertorPDFBOX ;


// logger
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.*; // TODO HACK hnus hnus
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;
import org.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding;
import org.apache.xmpbox.type.BadFieldValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// junit
import org.junit.Test;

// 
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode ;
import org.apache.xmpbox.XMPMetadata ;
//import org.apache.jempbox.xmp.pdfa.XMPSchemaPDFAId ;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.xml.XmpSerializer;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent ;

import org.apache.pdfbox.pdmodel.common.PDMetadata;

import javax.xml.transform.TransformerException;

public class PdfboxTest {
    private static final Logger log = LoggerFactory.getLogger(PdfboxTest.class) ;
    private static <T> void LOG(T t)  { // TODO vyhodit
        System.out.println("LOG: " + t) ;
    }

    private String testValidity(String pdffilepath, String flavourId) throws FileNotFoundException {
        PDFValidator validator = new PDFValidatorVERA() ;
        Result result = new Result(pdffilepath) ;
        validator.validate(new FileInputStream(pdffilepath),flavourId,result) ;
        return result.getValue() ;
    }

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
      pdd.setTitle("Rumburak Sample document");
      pdd.setCreator("Rumburak PDF Examples");
      pdd.setSubject("Rumburak Example document");

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

    public void createPdf(PDDocument document, PDFont font, String text) throws IOException {
        for (int i=0; i<1; i++) {
            PDPage blankPage = new PDPage();
            document.addPage( blankPage );
        }

        PDPage page = document.getPage(0);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(font, 22) ;
        contentStream.newLineAtOffset(25, 500);
        contentStream.showText(text);
        contentStream.endText();
        contentStream.close();

        contentStream = new PDPageContentStream(document, page, AppendMode.APPEND,true);
        contentStream.beginText();
        contentStream.setFont(font, 62);
        contentStream.newLineAtOffset(25, 200);
        contentStream.showText(text);
        contentStream.endText();
        contentStream.close();
        //setInformation(document) ;

    }


    private void makePdfValid(PDDocument doc, String flavourId) throws BadFieldValueException, TransformerException, IOException {
        String fontPath = Paths.get("src","test","resources","ttf","LiberationSerif-Regular.ttf").toFile().getAbsolutePath() ;
        int flavourIdInt = Character.getNumericValue( flavourId.charAt(0) ) ;
        char flavourIdStr = Character.toUpperCase( flavourId.charAt(1) ) ;

        PDFont font = PDType0Font.load(doc, new File(fontPath)); // c
        if (!font.isEmbedded()) {
            throw new IllegalStateException("PDF/A compliance requires that all fonts used for text rendering in rendering modes other than rendering mode 3 are embedded.");
        }

        XMPMetadata xmp = XMPMetadata.createXMPMetadata();
        PDFAIdentificationSchema id = xmp.createAndAddPFAIdentificationSchema();
        id.setPart(flavourIdInt);
        id.setConformance("B");
        //AdobePDFSchema schema = xmp.getAdobePDFSchema() ;
        //log.info( "schema.getPDFVersion().toString()" + schema.getPDFVersion().toString() );

        XmpSerializer serializer = new XmpSerializer();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.serialize(xmp, baos, true);

        PDMetadata metadata = new PDMetadata(doc);
        metadata.importXMPMetadata(baos.toByteArray());
        doc.getDocumentCatalog().setMetadata(metadata);

        InputStream colorProfile = new FileInputStream(Paths.get("src","test","resources","icc-pdfbox","sRGB.icc").toFile().getAbsolutePath()) ;
        PDOutputIntent intent = new PDOutputIntent(doc, colorProfile);
        intent.setInfo("sRGB IEC61966-2.1");
        intent.setOutputCondition("sRGB IEC61966-2.1");
        intent.setOutputConditionIdentifier("sRGB IEC61966-2.1");
        intent.setRegistryName("http://www.color.org");
        doc.getDocumentCatalog().addOutputIntent(intent);
    }

    @Test
    public void makeValidOnePDF_TEST() throws IOException, BadFieldValueException, TransformerException {

        // inputs
        String pdfFilePath = Paths.get("src","test","resources","example_065.pdf").toFile().getAbsolutePath() ;
        String convertedPdfFilePath = Paths.get("src","test","tmp","example_065-validated.pdf").toFile().getAbsolutePath() ;
        String fontPath = Paths.get("src","test","resources","ttf","LiberationSerif-Regular.ttf").toFile().getAbsolutePath() ;
        String flavourId = "2b" ;

        PDDocument document ;

        File file = new File(pdfFilePath);
        document = PDDocument.load(file);

        LOG( testValidity(pdfFilePath,flavourId) ) ;

        makePdfValid(document,flavourId) ;
        document.save(convertedPdfFilePath) ;
        document.close() ;

        LOG( testValidity(convertedPdfFilePath,flavourId) ) ;

    }

    @Test
    public  void truefont_TEST() throws IOException {
        // inputs
        String pdfFilePath = Paths.get("src","test","tmp","MYPDF.pdf").toFile().getAbsolutePath() ;
        String convertedPdfFilePath = Paths.get("src","test","tmp","MYPDF-validated.pdf").toFile().getAbsolutePath() ;
        String fontPath = Paths.get("src","test","resources","ttf","LiberationSerif-Regular.ttf").toFile().getAbsolutePath() ;
        String flavourId = "2b" ;

        // generate simple pdf with text as argument
        PDDocument document = new PDDocument();



        Encoding e = Encoding.getInstance(COSName.STANDARD_ENCODING);
        PDFont font = PDTrueTypeFont.load(document, new FileInputStream(new File(fontPath)), e) ;

        LOG(font.toUnicode(97)) ;



        document.close() ;
    }


    //COSStream toUnicodeStream = new COSStream();

    @Test
    public void CreateSimplePdf_TEST() throws java.io.IOException, BadFieldValueException, TransformerException {
        // inputs
        String pdfFilePath = Paths.get("src","test","tmp","MYPDF.pdf").toFile().getAbsolutePath() ;
        String convertedPdfFilePath = Paths.get("src","test","tmp","MYPDF-validated.pdf").toFile().getAbsolutePath() ;
        String fontPath = Paths.get("src","test","resources","ttf","LiberationSerif-Regular.ttf").toFile().getAbsolutePath() ;
        String flavourId = "2b" ;

        // generate simple pdf with text as argument
        PDDocument document = new PDDocument();

        PDFont font ;
        if (false) {
            // load fonts into plain doc to be embeded
            font = PDType0Font.load(document, new File(fontPath));
//            Encoding e = Encoding.getInstance(COSName.STANDARD_ENCODING);
//            font = PDTrueTypeFont.load(document, new FileInputStream(new File(fontPath)), e) ;
        } else {
            font = PDType1Font.TIMES_ROMAN ;  //  Postscript Type-1, vector based

        }

        createPdf(document, font, "a") ;
        document.save(pdfFilePath);
        document.close() ;

        if (true) {
            document = PDDocument.load(new File(pdfFilePath)) ;
        } else {
            // here i just load different pdf so creating of pdf in previous lines is useless
            document = PDDocument.load(new File(Paths.get("src","test","resources","sample-FONT_NOT_EMBEDED.pdf").toFile().getAbsolutePath())) ;
        }

        if (false) {
            // debug print of fonts used (+ they are by save processed by subset method)
            PDFont fontUsed;
            for (PDPage pg : document.getPages()) {
                PDResources resources = pg.getResources();
                for (COSName cosName : resources.getFontNames()) {
                    LOG("cosName: " + cosName.toString());
                    fontUsed = resources.getFont(cosName);
                    COSBase encoding = fontUsed.getCOSObject().getDictionaryObject(COSName.ENCODING);
                    LOG("font used in created pdf file: " + fontUsed.getName());
                    LOG(encoding.toString()) ;
                }
            }
        }

        PDFont font2 ;
        boolean font2isTrueType = true ;
        if (font2isTrueType) {
            //font2 = PDType0Font.load(document, new File(fontPath));
            //Encoding e = Encoding.getInstance(COSName.STANDARD_ENCODING);
            font2 = PDTrueTypeFont.load(document, new FileInputStream(new File(fontPath)), new WinAnsiEncoding()) ;
        } else {
            font2 = PDType1Font.COURIER_BOLD ; // tady velke pdfbox logy
        }
        boolean peformFontReplace = true ;

        if (peformFontReplace) {
            // replacing fonts. If font2isTrueType is false then it works
            PDFont fontUsed ;
            for (PDPage pg : document.getPages()) {
                PDResources resources = pg.getResources();
                for (COSName cosName : resources.getFontNames()) {
                    LOG("cosName: " + cosName.toString());
                    resources.put(cosName, font2) ;         // az tohle aktivuje 'embeded'..
                    fontUsed = resources.getFont(cosName);
                    LOG("font used in file processed by making it valid: " + fontUsed.getName());
                }
            }


        }

        // converting to desired flavourId
        makePdfValid(document,flavourId) ;
        document.save(convertedPdfFilePath) ;
        document.close() ;

        // testing validity
        LOG(testValidity(pdfFilePath,flavourId)) ;
        LOG(testValidity(convertedPdfFilePath,flavourId)) ;

    }
}






