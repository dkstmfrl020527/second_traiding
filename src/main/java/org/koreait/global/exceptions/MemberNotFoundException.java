package org.koreait.global.exceptions;

public class MemberNotFoundException extends NotFoundException {
    public MemberNotFoundException(){
        super("NotFound.member");
        setErrorCode(true);
    }
}
