
package isfg.gre.pdfvalid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan ;

@SpringBootApplication
@ComponentScan({"isfg.gre.pdfvalid.controller","isfg.gre.pdfvalid.service"}) // it takes all by default which may cause redundant service initialisations
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
