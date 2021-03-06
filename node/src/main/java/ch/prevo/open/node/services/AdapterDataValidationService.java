package ch.prevo.open.node.services;

import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.data.api.EmploymentTermination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Service
public class AdapterDataValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmploymentCommencementService.class);

    private final Validator validator;

    @Inject
    public AdapterDataValidationService(Validator validator) {
        this.validator = validator;
    }

    public Set<ConstraintViolation<EmploymentCommencement>> getEmploymentCommencementViolations(EmploymentCommencement employmentCommencement) {
        return validator.validate(employmentCommencement);
    }

    public Set<ConstraintViolation<EmploymentTermination>> getEmploymentTerminationViolations(EmploymentTermination employmentCommencement) {
        return validator.validate(employmentCommencement);
    }

}
