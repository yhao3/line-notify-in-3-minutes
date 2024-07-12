package com.github.yhao3.line.notify.web.rest;

import com.github.yhao3.line.notify.service.LineNotifyAuthorizeService;
import com.github.yhao3.line.notify.service.LineNotifySendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/line-notify")
public class AuthorizeController {

    @Value("${line-notify.client-id}")
    private String clientId;

    @Value("${line-notify.client-secret}")
    private String clientSecret;

    private final LineNotifyAuthorizeService lineNotifyAuthorizeService;
    private final LineNotifySendService lineNotifySendService;
    private final Environment env;

    @GetMapping("/authorize")
    public ModelAndView authorize(RedirectAttributes attributes) {

        String serverPort = env.getProperty("server.port");

        // Redirect to Line Notify authorize page:
        return new ModelAndView("redirect:https://notify-bot.line.me/oauth/authorize")
                .addObject("response_type", "code")
                .addObject("scope", "notify")
                .addObject("response_mode", "form_post")
                .addObject("client_id", clientId)
                .addObject(
                        "redirect_uri",
                        String.format("http://localhost:%s/api/line-notify/exchange-token", serverPort)
                )
                .addObject("state", "randomState");
    }

    @PostMapping("/exchange-token")
    public ModelAndView exchangeToken(@RequestParam String code, @RequestParam String state) {
        lineNotifyAuthorizeService.exchangeToken(code);
        // redirect to the home page
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/is-authorized")
    public ResponseEntity<Boolean> isAuthorized() {
        return ResponseEntity.ok(lineNotifyAuthorizeService.isAuthorized());
    }

    @PostMapping("/revoke")
    public ModelAndView revoke() {
        lineNotifyAuthorizeService.revoke();
        return new ModelAndView("redirect:/");
    }

}
