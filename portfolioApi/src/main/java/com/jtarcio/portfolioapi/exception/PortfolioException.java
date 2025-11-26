package com.jtarcio.portfolioapi.exception;

public class PortfolioException extends RuntimeException{
    public PortfolioException(String mensageErro) {
        super(mensageErro);
    }

    public PortfolioException(String mensageErro, Throwable cause) {
        super(mensageErro, cause);
    }
}
