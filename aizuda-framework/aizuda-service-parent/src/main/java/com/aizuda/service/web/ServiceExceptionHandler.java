/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.service.web;

import com.aizuda.common.toolkit.ThrowableUtils;
import com.aizuda.core.api.ApiResult;
import com.aizuda.core.api.IErrorCode;
import com.aizuda.core.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.util.NestedServletException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service 异常处理
 *
 * @author 青苗
 * @since 1.1.0
 */
@Slf4j
@RestControllerAdvice
public class ServiceExceptionHandler {
    /**
     * 是否在响应结果中展示验证错误提示信息
     */
    @Value("${spring.validation.message.enable:true}")
    private Boolean enableValidationMessage;

    /**
     * 验证异常处理 - 在 @RequestBody 上添加 @Validated 处触发
     *
     * @param e {@link MethodArgumentNotValidException}
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ApiResult<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ApiResult.failed(this.convertFiledErrors(e.getBindingResult().getFieldErrors()));
    }

    /**
     * 验证异常处理 - form参数（对象参数，没有加 @RequestBody）触发
     *
     * @param e {@link BindException}
     */
    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ApiResult<String> handleBindException(BindException e) {
        return ApiResult.failed(this.convertFiledErrors(e.getBindingResult().getFieldErrors()));
    }

    /**
     * 验证异常处理 - @Validated加在 controller 类上，
     * 且在参数列表中直接指定constraints时触发
     *
     * @param request HttpServletRequest
     * @param ex      ConstraintViolationException
     */
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ApiResult<String> handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException ex) {
        return ApiResult.failed(this.convertConstraintViolations(ex));
    }

    /**
     * 转换FieldError列表为错误提示信息
     *
     * @param fieldErrors List<FieldError>
     */
    private String convertFiledErrors(List<FieldError> fieldErrors) {
        return Optional.ofNullable(fieldErrors)
                .filter(fieldErrorsInner -> this.enableValidationMessage)
                .map(fieldErrorsInner -> fieldErrorsInner.stream()
                        .flatMap(fieldError -> Stream.of(fieldError.getField() + " " + fieldError.getDefaultMessage()))
                        .collect(Collectors.joining(", ")))
                .orElse(null);
    }

    /**
     * 转换ConstraintViolationException 异常为错误提示信息
     *
     * @param constraintViolationException ConstraintViolationException
     */
    private String convertConstraintViolations(ConstraintViolationException constraintViolationException) {
        return Optional.ofNullable(constraintViolationException.getConstraintViolations())
                .filter(constraintViolations -> this.enableValidationMessage)
                .map(constraintViolations -> constraintViolations.stream().flatMap(constraintViolation -> {
                            String path = constraintViolation.getPropertyPath().toString();
                            return Stream.of(path.substring(path.lastIndexOf(".") + 1) +
                                    " " + constraintViolation.getMessage());
                        }).collect(Collectors.joining(", "))
                ).orElse(null);
    }

    /**
     * 自定义 REST 业务异常
     *
     * @param e    异常类型
     * @param resp 响应请求
     */
    @ExceptionHandler(value = Throwable.class)
    public ApiResult<Object> handleBadRequest(Throwable e, HttpServletResponse resp) {
        /*
         * 业务逻辑异常
         */
        if (e instanceof ApiException) {
            IErrorCode errorCode = ((ApiException) e).getErrorCode();
            if (null != errorCode) {
                return ApiResult.failed(errorCode);
            }
            return ApiResult.failed(e.getMessage());
        }

        // 请求参数无法读取
        if (e instanceof HttpMessageNotReadableException) {
            return ApiResult.failed(e.getMessage());
        }

        /*
         * 系统内部异常，打印异常栈
         */
        if (log.isErrorEnabled()) {
            log.error("Error: handleBadRequest StackTrace : {}", ThrowableUtils.getStackTrace(e));
        }
        if (e instanceof MethodArgumentTypeMismatchException) {
            resp.setStatus(301);
            return ApiResult.failed("请求参数错误");
        }
        return ApiResult.failed("Internal Server Error");
    }
}
