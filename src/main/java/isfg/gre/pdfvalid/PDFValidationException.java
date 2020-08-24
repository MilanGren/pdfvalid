
package isfg.gre.pdfvalid ;

import java.lang.Throwable ;
import java.lang.RuntimeException ;


public class PDFValidationException extends RuntimeException {  

    public PDFValidationException(String msg){  
        super(msg);  
    }  

    public PDFValidationException(String msg,Throwable e){  
        super(msg,e);  
    }  
}  