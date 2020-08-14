
package com.example.demo.controller ;

import org.springframework.stereotype.Controller;
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
import java.io.InputStream; 

// this project
import com.example.demo.PDFValidator ; // interface
import com.example.demo.Result ; // wrapping result

@Controller
@RequestMapping("/")
public class ControllerApp {

  @Autowired
  private PDFValidator pdfvalidator ;
  
  public <T> void LOG(T t) {
    System.out.println(t) ;
  }

  @ExceptionHandler(Exception.class)
  public String error(Exception e, Model model) {
    model.addAttribute("err", e);
    return "error";
  }

  @GetMapping("/pdf")
  public String handerGet(Model model) {
    Result result = new Result(false) ;
    result.setStartingValue("choose a file and submit") ;
    model.addAttribute("result", result);
    return "upload" ; 
  }    

  @PostMapping("/pdf")
  public String handlerPost(@RequestParam("file") MultipartFile file, @RequestParam("level") String level, Model model) throws java.io.FileNotFoundException,
                                                                                                                               org.verapdf.core.ModelParsingException,
                                                                                                                               java.io.IOException,
                                                                                                                               org.verapdf.core.EncryptedPdfException,
                                                                                                                               org.verapdf.core.ValidationException
    {

    String filename = StringUtils.cleanPath(file.getOriginalFilename()) ;
    
    InputStream stream = file.getInputStream() ;

    boolean isvalid = pdfvalidator.validate(stream,level) ; 
    
    model.addAttribute("result", new Result(isvalid));

    return "upload" ; 
  }

}










