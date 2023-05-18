package microsservico.udemy.msclientes.application;

import lombok.RequiredArgsConstructor;
import microsservico.udemy.msclientes.domain.Cliente;
import microsservico.udemy.msclientes.infra.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {


    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente save(Cliente cliente){
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> getByCpf(String cpf){
        return  clienteRepository.findByCpf(cpf);
    }

}
