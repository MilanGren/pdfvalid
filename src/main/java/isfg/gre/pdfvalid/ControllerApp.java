
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

// two taks are defined for this controlled
//   1. given level (flavourID) and file => "Passed" or "Failed" depending on if is/isnot valid
//   2. given file and check = true      => trying to find any "Passed" level occurence among all defined by VERA

  @PostMapping("/pdfvalid") // if check=true (searching for any valid level) then level is not used
  public Result post(@RequestParam("file") MultipartFile file, @RequestParam(value="level", defaultValue="1b") String level, @RequestParam(value="check", defaultValue="false") boolean check) throws PDFValidationException, IOException {

    String filename = file.getOriginalFilename() ;

    String extension = FilenameUtils.getExtension(filename) ;

    if (!extension.equals("pdf")) {  // or cover it by exception?
      log.warn("file "+filename+" extension is "+extension+" (should be pdf)") ;
    }
    
    Result result = new Result(filename) ;
    
    if (check) {
      log.info(filename + ": will check all VERA-defined flavours if there is any compliance") ;
      pdfvalidator.tryAllFlavoursGetFirstOccurence(file.getInputStream(),result) ;
    } else {
      pdfvalidator.validate(file.getInputStream(),level,result) ; 
    }
  
    if (result.isvalid) {
      log.info(filename + " validation: " + result.getValue() + ", level: " + result.getAskedFlavourId() + ", iso: " + result.getIso()) ;
    } else {
      log.info(filename + " validation: " + result.getValue()) ;
    }
    
  
  
    return result  ;
  }

}










