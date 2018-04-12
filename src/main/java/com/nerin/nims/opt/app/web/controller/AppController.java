package com.nerin.nims.opt.app.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zach on 16/5/10.
 */
@Controller
@RequestMapping("/")
public class AppController {
    protected Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping("/")
    public void home(HttpServletResponse response) throws IOException {
        response.sendRedirect("/index.html");
        return;
    }

    @RequestMapping("/management")
    public void management(HttpServletResponse response) throws IOException {
        response.sendRedirect("/pages/management/index.html");
        return;
    }

}
