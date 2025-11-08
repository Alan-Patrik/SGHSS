package com.alanpatrik.sghss.api.exception.handler;

import com.alanpatrik.sghss.api.exception.ConflitoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.ErroDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalDefaultExceptionHandler {

    @ExceptionHandler(InformacaoNaoEncontradaException.class)
    public ResponseEntity<ErroDTO> informacaoNaoEncontrada(InformacaoNaoEncontradaException ex, HttpServletRequest request) {
        var erro = new ErroDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(ParametroInvalidoException.class)
    public ResponseEntity<ErroDTO> informacaoNaoEncontrada(ParametroInvalidoException ex, HttpServletRequest request) {
        var erro = new ErroDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErroDTO> informacaoJaCadastrada(ConflitoException ex, HttpServletRequest request) {
        var erro = new ErroDTO(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }
}