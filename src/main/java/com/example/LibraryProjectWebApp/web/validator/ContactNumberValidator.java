package com.example.LibraryProjectWebApp.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContactNumberValidator implements
    ConstraintValidator<PhoneNumber, String> {
  @Override
  public void initialize(PhoneNumber contactNumber) {
  }

  String patterns = "^(\\+\\d{1,3}( )?)?((\\(\\d{2}\\))|\\d{2})\\d{7}$";

  @Override
  public boolean isValid(String contactField,
                         ConstraintValidatorContext cxt) {
    if (contactField == null) {
      return true;
    }
    return contactField.matches(patterns);
  }
}
