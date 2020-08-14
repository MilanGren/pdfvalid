
package com.example.demo ;

public class Result {
      private String value = "Failed" ;
      public Result(boolean isvalid){
      
            if (isvalid) {
                  value = "Passed" ;
            }
      
      }
      
      
      public void setStartingValue(String value) {this.value = value ;}
      
      public String getValue() {return value ;}
}