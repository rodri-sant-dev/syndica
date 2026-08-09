package com.syndica.backend.execptions;

public class UserDoesNotExistExecption extends RuntimeException {
    public UserDoesNotExistExecption(){
        super("Username or password is incorrect");
    }  

    public UserDoesNotExistExecption(String mensagem){
        super(mensagem);
    }    
}

