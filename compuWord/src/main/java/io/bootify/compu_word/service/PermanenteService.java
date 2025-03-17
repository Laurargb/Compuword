package io.bootify.compu_word.service;

import io.bootify.compu_word.domain.Empleado;
import io.bootify.compu_word.domain.Permanente;
import io.bootify.compu_word.model.PermanenteDTO;
import io.bootify.compu_word.repos.EmpleadoRepository;
import io.bootify.compu_word.repos.PermanenteRepository;
import io.bootify.compu_word.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PermanenteService {

    private final PermanenteRepository permanenteRepository;
    private final EmpleadoRepository empleadoRepository;

    public PermanenteService(final PermanenteRepository permanenteRepository,
            final EmpleadoRepository empleadoRepository) {
        this.permanenteRepository = permanenteRepository;
        this.empleadoRepository = empleadoRepository;
    }

    public List<PermanenteDTO> findAll() {
        final List<Permanente> permanentes = permanenteRepository.findAll(Sort.by("contratoAnual"));
        return permanentes.stream()
                .map(permanente -> mapToDTO(permanente, new PermanenteDTO()))
                .toList();
    }

    public PermanenteDTO get(final String contratoAnual) {
        return permanenteRepository.findById(contratoAnual)
                .map(permanente -> mapToDTO(permanente, new PermanenteDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final PermanenteDTO permanenteDTO) {
        final Permanente permanente = new Permanente();
        mapToEntity(permanenteDTO, permanente);
        permanente.setContratoAnual(permanenteDTO.getContratoAnual());
        return permanenteRepository.save(permanente).getContratoAnual();
    }

    public void update(final String contratoAnual, final PermanenteDTO permanenteDTO) {
        final Permanente permanente = permanenteRepository.findById(contratoAnual)
                .orElseThrow(NotFoundException::new);
        mapToEntity(permanenteDTO, permanente);
        permanenteRepository.save(permanente);
    }

    public void delete(final String contratoAnual) {
        permanenteRepository.deleteById(contratoAnual);
    }

    private PermanenteDTO mapToDTO(final Permanente permanente, final PermanenteDTO permanenteDTO) {
        permanenteDTO.setContratoAnual(permanente.getContratoAnual());
        permanenteDTO.setSueldo(permanente.getSueldo());
        permanenteDTO.setFechaIngreso(permanente.getFechaIngreso());
        permanenteDTO.setCargo(permanente.getCargo());
        permanenteDTO.setPermanente(permanente.getPermanente() == null ? null : permanente.getPermanente().getNombre());
        return permanenteDTO;
    }

    private Permanente mapToEntity(final PermanenteDTO permanenteDTO, final Permanente permanente) {
        permanente.setSueldo(permanenteDTO.getSueldo());
        permanente.setFechaIngreso(permanenteDTO.getFechaIngreso());
        permanente.setCargo(permanenteDTO.getCargo());


        
        final Empleado empleado = permanenteDTO.getPermanente() == null ? null : empleadoRepository.findById(permanenteDTO.getPermanente())
                .orElseThrow(() -> new NotFoundException("permanente not found"));

        permanente.setPermanente(empleado);
        return permanente;
    }

    public boolean contratoAnualExists(final String contratoAnual) {
        return permanenteRepository.existsByContratoAnualIgnoreCase(contratoAnual);
    }

    public boolean permanenteExists(final String nombre) {
        return permanenteRepository.existsByPermanenteNombreIgnoreCase(nombre);
    }

}
