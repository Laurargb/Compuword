package io.bootify.compu_word.rest;

import io.bootify.compu_word.model.ReporteDesempenoDTO;
import io.bootify.compu_word.service.ReporteDesempenoService;
import io.bootify.compu_word.util.ReferencedException;
import io.bootify.compu_word.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/reporteDesempenos", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReporteDesempenoResource {

    private final ReporteDesempenoService reporteDesempenoService;

    public ReporteDesempenoResource(final ReporteDesempenoService reporteDesempenoService) {
        this.reporteDesempenoService = reporteDesempenoService;
    }

    @GetMapping
    public ResponseEntity<List<ReporteDesempenoDTO>> getAllReporteDesempenos() {
        return ResponseEntity.ok(reporteDesempenoService.findAll());
    }

    @GetMapping("/{datosEmpleado}")
    public ResponseEntity<ReporteDesempenoDTO> getReporteDesempeno(
            @PathVariable(name = "datosEmpleado") final String datosEmpleado) {
        return ResponseEntity.ok(reporteDesempenoService.get(datosEmpleado));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<String> createReporteDesempeno(
            @RequestBody @Valid final ReporteDesempenoDTO reporteDesempenoDTO) {
        final String createdDatosEmpleado = reporteDesempenoService.create(reporteDesempenoDTO);
        return new ResponseEntity<>('"' + createdDatosEmpleado + '"', HttpStatus.CREATED);
    }

    @PutMapping("/{datosEmpleado}")
    public ResponseEntity<String> updateReporteDesempeno(
            @PathVariable(name = "datosEmpleado") final String datosEmpleado,
            @RequestBody @Valid final ReporteDesempenoDTO reporteDesempenoDTO) {
        reporteDesempenoService.update(datosEmpleado, reporteDesempenoDTO);
        return ResponseEntity.ok('"' + datosEmpleado + '"');
    }

    @DeleteMapping("/{datosEmpleado}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteReporteDesempeno(
            @PathVariable(name = "datosEmpleado") final String datosEmpleado) {
        final ReferencedWarning referencedWarning = reporteDesempenoService.getReferencedWarning(datosEmpleado);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        reporteDesempenoService.delete(datosEmpleado);
        return ResponseEntity.noContent().build();
    }

}
