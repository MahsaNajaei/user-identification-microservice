package com.energymeter.useridentificationservice.exception;

public class EntityIdNotFoundException extends RuntimeException {

    public EntityIdNotFoundException(String entityName, long id) {
        super(String.format("%s With Id = %s Not Found!", entityName, id));
    }
}
