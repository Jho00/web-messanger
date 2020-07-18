package com.akutin.messaginglogic.services;

import com.akutin.messaginglogic.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMessage(String subject, String text, User target) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(target.getEmail());

            msg.setSubject(subject);
            msg.setText(text);

            javaMailSender.send(msg);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
