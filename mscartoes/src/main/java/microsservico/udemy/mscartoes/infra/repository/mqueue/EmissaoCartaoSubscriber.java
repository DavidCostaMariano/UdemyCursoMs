package microsservico.udemy.mscartoes.infra.repository.mqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microsservico.udemy.mscartoes.domain.Cartao;
import microsservico.udemy.mscartoes.domain.ClienteCartao;
import microsservico.udemy.mscartoes.domain.DadosSolicitacaoEmissaoCartao;
import microsservico.udemy.mscartoes.infra.repository.CartaoRepository;
import microsservico.udemy.mscartoes.infra.repository.ClienteCartaoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class EmissaoCartaoSubscriber {

    private final CartaoRepository cartaoRepository;
    private final ClienteCartaoRepository clienteCartaoRepository;
    @RabbitListener(queues = "${mq.queues.emissao-cartoes}")
    public void receberSolicitacaoEmissao(@Payload String payload){
        try{
            var mapper = new ObjectMapper();
            DadosSolicitacaoEmissaoCartao dados = mapper.readValue(payload, DadosSolicitacaoEmissaoCartao.class);
            Cartao cartao = cartaoRepository.findById(dados.getIdCartao())
                    .orElseThrow();
            ClienteCartao clienteCartao = new ClienteCartao();
            clienteCartao.setCartao(cartao);
            clienteCartao.setCpf(dados.getCpf());
            clienteCartao.setLimite(dados.getLimiteLiberado());
            clienteCartao.setCpf(dados.getCpf());
            clienteCartaoRepository.save(clienteCartao);
        }catch (Exception e){
            log.error("Erro ao receber solicitação de emissao de cartao: {}", e.getMessage());
        }
    }

}
