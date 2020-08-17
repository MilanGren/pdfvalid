
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


import java.lang.Exception ;
import java.io.InputStream ; 
import java.io.FileInputStream ;
import java.io.IOException ;

// this project
import com.example.demo.PDFValidator ; // interface
import com.example.demo.Result ; // wrapping result
import com.example.demo.PdfValidationException ;

@RestController
@RequestMapping("/")
public class ControllerApp {

  @Autowired
  private PDFValidator pdfvalidator ;
  
  public <T> void LOG(T t) {
    System.out.println(t) ;
  }

  @ExceptionHandler(Exception.class)
  public void error(Exception e) {
    LOG("err") ;
  }
  
  @PostMapping("/post")
  public Result post(@RequestParam("file") MultipartFile file, @RequestParam("level") String level) throws PdfValidationException, IOException {
  
    String filename = StringUtils.cleanPath(file.getOriginalFilename()) ;

    InputStream stream = file.getInputStream() ;
if (true) {
  throw new PdfValidationException("aaaaaaaaaaahhhhhhoooooooojjjjjjj") ; 
}
    boolean isvalid = pdfvalidator.validate(stream,level) ; 
  
    return new Result(isvalid,level,filename) ;
  }

}










