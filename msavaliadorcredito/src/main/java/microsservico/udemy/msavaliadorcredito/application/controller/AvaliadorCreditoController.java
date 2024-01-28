package microsservico.udemy.msavaliadorcredito.application.controller;

import lombok.RequiredArgsConstructor;
import microsservico.udemy.msavaliadorcredito.application.exception.DadosClienteNotFoundException;
import microsservico.udemy.msavaliadorcredito.application.exception.ErroComunicacaoMicrosservicesException;
import microsservico.udemy.msavaliadorcredito.application.exception.ErroSolicitacaoEmissaoCartaoException;
import microsservico.udemy.msavaliadorcredito.application.service.AvaliadorService;
import microsservico.udemy.msavaliadorcredito.domain.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("avaliacoes-credito")
@RequiredArgsConstructor
public class AvaliadorCreditoController {


    private final AvaliadorService avaliadorService;

    @GetMapping
    public String status(){
        return "ok";
    }

    @GetMapping(value = "situacao-cliente", params = "cpf")
    public ResponseEntity consultarSituacaoCliente(@RequestParam("cpf") String cpf){
        try {
            SituacaoCliente situacaoCliente = avaliadorService.obterSituacaoCliente(cpf);
            return ResponseEntity.ok(situacaoCliente);
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicrosservicesException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao dadosAvaliacao){
        try {
            RetornoAvaliacaoCliente avaliacaoCliente = avaliadorService.realizarAvaliacao(dadosAvaliacao.getCpf(), dadosAvaliacao.getRenda());
            return ResponseEntity.ok(avaliacaoCliente);
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicrosservicesException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping(value = "solicitacoes-cartao")
    public ResponseEntity solicitarCartao(@RequestBody DadosSolicitacaoEmissaoCartao dados){
        try{
            ProtocoloSolicitacaoCartao protocoloSolicitacaoCartao = avaliadorService.solicitarEmissaoCartao(dados);
            return ResponseEntity.ok(protocoloSolicitacaoCartao);
        }catch (ErroSolicitacaoEmissaoCartaoException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
