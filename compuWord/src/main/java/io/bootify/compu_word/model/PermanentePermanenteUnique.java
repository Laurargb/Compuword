package io.bootify.compu_word.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import io.bootify.compu_word.service.PermanenteService;
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
 * Validate that the nombre value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = PermanentePermanenteUnique.PermanentePermanenteUniqueValidator.class
)
public @interface PermanentePermanenteUnique {

    String message() default "{Exists.permanente.permanente}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class PermanentePermanenteUniqueValidator implements ConstraintValidator<PermanentePermanenteUnique, String> {

        private final PermanenteService permanenteService;
        private final HttpServletRequest request;

        public PermanentePermanenteUniqueValidator(final PermanenteService permanenteService,
                final HttpServletRequest request) {
            this.permanenteService = permanenteService;
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
            final String currentId = pathVariables.get("contratoAnual");
            if (currentId != null && value.equalsIgnoreCase(permanenteService.get(currentId).getPermanente())) {
                // value hasn't changed
                return true;
            }
            return !permanenteService.permanenteExists(value);
        }

    }

}
