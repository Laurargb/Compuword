package io.bootify.compu_word.controller;

import io.bootify.compu_word.model.ReporteDesempenoDTO;
import io.bootify.compu_word.service.ReporteDesempenoService;
import io.bootify.compu_word.util.ReferencedWarning;
import io.bootify.compu_word.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/reporteDesempenos")
public class ReporteDesempenoController {

    private final ReporteDesempenoService reporteDesempenoService;

    public ReporteDesempenoController(final ReporteDesempenoService reporteDesempenoService) {
        this.reporteDesempenoService = reporteDesempenoService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("reporteDesempenoes", reporteDesempenoService.findAll());
        return "reporteDesempeno/list";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("reporteDesempeno") final ReporteDesempenoDTO reporteDesempenoDTO) {
        return "reporteDesempeno/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("reporteDesempeno") @Valid final ReporteDesempenoDTO reporteDesempenoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reporteDesempeno/add";
        }
        reporteDesempenoService.create(reporteDesempenoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reporteDesempeno.create.success"));
        return "redirect:/reporteDesempenos";
    }

    @GetMapping("/edit/{datosEmpleado}")
    public String edit(@PathVariable(name = "datosEmpleado") final String datosEmpleado,
            final Model model) {
        model.addAttribute("reporteDesempeno", reporteDesempenoService.get(datosEmpleado));
        return "reporteDesempeno/edit";
    }

    @PostMapping("/edit/{datosEmpleado}")
    public String edit(@PathVariable(name = "datosEmpleado") final String datosEmpleado,
            @ModelAttribute("reporteDesempeno") @Valid final ReporteDesempenoDTO reporteDesempenoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reporteDesempeno/edit";
        }
        reporteDesempenoService.update(datosEmpleado, reporteDesempenoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reporteDesempeno.update.success"));
        return "redirect:/reporteDesempenos";
    }

    @PostMapping("/delete/{datosEmpleado}")
    public String delete(@PathVariable(name = "datosEmpleado") final String datosEmpleado,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = reporteDesempenoService.getReferencedWarning(datosEmpleado);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            reporteDesempenoService.delete(datosEmpleado);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("reporteDesempeno.delete.success"));
        }
        return "redirect:/reporteDesempenos";
    }

}
