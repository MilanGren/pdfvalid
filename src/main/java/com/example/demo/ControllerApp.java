
package com.example.demo.controller ;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.util.StringUtils;


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

  @GetMapping("/pdf")
  public String fileUploadForm(Model model) {
    model.addAttribute("result", new Result(false)); // toto asi jde napsat pridat rovnou do html
    return "htmlupload" ; 
  }    

  @PostMapping("/pdf")
  public String handler(@RequestParam("file") MultipartFile file,
                        @RequestParam("level") String level,
                                  RedirectAttributes redirectAttributes, Model model) throws java.io.FileNotFoundException,
                                                                                  org.verapdf.core.ModelParsingException,
                                                                                  java.io.IOException,
                                                                                  org.verapdf.core.EncryptedPdfException,
                                                                                  org.verapdf.core.ValidationException
    {

    String filename = StringUtils.cleanPath(file.getOriginalFilename());

    boolean isvalid = pdfvalidator.validateM(file.getInputStream(),level) ; 
    
    model.addAttribute("result", new Result(isvalid));

    redirectAttributes.addFlashAttribute("message", "You successfully uploaded .. ");

    return "htmlupload" ; 
  }

}










