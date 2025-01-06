package net.guides.todo.todo_management.todo_management_spring_boot.controller;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

/**
 * The ErrorController class in Java handles exceptions by providing custom error messages for
 * different types of exceptions.
 */
@Controller("error")
public class ErrorController {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(HttpServletRequest request, Exception ex) {
        ModelAndView modelAndView = new ModelAndView("error"); // Specify error view
        modelAndView.addObject("exception", ex.getLocalizedMessage());
        modelAndView.addObject("url", request.getRequestURL());
        return modelAndView;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ModelAndView handleNoSuchElementException(HttpServletRequest request, Exception ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("exception", "The requested resource was not found.");
        modelAndView.addObject("url", request.getRequestURL());
        return modelAndView;
    }
}
