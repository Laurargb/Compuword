package io.bootify.compu_word.controller;

import io.bootify.compu_word.domain.Empleado;
import io.bootify.compu_word.model.PermanenteDTO;
import io.bootify.compu_word.repos.EmpleadoRepository;
import io.bootify.compu_word.service.PermanenteService;
import io.bootify.compu_word.util.CustomCollectors;
import io.bootify.compu_word.util.WebUtils;
import jakarta.validation.Valid;

import java.util.Arrays;
import java.util.List;

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
@RequestMapping("/permanentes")
public class PermanenteController {

    private final PermanenteService permanenteService;
    private final EmpleadoRepository empleadoRepository;

    public PermanenteController(final PermanenteService permanenteService,
            final EmpleadoRepository empleadoRepository) {
        this.permanenteService = permanenteService;
        this.empleadoRepository = empleadoRepository;
    }

    // Clase interna para manejar las opciones del select
    public static class TipoPermanente {
        private final Long id;
        private final String nombre;

        public TipoPermanente(Long id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public Long getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("permanenteValues", empleadoRepository.findAll(Sort.by("nombre"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Empleado::getNombre, Empleado::getNombre)));
        
        // Agregar las opciones para el select
        model.addAttribute("tipoPermanentes", getOpcionesPermanente());
    }

    private List<TipoPermanente> getOpcionesPermanente() {
        return Arrays.asList(
            new TipoPermanente(1L, "Temporal"),
            new TipoPermanente(2L, "Contrato fijo"),
            new TipoPermanente(3L, "Indefinido")
        );
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("permanentes", permanenteService.findAll());
        return "permanente/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("permanente") final PermanenteDTO permanenteDTO) {
        return "permanente/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("permanente") @Valid final PermanenteDTO permanenteDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "permanente/add";
        }
        permanenteService.create(permanenteDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("permanente.create.success"));
        return "redirect:/permanentes";
    }

    @GetMapping("/edit/{contratoAnual}")
    public String edit(@PathVariable(name = "contratoAnual") final String contratoAnual,
            final Model model) {
        model.addAttribute("permanente", permanenteService.get(contratoAnual));
        return "permanente/edit";
    }

    @PostMapping("/edit/{contratoAnual}")
    public String edit(@PathVariable(name = "contratoAnual") final String contratoAnual,
            @ModelAttribute("permanente") @Valid final PermanenteDTO permanenteDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "permanente/edit";
        }
        permanenteService.update(contratoAnual, permanenteDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("permanente.update.success"));
        return "redirect:/permanentes";
    }

    @PostMapping("/delete/{contratoAnual}")
    public String delete(@PathVariable(name = "contratoAnual") final String contratoAnual,
            final RedirectAttributes redirectAttributes) {
        permanenteService.delete(contratoAnual);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("permanente.delete.success"));
        return "redirect:/permanentes";
    }
}