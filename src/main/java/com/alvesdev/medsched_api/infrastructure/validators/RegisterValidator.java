package com.alvesdev.medsched_api.infrastructure.validators;

import com.alvesdev.medsched_api.dto.request.register.ProfileType;
import com.alvesdev.medsched_api.dto.request.register.RegisterUserReqDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegisterValidator implements ConstraintValidator<ValidRegister, RegisterUserReqDto> {

    @Override
    public boolean isValid(RegisterUserReqDto dto, ConstraintValidatorContext context) {
        if (dto == null || dto.profileType() == null) {
            return true;
        }

        if (dto.profileType() == ProfileType.DOCTOR) {
            boolean isSpecializationValid = dto.specialization() != null && !dto.specialization().isBlank();
            boolean isLicenseNumberValid = dto.licenseNumber() != null && !dto.licenseNumber().isBlank();
            
            if (!isSpecializationValid) {
                context.buildConstraintViolationWithTemplate("Specialization is required for doctors").addPropertyNode("specialization").addConstraintViolation();
            }
            if (!isLicenseNumberValid) {
                context.buildConstraintViolationWithTemplate("License number is required for doctors").addPropertyNode("licenseNumber").addConstraintViolation();
            }

            return isSpecializationValid && isLicenseNumberValid;
        }

        if (dto.profileType() == ProfileType.PATIENT) {

            boolean isBirthDateValid = dto.birthDate() != null;

            if (!isBirthDateValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Birth date is required for patients").addPropertyNode("birthDate").addConstraintViolation();
            }
            return isBirthDateValid;
        }

        return false; 
    }
}
