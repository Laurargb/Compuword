package io.bootify.compu_word.controller;

import io.bootify.compu_word.domain.Empleado;
import io.bootify.compu_word.domain.ReporteDesempeno;
import io.bootify.compu_word.model.EmpleadoDTO;
import io.bootify.compu_word.repos.EmpleadoRepository;
import io.bootify.compu_word.repos.ReporteDesempenoRepository;
import io.bootify.compu_word.service.EmpleadoService;
import io.bootify.compu_word.util.CustomCollectors;
import io.bootify.compu_word.util.ReferencedWarning;
import io.bootify.compu_word.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;
    private final EmpleadoRepository empleadoRepository;
    private final ReporteDesempenoRepository reporteDesempenoRepository;

    public EmpleadoController(final EmpleadoService empleadoService,
            final EmpleadoRepository empleadoRepository,
            final ReporteDesempenoRepository reporteDesempenoRepository) {
        this.empleadoService = empleadoService;
        this.empleadoRepository = empleadoRepository;
        this.reporteDesempenoRepository = reporteDesempenoRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("empleado1Values", empleadoRepository.findAll(Sort.by("nombre"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Empleado::getNombre, Empleado::getNombre)));
        model.addAttribute("empleado2Values", reporteDesempenoRepository.findAll(Sort.by("datosEmpleado"))
                .stream()
                .collect(CustomCollectors.toSortedMap(ReporteDesempeno::getDatosEmpleado, ReporteDesempeno::getDatosEmpleado)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("empleadoes", empleadoService.findAll());
        return "empleado/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("empleado") final EmpleadoDTO empleadoDTO) {
        return "empleado/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("empleado") @Valid final EmpleadoDTO empleadoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "empleado/add";
        }
        empleadoService.create(empleadoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("empleado.create.success"));
        return "redirect:/empleados";
    }

    @GetMapping("/edit/{nombre}")
    public String edit(@PathVariable(name = "nombre") final String nombre, final Model model) {
        model.addAttribute("empleado", empleadoService.get(nombre));
        return "empleado/edit";
    }

    @PostMapping("/edit/{nombre}")
    public String edit(@PathVariable(name = "nombre") final String nombre,
            @ModelAttribute("empleado") @Valid final EmpleadoDTO empleadoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "empleado/edit";
        }
        empleadoService.update(nombre, empleadoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("empleado.update.success"));
        return "redirect:/empleados";
    }

    @PostMapping("/delete/{nombre}")
    public String delete(@PathVariable(name = "nombre") final String nombre,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = empleadoService.getReferencedWarning(nombre);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            empleadoService.delete(nombre);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("empleado.delete.success"));
        }
        return "redirect:/empleados";
    }

}
