package microsservico.udemy.mscartoes.service;


import lombok.RequiredArgsConstructor;
import microsservico.udemy.mscartoes.domain.ClienteCartao;
import microsservico.udemy.mscartoes.infra.repository.ClienteCartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteCartaoService {

    @Autowired
    private ClienteCartaoRepository cartaoRepository;
    public List<ClienteCartao> listCartoesByCpf(String cpf){
        return cartaoRepository.findByCpf(cpf);
    }

}
