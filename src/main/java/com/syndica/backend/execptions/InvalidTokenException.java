package com.syndica.backend.execptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(){
        super("Refresh token is invalid");
    }  

    public InvalidTokenException(String mensagem){
        super(mensagem);
    }    

}
