package com.marketTrio.common.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex, RedirectAttributes redirectAttributes) {
		log.warn("파일 업로드 용량 초과", ex);
		redirectAttributes.addFlashAttribute("fileSizeError", "파일은 최대 10MB까지 업로드할 수 있습니다.");
		return "redirect:/auction/create";
	}
}

