package io.bootify.compu_word.repos;

import io.bootify.compu_word.domain.ReporteDesempeno;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReporteDesempenoRepository extends JpaRepository<ReporteDesempeno, String> {

    boolean existsByDatosEmpleadoIgnoreCase(String datosEmpleado);

}
