package microsservico.udemy.msclientes.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microsservico.udemy.msclientes.application.representation.ClienteSaveRequest;
import microsservico.udemy.msclientes.domain.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;
    @GetMapping
    public String status(){
        log.info("Obtendo status do microservice de clientes");
        return "OK";
    }

    @PostMapping
    public ResponseEntity<Cliente> salvarCliente(@RequestBody ClienteSaveRequest request){
        Cliente cliente = request.toModel();
        clienteService.save(cliente);
        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("cpf={cpf}")
                .buildAndExpand(cliente.getCpf())
                .toUri();
        return ResponseEntity.created(headerLocation).build();
    }

    @GetMapping(params = "cpf")
    public ResponseEntity dadosCliente(@RequestParam("cpf") String cpf){
        Optional<Cliente> cliente = clienteService.getByCpf(cpf);
        if(cliente.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }
}
