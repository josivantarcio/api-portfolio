package com.jtarcio.portfolioapi.exception;

public class PortfolioException extends RuntimeException{
    public PortfolioException(String mensageErro) {
        super(mensageErro);
    }
}
