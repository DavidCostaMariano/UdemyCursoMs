package microsservico.udemy.msavaliadorcredito.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetornoAvaliacaoCliente {

    private List<CartaoAprovado> cartoes;

}
