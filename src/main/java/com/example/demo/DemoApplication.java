package com.example.demo;

import com.example.demo.config.FooInterceptor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
    LoggerFactory.getLogger("tweet").info(System.getProperty("user.dir"));
		SpringApplication.run(DemoApplication.class, args);
	}

	@Configuration
  public class ServerConfig implements WebMvcConfigurer {
	  @Value("${spring.resources.static-locations}")
	  String resourceLocations;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(new FooInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.addResourceHandler("/public/**").addResourceLocations(resourceLocations);
    }
  }

	@Controller
  @ControllerAdvice
  public class PersonController {
	  @Value("${spa.default-file}")
	  String defaultFile;

    @GetMapping("/greet/{name}")
    public String greetPerson(@RequestAttribute String greeting, @PathVariable("name") String name, Model model, HttpServletResponse response) {
	    model.addAttribute("greeting", greeting);
	    model.addAttribute("name", name);
	    response.setHeader("AUTH_USER", name);
	    return "index";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> renderDefaultPage() {
      try {
        File indexFile = ResourceUtils.getFile("classpath:/public/index.html");
        FileInputStream inputStream = new FileInputStream(indexFile);
        String body = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(body);
      } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error completing the action.");
      }
    }

  }
}
