package microsservico.udemy.msavaliadorcredito.application.exception;

import lombok.Getter;

public class ErroComunicacaoMicrosservicesException extends Exception{

    @Getter
    private Integer status;
    public ErroComunicacaoMicrosservicesException(String mensagem, Integer status) {
        super(mensagem);
        this.status = status;
    }
}
