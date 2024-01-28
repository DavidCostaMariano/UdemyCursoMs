package microsservico.udemy.msavaliadorcredito.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import microsservico.udemy.msavaliadorcredito.application.exception.DadosClienteNotFoundException;
import microsservico.udemy.msavaliadorcredito.application.exception.ErroComunicacaoMicrosservicesException;
import microsservico.udemy.msavaliadorcredito.application.exception.ErroSolicitacaoEmissaoCartaoException;
import microsservico.udemy.msavaliadorcredito.domain.model.*;
import microsservico.udemy.msavaliadorcredito.infra.clients.CartoesResourceClient;
import microsservico.udemy.msavaliadorcredito.infra.clients.ClienteResourceClient;
import microsservico.udemy.msavaliadorcredito.infra.clients.mqueue.SolicitacaoEmissaoCartaoPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorService {


    private final ClienteResourceClient clienteResourceClient;
    private final CartoesResourceClient cartoesResourceClient;

    private final SolicitacaoEmissaoCartaoPublisher emissaoCartaoPublisher;
    public SituacaoCliente obterSituacaoCliente(String cpf)
            throws DadosClienteNotFoundException, ErroComunicacaoMicrosservicesException{
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clienteResourceClient.dadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> dadosCartaoResponse = cartoesResourceClient.getCartoesByCliente(cpf);
            return SituacaoCliente
                    .builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cartoes(dadosCartaoResponse.getBody())
                    .build();
        }catch (FeignException.FeignClientException e){
            int status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status){
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicrosservicesException(e.getMessage(), status);
        }
    }

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda)
        throws DadosClienteNotFoundException, ErroComunicacaoMicrosservicesException{
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clienteResourceClient.dadosCliente(cpf);
            ResponseEntity<List<Cartao>> cartoesResponse = cartoesResourceClient.getCartoesRendaAte(renda);
            List<Cartao> cartoes = cartoesResponse.getBody();
            List<CartaoAprovado> cartaoAprovados = cartoes.stream().map(cartao ->{
                DadosCliente dadosCliente = dadosClienteResponse.getBody();
                CartaoAprovado aprovado = new CartaoAprovado();
                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());
                BigDecimal fator = idadeBD.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);
                aprovado.setCartao(cartao.getNome());
                aprovado.setBandeira(cartao.getBandeira());
                aprovado.setLimiteAprovado(limiteAprovado);
                return aprovado;
            }).collect(Collectors.toList());
            return new RetornoAvaliacaoCliente(cartaoAprovados);
        }catch (FeignException.FeignClientException e){
            int status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status){
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicrosservicesException(e.getMessage(), status);
        }
    }

    public ProtocoloSolicitacaoCartao solicitarEmissaoCartao(DadosSolicitacaoEmissaoCartao dados){
        try{
            emissaoCartaoPublisher.solicitarCartao(dados);
            var protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);
        }catch (Exception e){
            throw new ErroSolicitacaoEmissaoCartaoException(e.getMessage());
        }
    }

}

