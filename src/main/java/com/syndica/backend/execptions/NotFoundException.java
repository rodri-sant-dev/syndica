package com.syndica.backend.execptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(){
        super("Item not found");
    }  

    public NotFoundException(String mensagem){
        super(mensagem);
    }    
}

