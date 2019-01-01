package com.bytecubed.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    protected String login(final HttpServletRequest req) {
        String redirectUri = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/callback";
//        String authorizeUrl = controller.buildAuthorizeUrl(req, redirectUri)
//                .withAudience(String.format("https://%s/userinfo", appConfig.getDomain()))
//                .build();
//        return "redirect:" + authorizeUrl;
        return null;
    }

}
