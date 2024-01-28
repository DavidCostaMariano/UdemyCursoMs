package microsservico.udemy.msavaliadorcredito.domain.model;

import lombok.Data;

@Data
public class DadosCliente {

    private Long id;
    private String nome;

    private int idade;

}
