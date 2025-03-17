package io.bootify.compu_word.repos;

import io.bootify.compu_word.domain.Empleado;
import io.bootify.compu_word.domain.ReporteDesempeno;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmpleadoRepository extends JpaRepository<Empleado, String> {

    Empleado findFirstByEmpleado1AndNombreNot(Empleado empleado, final String nombre);

    Empleado findFirstByEmpleado2(ReporteDesempeno reporteDesempeno);

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByEmpleado2DatosEmpleadoIgnoreCase(String datosEmpleado);

}
