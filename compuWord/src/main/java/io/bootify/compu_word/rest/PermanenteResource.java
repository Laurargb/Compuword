package io.bootify.compu_word.rest;

import io.bootify.compu_word.model.PermanenteDTO;
import io.bootify.compu_word.service.PermanenteService;
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
@RequestMapping(value = "/api/permanentes", produces = MediaType.APPLICATION_JSON_VALUE)
public class PermanenteResource {

    private final PermanenteService permanenteService;

    public PermanenteResource(final PermanenteService permanenteService) {
        this.permanenteService = permanenteService;
    }

    @GetMapping
    public ResponseEntity<List<PermanenteDTO>> getAllPermanentes() {
        return ResponseEntity.ok(permanenteService.findAll());
    }

    @GetMapping("/{contratoAnual}")
    public ResponseEntity<PermanenteDTO> getPermanente(
            @PathVariable(name = "contratoAnual") final String contratoAnual) {
        return ResponseEntity.ok(permanenteService.get(contratoAnual));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<String> createPermanente(
            @RequestBody @Valid final PermanenteDTO permanenteDTO) {
        final String createdContratoAnual = permanenteService.create(permanenteDTO);
        return new ResponseEntity<>('"' + createdContratoAnual + '"', HttpStatus.CREATED);
    }

    @PutMapping("/{contratoAnual}")
    public ResponseEntity<String> updatePermanente(
            @PathVariable(name = "contratoAnual") final String contratoAnual,
            @RequestBody @Valid final PermanenteDTO permanenteDTO) {
        permanenteService.update(contratoAnual, permanenteDTO);
        return ResponseEntity.ok('"' + contratoAnual + '"');
    }

    @DeleteMapping("/{contratoAnual}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePermanente(
            @PathVariable(name = "contratoAnual") final String contratoAnual) {
        permanenteService.delete(contratoAnual);
        return ResponseEntity.noContent().build();
    }

}
