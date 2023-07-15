package microsservico.udemy.mscartoes.controller;

import microsservico.udemy.mscartoes.domain.Cartao;
import microsservico.udemy.mscartoes.domain.ClienteCartao;
import microsservico.udemy.mscartoes.dto.CartaoSaveRequest;
import microsservico.udemy.mscartoes.dto.CartoesPorClienteResponse;
import microsservico.udemy.mscartoes.service.CartaoService;
import microsservico.udemy.mscartoes.service.ClienteCartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("cartoes")
public class CartoesController {

    @Autowired
    private CartaoService cartaoService;
    @Autowired
    private ClienteCartaoService clienteCartaoService;

    @GetMapping
    public String status(){
        return "ok";
    }

    @PostMapping
    public ResponseEntity cadastra(@RequestBody CartaoSaveRequest request) {
        Cartao cartao = request.toModel();
        cartaoService.save(cartao);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(params = "renda")
    public ResponseEntity getCartoesRendaAte(@RequestParam("renda") Long renda){
        List<Cartao> cartoesRendaMenorIgual = cartaoService.getCartoesRendaMenorIgual(renda);
        return ResponseEntity.ok(cartoesRendaMenorIgual);
    }


    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesPorClienteResponse>> getCartoesByCliente(String cpf){
        List<ClienteCartao> clienteCartoes = clienteCartaoService.listCartoesByCpf(cpf);
        List<CartoesPorClienteResponse> resultList = clienteCartoes.stream()
                .map(CartoesPorClienteResponse::fromModel)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

}
