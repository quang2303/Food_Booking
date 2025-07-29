package com.quang.app.JavaWeb_cdquang.validation;

import org.springframework.web.multipart.MultipartFile;

import com.quang.app.JavaWeb_cdquang.utils.FileHelper;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageFileValidator implements ConstraintValidator<ValidImage, MultipartFile> {

	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
		boolean result = true;

        if (file != null) {
        	if(!FileHelper.checkImageType(file.getContentType())) {
        		context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "Only PNG, JPEG or JPG images are allowed.")
                       .addConstraintViolation();

                result = false;
        	} else if (FileHelper.checkImageSize(file.getSize())) {
        		context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "Only size image less than 5mb are allowed.")
                       .addConstraintViolation();

                result = false;
        	}
        }

        return result;
	}

}
