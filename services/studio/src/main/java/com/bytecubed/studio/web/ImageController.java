package com.bytecubed.studio.web;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/images")
public class ImageController {

    private ServletContext servletContext;

    @Autowired
    public ImageController(ServletContext servletContext ){
        this.servletContext = servletContext;
    }

    @GetMapping("/playcards/{id}")
    public void getImageAsByteArray(HttpServletResponse response, @PathVariable UUID id ) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream in = classloader.getResourceAsStream("images/ravens-30-no-logo.png");
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }
}
