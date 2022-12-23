package com.f90.telegram.bot.memorygymbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.NestedServletException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Object errorException = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        String errorMessage = "<none>";
        if(errorException != null) {
            errorMessage = ((NestedServletException)errorException).getMessage();
        }
        log.warn("handleError() - msg: error on requestUri={} with statusCode={} errorMessage={}", requestUri, statusCode, errorMessage);
        return "error";
    }
}