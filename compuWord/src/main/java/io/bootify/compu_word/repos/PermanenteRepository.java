package io.bootify.compu_word.repos;

import io.bootify.compu_word.domain.Empleado;
import io.bootify.compu_word.domain.Permanente;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PermanenteRepository extends JpaRepository<Permanente, String> {

    Permanente findFirstByPermanente(Empleado empleado);

    boolean existsByContratoAnualIgnoreCase(String contratoAnual);

    boolean existsByPermanenteNombreIgnoreCase(String nombre);

}
