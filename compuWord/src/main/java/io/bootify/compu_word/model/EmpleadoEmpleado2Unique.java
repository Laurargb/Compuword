package io.bootify.compu_word.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import io.bootify.compu_word.service.EmpleadoService;
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
 * Validate that the datosEmpleado value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = EmpleadoEmpleado2Unique.EmpleadoEmpleado2UniqueValidator.class
)
public @interface EmpleadoEmpleado2Unique {

    String message() default "{Exists.empleado.empleado2}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class EmpleadoEmpleado2UniqueValidator implements ConstraintValidator<EmpleadoEmpleado2Unique, String> {

        private final EmpleadoService empleadoService;
        private final HttpServletRequest request;

        public EmpleadoEmpleado2UniqueValidator(final EmpleadoService empleadoService,
                final HttpServletRequest request) {
            this.empleadoService = empleadoService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("nombre");
            if (currentId != null && value.equalsIgnoreCase(empleadoService.get(currentId).getEmpleado2())) {
                // value hasn't changed
                return true;
            }
            return !empleadoService.empleado2Exists(value);
        }

    }

}
