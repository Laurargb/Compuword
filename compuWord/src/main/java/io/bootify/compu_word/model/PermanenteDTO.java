package io.bootify.compu_word.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PermanenteDTO {

    @Size(max = 255)
    @PermanenteContratoAnualValid
    private String contratoAnual;

    private Integer sueldo;

    private LocalDateTime fechaIngreso;

    @Size(max = 255)
    private String cargo;

    @NotNull
    @Size(max = 255)
    @PermanentePermanenteUnique
    private String permanente;

}
