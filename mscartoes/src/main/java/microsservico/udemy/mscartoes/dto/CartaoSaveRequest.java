package microsservico.udemy.mscartoes.dto;

import lombok.Data;
import microsservico.udemy.mscartoes.domain.BandeiraCartao;
import microsservico.udemy.mscartoes.domain.Cartao;

import java.math.BigDecimal;

@Data
public class CartaoSaveRequest {

    private String nome;

    private BandeiraCartao bandeira;

    private BigDecimal renda;

    private BigDecimal limite;



    public Cartao toModel(){
        return new Cartao(nome, bandeira, renda, limite);
    }
}
