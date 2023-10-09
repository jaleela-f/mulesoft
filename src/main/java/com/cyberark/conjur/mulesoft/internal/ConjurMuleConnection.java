package com.cyberark.conjur.mulesoft.internal;


/**
 * This class represents an extension connection just as example (there is no real connection with anything here c:).
 */
public final class ConjurMuleConnection {

  private final String id;
  
  private final Object value;
 
  

  public ConjurMuleConnection(String id,Object value) {
    this.id = id;
    this.value =value;
   
  }

  public String getId() {
    return id;
  }
  
  public Object getValue() {
	  return value;
  }

  public void invalidate() {
   
  }
}
