package io.bootify.compu_word.rest;

import io.bootify.compu_word.model.EmpleadoDTO;
import io.bootify.compu_word.service.EmpleadoService;
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
@RequestMapping(value = "/api/empleados", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmpleadoResource {

    private final EmpleadoService empleadoService;

    public EmpleadoResource(final EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public ResponseEntity<List<EmpleadoDTO>> getAllEmpleados() {
        return ResponseEntity.ok(empleadoService.findAll());
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<EmpleadoDTO> getEmpleado(
            @PathVariable(name = "nombre") final String nombre) {
        return ResponseEntity.ok(empleadoService.get(nombre));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<String> createEmpleado(
            @RequestBody @Valid final EmpleadoDTO empleadoDTO) {
        final String createdNombre = empleadoService.create(empleadoDTO);
        return new ResponseEntity<>('"' + createdNombre + '"', HttpStatus.CREATED);
    }

    @PutMapping("/{nombre}")
    public ResponseEntity<String> updateEmpleado(@PathVariable(name = "nombre") final String nombre,
            @RequestBody @Valid final EmpleadoDTO empleadoDTO) {
        empleadoService.update(nombre, empleadoDTO);
        return ResponseEntity.ok('"' + nombre + '"');
    }

    @DeleteMapping("/{nombre}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteEmpleado(@PathVariable(name = "nombre") final String nombre) {
        final ReferencedWarning referencedWarning = empleadoService.getReferencedWarning(nombre);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        empleadoService.delete(nombre);
        return ResponseEntity.noContent().build();
    }

}
