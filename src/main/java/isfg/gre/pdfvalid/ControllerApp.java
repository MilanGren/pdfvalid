
package isfg.gre.pdfvalid.controller ;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.apache.commons.io.FilenameUtils;

import java.lang.Exception ;
import java.io.InputStream ; 
import java.io.IOException ;

// logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// this project
import isfg.gre.pdfvalid.PDFValidator ; // interface
import isfg.gre.pdfvalid.Result ; // wrapping result
import isfg.gre.pdfvalid.PDFValidationException ;

@RestController
@RequestMapping("/")
public class ControllerApp {

  private static final Logger log = LoggerFactory.getLogger(ControllerApp.class) ;

  @Autowired
  private PDFValidator pdfvalidator ;

  @ExceptionHandler(PDFValidationException.class)
  public PDFValidationException error(PDFValidationException e) {
    log.error(e.getMessage(),e) ;
    return e ;
  }

  @ExceptionHandler(Exception.class)
  public Exception error(Exception e) {
    log.error(e.getMessage(),e) ;
    return e ;
  }
  
  @PostMapping("/pdfvalid")
  public Result post(@RequestParam("file") MultipartFile file, @RequestParam("level") String level) throws PDFValidationException, IOException {

    String filename = file.getOriginalFilename() ;

    String extension = FilenameUtils.getExtension(filename) ;

    if (!extension.equals("pdf")) {
      log.warn("file "+filename+" extension is "+extension+" (should be pdf)") ;
    }
    
    InputStream stream = file.getInputStream() ;

    boolean isvalid = pdfvalidator.validate(stream,level) ; 
  
    log.info(filename + " is " + level + " valid: " + isvalid) ;
  
    return new Result(isvalid,level,filename) ;
  }

}










