package io.bootify.compu_word.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import io.bootify.compu_word.service.ReporteDesempenoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Check that datosEmpleado is present and available when a new ReporteDesempeno is created.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = ReporteDesempenoDatosEmpleadoValid.ReporteDesempenoDatosEmpleadoValidValidator.class
)
public @interface ReporteDesempenoDatosEmpleadoValid {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class ReporteDesempenoDatosEmpleadoValidValidator implements ConstraintValidator<ReporteDesempenoDatosEmpleadoValid, String> {

        private final ReporteDesempenoService reporteDesempenoService;
        private final HttpServletRequest request;

        public ReporteDesempenoDatosEmpleadoValidValidator(
                final ReporteDesempenoService reporteDesempenoService,
                final HttpServletRequest request) {
            this.reporteDesempenoService = reporteDesempenoService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("datosEmpleado");
            if (currentId != null) {
                // only relevant for new objects
                return true;
            }
            String error = null;
            if (value == null) {
                // missing input
                error = "NotNull";
            } else if (reporteDesempenoService.datosEmpleadoExists(value)) {
                error = "Exists.reporteDesempeno.datosEmpleado";
            }
            if (error != null) {
                cvContext.disableDefaultConstraintViolation();
                cvContext.buildConstraintViolationWithTemplate("{" + error + "}")
                        .addConstraintViolation();
                return false;
            }
            return true;
        }

    }

}
