
package isfg.gre.pdfvalid ;

import java.io.InputStream ;

public interface PDFValidator {
    public void validate(InputStream a, String b, Result r) throws PDFValidationException ;
    public void decide(InputStream a, Result r) throws PDFValidationException ;
    public void tryAllFlavoursGetFirstOccurence(InputStream a, Result r) throws PDFValidationException ;
}