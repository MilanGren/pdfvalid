
package isfg.gre.pdfvalid ;

public class Result {
      private String value = "Failed" ;
      private String askedFlavourId ;
      private String filename ;
      private String iso ;

      public Result(String filename) {
            this.filename = filename ; 
      }

      public void Set(boolean isvalid, String askedFlavourId, String iso) {
            if (isvalid) {
                  value = "Passed" ;
            }
            this.askedFlavourId = askedFlavourId ;
            this.iso = iso ;
      }
      
      public String getValue() {return value ;}
      public String getAskedFlavourId() {return askedFlavourId ;}
      public String getFilename() {return filename ;}
      public String getIso() {return iso ;}
      
      
      public void setAskedFlavourId(String askedFlavourId) {
            this.askedFlavourId = askedFlavourId ; 
      }
      
}