package isfg.gre.pdfvalid.service;

import org.apache.fontbox.ttf.TTFSubsetter;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class PDFAConvertorPDFBOX {

    // TODO System.out... vyhodit

    public static <T> void LOG(T t)  { // TODO vyhodit
        System.out.println("LOG: " + t) ;
    }

    private final String OUTPUT_DIR = Paths.get("src","test","tmp").toFile().getAbsolutePath() ;

    private PDFont fontTBembeded;

    public void pokus(String filePath) throws IOException {
        String fontTBembededPath = Paths.get("src","test","resources","ttf","LiberationSerif-Regular.ttf").toFile().getAbsolutePath() ;
        try (final PDDocument document = PDDocument.load(new File(filePath))){

            PDFParser parser = new PDFParser((RandomAccessRead) new FileInputStream(filePath));
            parser.parse();
            PDFTextStripper pdfStripper = null ;
            PDDocument pdDoc = null ;


            COSDocument cosDoc = parser.getDocument() ;
                pdfStripper = new PDFTextStripper();
                pdDoc = new PDDocument(cosDoc);
                pdfStripper.setStartPage(0);
                pdfStripper.setEndPage(0);
                String parsedText = pdfStripper.getText(pdDoc);
                System.out.println(parsedText);

            System.out.println("AHOJ");


            fontTBembeded = PDType0Font.load(document, new File(fontTBembededPath));
            for (PDPage page : document.getPages()) {
                PDResources resources = page.getResources();
                //resources.add(fontTBembeded) ;
                for (COSName fontName : resources.getFontNames()) {
                    LOG(fontName.toString()) ;
                    PDFont font = resources.getFont(fontName);


                    LOG(font instanceof PDType0Font) ;
                    if (font instanceof PDType0Font) {
                        PDCIDFont descendantFont = ((PDType0Font) font).getDescendantFont();
                        if (descendantFont instanceof PDCIDFontType2) {
                            //writeFont(descendantFont.getFontDescriptor(), font.getName());
                            LOG(font.getName());
                            //PDFontDescriptor fd = descendantFont.getFontDescriptor() ;
                            //fd.setForceBold(true);
                        }
                    }

                    //TTFSubsetter x = new TTFSubsetter(font) ;

                    //fontTBembeded.addToSubset(073 );
                    //fontTBembeded.addToSubset(0062 );
                    //fontTBembeded.addToSubset(0063 );
                   // fontTBembeded.subset();

                    LOG(fontTBembeded.getName()) ;

                    //LOG(fontTBembeded.getName()) ;
                    //resources.put(fontName, fontTBembeded) ; // rozbije dokument
                    //LOG(obj.toString()) ;
                }
            }
            //document.save( Paths.get("src","test","tmp","changing_fonts.pdf").toFile().getAbsolutePath() );
            document.save("/home_pleiades/gre/java/maven/pdfvalid/src/test/tmp/changing_fonts.pdf") ;
            document.close();
        } catch (IOException e){
            System.err.println("Exception while trying to read pdf document - " + e);
        }
    }

    public void fontInfo(String filePath) throws Exception {
        String fontTBembededPath = Paths.get("src","test","resources","ttf","Pacifico.ttf").toFile().getAbsolutePath() ;
        try (final PDDocument document = PDDocument.load(new File(filePath))){
            fontTBembeded = PDType0Font.load(document, new File(fontTBembededPath));
            for (PDPage page : document.getPages()) {
                PDResources resources = page.getResources();
                processResources(resources);
            }
            document.save( Paths.get("src","test","tmp","changing_fonts.pdf").toFile().getAbsolutePath() );
            document.close();
        } catch (IOException e){
            System.err.println("Exception while trying to read pdf document - " + e);
        }
    }

    private void processResources(PDResources resources) throws IOException {
        if (resources == null) {
            return;
        }

        for (COSName key : resources.getFontNames()) {
            LOG(key.toString()) ;
            PDFont font = resources.getFont(key);
            LOG(font.toString()) ;
            if (font instanceof PDTrueTypeFont) {
                LOG("it is PDTrueTypeFont") ;
                PDFontDescriptor fd  = font.getFontDescriptor() ;
                //fd.setForceBold(true); - nefunguje
                //resources.put(key, fontTBembeded) - rozbije dokument ;
                writeFont(fd, font.getName());
            } else if (font instanceof PDType0Font) {
                System.exit(1); // TODO vyhodit
                LOG("it is PDType0Font") ;
                PDCIDFont descendantFont = ((PDType0Font) font).getDescendantFont();
                if (descendantFont instanceof PDCIDFontType2) {
                    writeFont(descendantFont.getFontDescriptor(), font.getName());
                }
            }
        }

        for (COSName name : resources.getXObjectNames()) {
            PDXObject xobject = resources.getXObject(name);
            if (xobject instanceof PDFormXObject) {
                PDFormXObject xObjectForm = (PDFormXObject) xobject;
                PDResources formResources = xObjectForm.getResources();
                processResources(formResources);
            }
        }

    }

    private void writeFont(PDFontDescriptor fd, String name) throws IOException {
        if (fd != null) {
            PDStream ff2Stream = fd.getFontFile2(); // fontfile2 stream
            if (ff2Stream != null) {
                String fontttfpath = Paths.get(OUTPUT_DIR.toString(), name+".ttf").toFile().getAbsolutePath() ;
                try (FileOutputStream fos = new FileOutputStream(new File(fontttfpath))) {
                    System.out.println("Writing font:" + fontttfpath);
                    IOUtils.copy(ff2Stream.createInputStream(), fos);
                }
            }
        }
    }



}


