package com.bookstore.config;

import com.bookstore.customer.Customer;
import com.bookstore.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailConfig {
    private final HttpServletRequest request;
    private final JavaMailSender mailSender;

    public MailConfig(HttpServletRequest request, JavaMailSender mailSender) {
        this.request = request;
        this.mailSender = mailSender;
    }

    public String getDomain() {
        return request.getRequestURL().toString().replace(request.getRequestURI(), "");
    }

    public void userCreateMail(User user, String token) throws MessagingException {
        String link = getDomain() + "/confirm-password?token=" + token;
        String htmlMsg = """
                <html>
                <head>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f4f7;
                            margin: 0;
                            padding: 0;
                        }
                        .card {
                            max-width: 600px;
                            margin: 30px auto;
                            background: #ffffff;
                            border-radius: 12px;
                            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
                            padding: 30px;
                        }
                        h2 {
                            color: #333333;
                        }
                        p {
                            color: #555555;
                            font-size: 15px;
                            line-height: 1.6;
                        }
                        .btn {
                            display: inline-block;
                            padding: 12px 24px;
                            margin: 20px 0;
                            background-color: #4CAF50;
                            color: #ffffff !important;
                            text-decoration: none;
                            border-radius: 8px;
                            font-weight: bold;
                        }
                        .footer {
                            margin-top: 30px;
                            font-size: 12px;
                            color: #888888;
                            text-align: center;
                            border-top: 1px solid #eaeaea;
                            padding-top: 15px;
                        }
                    </style>
                </head>
                <body>
                    <div class="card">
                        <h2>Dear, %s!</h2>
                        <p>You have been successfully added as <b>%s</b> (username: <b>%s</b>) at bookstore.</p>
                        <p>Please activate your account by clicking the button below:</p>
                        <p style="text-align: center;">
                            <a href="%s" class="btn">Activate Account</a>
                        </p>
                        <p>If you did not expect this email, you can safely ignore it.</p>
                        <div class="footer">
                            <p>Best regards,<br>Team Codez Boyz</p>
                            <p>&copy; 2025 bookstore. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(user.getName(), user.getRole(), user.getUsername(), link);

        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, "utf-8");
        helper.setTo(user.getEmail());
        helper.setSubject("Welcome to bookstore!");
        helper.setText(htmlMsg, true);
        mailSender.send(msg);
    }

    public void customerCreateMail(Customer customer) {
        String msg = "Dear " + customer.getName() + ",\n\n"
                + "Congratulations 🎉\nYou are now officially one of our valued customers.\n"
                + "We’re excited to have you on board.\n\n"
                + "Best regards,\n"
                + "bookstore";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(customer.getEmail());
        message.setSubject("Welcome to bookstore!");
        message.setText(msg);
        mailSender.send(message);
    }

    public void supplierCreateMail(com.bookstore.supplier.Supplier supplier) {
        String msg = "Dear " + supplier.getName() + ",\n\n"
                + "Congratulations 🎉\n" + supplier.getName() + ", is now officially part of our platform.\n"
                + "We are thrilled to have your Supplier join our network and look forward to working with you.\n\n"
                + "Best regards,\n"
                + "bookstore";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(supplier.getEmail());
        message.setSubject("Welcome to bookstore!");
        message.setText(msg);
        mailSender.send(message);
    }

    public void sentResetPasswordMail(User user, String token) throws MessagingException {
        String link = getDomain() + "/confirm-password?token=" + token;
        String htmlMsg = """
                <html>
                <head>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f4f7;
                            margin: 0;
                            padding: 0;
                        }
                        .card {
                            max-width: 600px;
                            margin: 30px auto;
                            background: #ffffff;
                            border-radius: 12px;
                            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
                            padding: 30px;
                        }
                        h2 {
                            color: #333333;
                        }
                        p {
                            color: #555555;
                            font-size: 15px;
                            line-height: 1.6;
                        }
                        .btn {
                            display: inline-block;
                            padding: 12px 24px;
                            margin: 20px 0;
                            background-color: #4CAF50;
                            color: #ffffff !important;
                            text-decoration: none;
                            border-radius: 8px;
                            font-weight: bold;
                        }
                        .footer {
                            margin-top: 30px;
                            font-size: 12px;
                            color: #888888;
                            text-align: center;
                            border-top: 1px solid #eaeaea;
                            padding-top: 15px;
                        }
                    </style>
                </head>
                <body>
                    <div class="card">
                        <h2>Dear, %s!</h2>
                        <p>You can reset your password by clicking the button below:</p>
                        <p style="text-align: center;">
                            <a href="%s" class="btn">Reset Password</a>
                        </p>
                        <p>If you did not expect this email, you can safely ignore it.</p>
                        <div class="footer">
                            <p>Best regards,<br>Team Codez Boyz</p>
                            <p>&copy; 2025 bookstore. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(user.getName(), link);

        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, "utf-8");
        helper.setTo(user.getEmail());
        helper.setSubject("Reset Password");
        helper.setText(htmlMsg, true);
        mailSender.send(msg);
    }
}
