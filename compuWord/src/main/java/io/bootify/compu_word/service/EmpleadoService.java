package io.bootify.compu_word.service;

import io.bootify.compu_word.domain.Empleado;
import io.bootify.compu_word.domain.Permanente;
import io.bootify.compu_word.domain.ReporteDesempeno;
import io.bootify.compu_word.model.EmpleadoDTO;
import io.bootify.compu_word.repos.EmpleadoRepository;
import io.bootify.compu_word.repos.PermanenteRepository;
import io.bootify.compu_word.repos.ReporteDesempenoRepository;
import io.bootify.compu_word.util.NotFoundException;
import io.bootify.compu_word.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final ReporteDesempenoRepository reporteDesempenoRepository;
    private final PermanenteRepository permanenteRepository;

    public EmpleadoService(final EmpleadoRepository empleadoRepository,
            final ReporteDesempenoRepository reporteDesempenoRepository,
            final PermanenteRepository permanenteRepository) {
        this.empleadoRepository = empleadoRepository;
        this.reporteDesempenoRepository = reporteDesempenoRepository;
        this.permanenteRepository = permanenteRepository;
    }

    public List<EmpleadoDTO> findAll() {
        final List<Empleado> empleadoes = empleadoRepository.findAll(Sort.by("nombre"));
        return empleadoes.stream()
                .map(empleado -> mapToDTO(empleado, new EmpleadoDTO()))
                .toList();
    }

    public EmpleadoDTO get(final String nombre) {
        return empleadoRepository.findById(nombre)
                .map(empleado -> mapToDTO(empleado, new EmpleadoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final EmpleadoDTO empleadoDTO) {
        final Empleado empleado = new Empleado();
        mapToEntity(empleadoDTO, empleado);
        empleado.setNombre(empleadoDTO.getNombre());
        return empleadoRepository.save(empleado).getNombre();
    }

    public void update(final String nombre, final EmpleadoDTO empleadoDTO) {
        final Empleado empleado = empleadoRepository.findById(nombre)
                .orElseThrow(NotFoundException::new);
        mapToEntity(empleadoDTO, empleado);
        empleadoRepository.save(empleado);
    }

    public void delete(final String nombre) {
        empleadoRepository.deleteById(nombre);
    }

    private EmpleadoDTO mapToDTO(final Empleado empleado, final EmpleadoDTO empleadoDTO) {
        empleadoDTO.setNombre(empleado.getNombre());
        empleadoDTO.setId(empleado.getId());
        empleadoDTO.setCorreoElectronico(empleado.getCorreoElectronico());
        empleadoDTO.setDepartament(empleado.getDepartament());
        empleadoDTO.setEmpleado1(empleado.getEmpleado1() == null ? null : empleado.getEmpleado1().getNombre());
        empleadoDTO.setEmpleado2(empleado.getEmpleado2() == null ? null : empleado.getEmpleado2().getDatosEmpleado());
        return empleadoDTO;
    }

    private Empleado mapToEntity(final EmpleadoDTO empleadoDTO, final Empleado empleado) {
        empleado.setId(empleadoDTO.getId());
        empleado.setCorreoElectronico(empleadoDTO.getCorreoElectronico());
        empleado.setDepartament(empleadoDTO.getDepartament());
        final Empleado empleado1 = empleadoDTO.getEmpleado1() == null ? null : empleadoRepository.findById(empleadoDTO.getEmpleado1())
                .orElseThrow(() -> new NotFoundException("empleado1 not found"));
        empleado.setEmpleado1(empleado1);
        final ReporteDesempeno empleado2 = empleadoDTO.getEmpleado2() == null ? null : reporteDesempenoRepository.findById(empleadoDTO.getEmpleado2())
                .orElseThrow(() -> new NotFoundException("empleado2 not found"));
        empleado.setEmpleado2(empleado2);
        return empleado;
    }

    public boolean nombreExists(final String nombre) {
        return empleadoRepository.existsByNombreIgnoreCase(nombre);
    }

    public boolean empleado2Exists(final String datosEmpleado) {
        return empleadoRepository.existsByEmpleado2DatosEmpleadoIgnoreCase(datosEmpleado);
    }

    public ReferencedWarning getReferencedWarning(final String nombre) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Empleado empleado = empleadoRepository.findById(nombre)
                .orElseThrow(NotFoundException::new);
        final Permanente permanentePermanente = permanenteRepository.findFirstByPermanente(empleado);
        if (permanentePermanente != null) {
            referencedWarning.setKey("empleado.permanente.permanente.referenced");
            referencedWarning.addParam(permanentePermanente.getContratoAnual());
            return referencedWarning;
        }
        final Empleado empleado1Empleado = empleadoRepository.findFirstByEmpleado1AndNombreNot(empleado, empleado.getNombre());
        if (empleado1Empleado != null) {
            referencedWarning.setKey("empleado.empleado.empleado1.referenced");
            referencedWarning.addParam(empleado1Empleado.getNombre());
            return referencedWarning;
        }
        return null;
    }

}
