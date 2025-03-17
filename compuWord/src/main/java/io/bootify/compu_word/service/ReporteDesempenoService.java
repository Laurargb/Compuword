package io.bootify.compu_word.service;

import io.bootify.compu_word.domain.Empleado;
import io.bootify.compu_word.domain.ReporteDesempeno;
import io.bootify.compu_word.model.ReporteDesempenoDTO;
import io.bootify.compu_word.repos.EmpleadoRepository;
import io.bootify.compu_word.repos.ReporteDesempenoRepository;
import io.bootify.compu_word.util.NotFoundException;
import io.bootify.compu_word.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ReporteDesempenoService {

    private final ReporteDesempenoRepository reporteDesempenoRepository;
    private final EmpleadoRepository empleadoRepository;

    public ReporteDesempenoService(final ReporteDesempenoRepository reporteDesempenoRepository,
            final EmpleadoRepository empleadoRepository) {
        this.reporteDesempenoRepository = reporteDesempenoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    public List<ReporteDesempenoDTO> findAll() {
        final List<ReporteDesempeno> reporteDesempenoes = reporteDesempenoRepository.findAll(Sort.by("datosEmpleado"));
        return reporteDesempenoes.stream()
                .map(reporteDesempeno -> mapToDTO(reporteDesempeno, new ReporteDesempenoDTO()))
                .toList();
    }

    public ReporteDesempenoDTO get(final String datosEmpleado) {
        return reporteDesempenoRepository.findById(datosEmpleado)
                .map(reporteDesempeno -> mapToDTO(reporteDesempeno, new ReporteDesempenoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final ReporteDesempenoDTO reporteDesempenoDTO) {
        final ReporteDesempeno reporteDesempeno = new ReporteDesempeno();
        mapToEntity(reporteDesempenoDTO, reporteDesempeno);
        reporteDesempeno.setDatosEmpleado(reporteDesempenoDTO.getDatosEmpleado());
        return reporteDesempenoRepository.save(reporteDesempeno).getDatosEmpleado();
    }

    public void update(final String datosEmpleado, final ReporteDesempenoDTO reporteDesempenoDTO) {
        final ReporteDesempeno reporteDesempeno = reporteDesempenoRepository.findById(datosEmpleado)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reporteDesempenoDTO, reporteDesempeno);
        reporteDesempenoRepository.save(reporteDesempeno);
    }

    public void delete(final String datosEmpleado) {
        reporteDesempenoRepository.deleteById(datosEmpleado);
    }

    private ReporteDesempenoDTO mapToDTO(final ReporteDesempeno reporteDesempeno,
            final ReporteDesempenoDTO reporteDesempenoDTO) {
        reporteDesempenoDTO.setDatosEmpleado(reporteDesempeno.getDatosEmpleado());
        reporteDesempenoDTO.setDatosDepartamento(reporteDesempeno.getDatosDepartamento());
        reporteDesempenoDTO.setEmpleado(reporteDesempeno.getEmpleado());
        reporteDesempenoDTO.setDepartamento(reporteDesempeno.getDepartamento());
        return reporteDesempenoDTO;
    }

    private ReporteDesempeno mapToEntity(final ReporteDesempenoDTO reporteDesempenoDTO,
            final ReporteDesempeno reporteDesempeno) {
        reporteDesempeno.setDatosDepartamento(reporteDesempenoDTO.getDatosDepartamento());
        reporteDesempeno.setEmpleado(reporteDesempenoDTO.getEmpleado());
        reporteDesempeno.setDepartamento(reporteDesempenoDTO.getDepartamento());
        return reporteDesempeno;
    }

    public boolean datosEmpleadoExists(final String datosEmpleado) {
        return reporteDesempenoRepository.existsByDatosEmpleadoIgnoreCase(datosEmpleado);
    }

    public ReferencedWarning getReferencedWarning(final String datosEmpleado) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final ReporteDesempeno reporteDesempeno = reporteDesempenoRepository.findById(datosEmpleado)
                .orElseThrow(NotFoundException::new);
        final Empleado empleado2Empleado = empleadoRepository.findFirstByEmpleado2(reporteDesempeno);
        if (empleado2Empleado != null) {
            referencedWarning.setKey("reporteDesempeno.empleado.empleado2.referenced");
            referencedWarning.addParam(empleado2Empleado.getNombre());
            return referencedWarning;
        }
        return null;
    }

}
