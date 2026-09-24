package com.omarproject.storeapi.Exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String theResourceName, int theId) {
        super(theResourceName + " with the id of " + theId + " could not be found");
    }
}
