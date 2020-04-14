package com.example.demo.handlers;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
public class SpaErrorController extends BasicErrorController {
  public SpaErrorController(ErrorAttributes errorAttributes) {
    super(errorAttributes, new ErrorProperties());
  }

  @Override
  public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
    final String forwardUri = Optional.ofNullable(request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI))
            .map(Object::toString)
            .orElse(null);

    System.err.println("errorHtml: " + forwardUri);

    if (forwardUri != null && forwardUri.startsWith("/public/")) {
      return new ModelAndView("forward:/public/index.html", HttpStatus.OK);
    } else {
      return super.errorHtml(request, response);
    }
  }
}
