
package isfg.gre.pdfvalid ;

public class Result {
      private String value = "Failed" ;
      private String flavourId ;
      private String filename ;
      private String iso ;
      public boolean isvalid ;

      public Result(String filename) {
            this.filename = filename ; 
      }

      public void Set(boolean isvalid, String flavourId, String iso) {
            this.isvalid = isvalid ;
            if (isvalid) {
                  value = "Passed" ;
            }
            this.flavourId = flavourId ;
            this.iso = iso ;
      }
      
      public String getValue() {return value ;}
      public String getFlavourId() {return flavourId ;}
      public String getFilename() {return filename ;}
      public String getIso() {return iso ;}
      
      public String toString() {
            return "filename: " + filename + ", value: " + getValue() + ", flavourId: " + getFlavourId() + ", iso: " + getIso() ;
      }
      
      public void setAskedFlavourId(String flavourId) {
            this.flavourId = flavourId ; 
      }
      
}