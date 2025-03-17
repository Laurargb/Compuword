package io.bootify.compu_word.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EmpleadoDTO {

    @Size(max = 255)
    @EmpleadoNombreValid
    private String nombre;

    private Integer id;

    @Size(max = 255)
    private String correoElectronico;

    @Size(max = 255)
    private String departament;

    @Size(max = 255)
    private String empleado1;

    @NotNull
    @Size(max = 255)
    @EmpleadoEmpleado2Unique
    private String empleado2;

}
