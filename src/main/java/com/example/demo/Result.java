
package com.example.demo ;

public class Result {
      private String value = "Failed" ;
      public Result(boolean isvalid){
      
            if (isvalid) {
                  value = "Passed" ;
            }
      
      }
      
      
      
      public String getValue() {return value ;}
}