package isfg.gre.pdfvalid.service;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException ;
import java.nio.file.Paths;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;

public class PDFConvertorPDFBOX {

    private static final Logger log = LoggerFactory.getLogger(PDFConvertorPDFBOX.class) ;

    private static <T> void LOG(T t)  {
        log.info((String)t) ;
    }

    public void extractEmbeddedFonts(String filePath) throws Exception {

        try (final PDDocument document = PDDocument.load(new File(filePath))){
            for (PDPage page : document.getPages()) {
                PDResources resources = page.getResources();
                processResources(resources);
            }
        } catch (IOException e){
            System.err.println("Exception while trying to read pdf document - " + e);
            System.exit(1) ;
        }
    }


    private void processResources(PDResources resources) throws IOException {
        if (resources == null) {
            return;
        }

        for (COSName key : resources.getFontNames()) {
            PDFont font = resources.getFont(key);
            if (font instanceof PDTrueTypeFont) {
                writeFont(font.getFontDescriptor(), font.getName());
            } else if (font instanceof PDType0Font) {
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
            PDStream ff2Stream = fd.getFontFile2();
            if (ff2Stream != null) {
                String fontFile = Paths.get("src","test","tmp",name+".ttf").toFile().getAbsolutePath() ;
                try (FileOutputStream fos = new FileOutputStream(new File(fontFile))) {
                    LOG("Writing font:" + fontFile);
                    IOUtils.copy(ff2Stream.createInputStream(), fos);
                }
            }
        }
    }


}
