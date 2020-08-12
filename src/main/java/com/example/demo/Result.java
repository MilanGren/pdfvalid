
package com.example.demo ;

public class Result {
      private String value = "NO" ;
      public Result(boolean isvalid){
      
            if (isvalid) {
                  value = "YES" ;
            }
      
      }
      
      
      
      public String getValue() {return value ;}
}