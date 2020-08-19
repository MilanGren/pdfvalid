
package isfg.gre.pdfvalid ;

public class Result {
      private String value = "Failed" ;
      private String level ;
      private String filename ;

      public Result(boolean isvalid, String level, String filename){
            if (isvalid) {
                  value = "Passed" ;
            }
            this.level = level ;
            this.filename = filename ;
      }
      
      public String getValue() {return value ;}
      public String getLevel() {return level ;}
      public String getFilename() {return filename ;}
      
}