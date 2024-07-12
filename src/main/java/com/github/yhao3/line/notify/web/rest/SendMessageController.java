package com.github.yhao3.line.notify.web.rest;

import com.github.yhao3.line.notify.service.LineNotifySendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class SendMessageController {

    private final LineNotifySendService lineNotifySendService;

    @GetMapping("/")
    public String showHome(Model model) {
        return "home";
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNotify(@RequestBody String message) {
        lineNotifySendService.send(message);
        return ResponseEntity.ok("Successfully sent");
    }

    @PostMapping("/submitMessage")
    public String submitMessage(@RequestParam("message") String message, RedirectAttributes redirectAttributes) {
        lineNotifySendService.send(message);
        redirectAttributes.addFlashAttribute("status", "Successfully sent");
        return "redirect:/"; // 重定向到首頁 "/"
    }
}
