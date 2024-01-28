package microsservico.udemy.msavaliadorcredito.application.exception;

public class ErroSolicitacaoEmissaoCartaoException extends RuntimeException{
    public ErroSolicitacaoEmissaoCartaoException(String message) {
        super(message);
    }
}
