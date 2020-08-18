
package com.example.demo.controller ;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.apache.commons.io.FilenameUtils;

import java.lang.Exception ;
import java.io.InputStream ; 
import java.io.FileInputStream ;
import java.io.IOException ;

// logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;


// this project
import com.example.demo.PDFValidator ; // interface
import com.example.demo.Result ; // wrapping result
import com.example.demo.PdfValidationException ;

@RestController
@RequestMapping("/")
public class ControllerApp {

  private static final Logger log = LoggerFactory.getLogger(ControllerApp.class) ;

  @Autowired
  private PDFValidator pdfvalidator ;

  @ExceptionHandler(Exception.class)
  public Exception error(Exception e) {
    log.error(e.getMessage(),e) ;
    return e ;
  }
  
  @PostMapping("/post")
  public Result post(@RequestParam("file") MultipartFile file, @RequestParam("level") String level) throws PdfValidationException, IOException {

    String filename = file.getOriginalFilename() ;

    String extension = FilenameUtils.getExtension(filename) ;

    if (!extension.equals("pdf")) {
      log.warn("file "+filename+" extension is "+extension+" (should be pdf)") ;
    }
  
    InputStream stream = file.getInputStream() ;

    boolean isvalid = pdfvalidator.validate(stream,level) ; 
  
    return new Result(isvalid,level,filename) ;
  }

}










