package com.aizuda.boot.config;

import com.aizuda.service.advice.ApiResponseBodyAdvice;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
@RestControllerAdvice(basePackages = {"com.aizuda.boot.modules"})
public class BootResponseBodyAdvice extends ApiResponseBodyAdvice {

}
