package com.marketTrio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.marketTrio.controller.MemberCommand;
import com.marketTrio.dao.mybatis.MyBatisMemberDao;
import com.marketTrio.domain.Member;

@Component
public class MyInfoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberCommand.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        MemberCommand memberCommand = (MemberCommand) obj;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "member.nickname", "NICKNAME_REQUIRED", "Nickname is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "member.name", "NAME_REQUIRED", "Name is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "member.id", "ID_REQUIRED", "ID is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "member.email", "EMAIL_REQUIRED", "Email is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "member.phone", "PHONE_REQUIRED", "Phone number is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "member.password", "PASSWORD_REQUIRED", "Password is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "repeatedPassword", "REPEATED_PASSWORD_REQUIRED", "Repeated password is required.");

        if (memberCommand.getPassword().length() < 1 || !memberCommand.getPassword().equals(memberCommand.getRepeatedPassword())) {
            errors.reject("PASSWORD_MISMATCH", "Passwords did not match or were not provided. Matching passwords are required.");
        }
    }
}
