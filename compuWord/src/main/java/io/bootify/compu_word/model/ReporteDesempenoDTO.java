package io.bootify.compu_word.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReporteDesempenoDTO {

    @Size(max = 255)
    @ReporteDesempenoDatosEmpleadoValid
    private String datosEmpleado;

    private String datosDepartamento;

    private String empleado;

    private String departamento;

}
