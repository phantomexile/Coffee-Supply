/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;



import java.util.Properties;
import java.util.Date;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Service to send emails
 */
public class EmailService {
    
    private static final String EMAIL_FROM = "tuanhpham2610@gmail.com";
    private static final String EMAIL_PASSWORD = "dwzj pwnr jkkd yqqv";
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    
    /**
     * Send password reset email
     * @param toEmail Recipient email
     * @param resetLink Password reset link
     * @return true if sent successfully
     */
   public boolean sendPasswordResetEmail(String toEmail, String resetLink) {
        try {
            System.out.println("=== EMAIL SENDING DEBUG ===");
            System.out.println("From: " + EMAIL_FROM);
            System.out.println("To: " + toEmail);
            System.out.println("SMTP: " + SMTP_HOST + ":" + SMTP_PORT);
            
            // Setup mail server properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.ssl.trust", SMTP_HOST);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            
            // Create authenticator
            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
                }
            };
            
            // Create session with debug enabled
            Session session = Session.getInstance(props, auth);
            
            System.out.println("Creating message...");
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Äáº·t Láº¡i Máº­t Kháº©u - Coffee Shop Management");
            message.setSentDate(new Date());
            
            // Create HTML email content
            String htmlContent = createEmailTemplate(resetLink);
            message.setContent(htmlContent, "text/html; charset=utf-8");
            
            System.out.println("Sending email...");
            // Send email
            Transport.send(message);
            
            System.out.println("âœ“ Password reset email sent successfully to: " + toEmail);
            System.out.println("=========================");
            return true;
            
        } catch (Exception e) {
            System.err.println("âœ— ERROR SENDING EMAIL:");
            System.err.println("Error type: " + e.getClass().getName());
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            System.err.println("=========================");
            return false;
        }
    }
    
    /**
     * Create HTML email template
     * @param resetLink Password reset link
     * @return HTML content
     */
    /**
     * Create HTML email template
     * @param resetLink Password reset link
     * @return HTML content
     */
    private String createEmailTemplate(String resetLink) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<style>"
                // CSS styles remain the same
                + "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }"
                + ".container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }"
                + ".header { background: linear-gradient(135deg, #6B4423 0%, #3E2723 100%); color: white; padding: 30px; text-align: center; }"
                + ".header h1 { margin: 0; font-size: 28px; }"
                + ".content { padding: 40px 30px; color: #333333; }"
                + ".content h2 { color: #6B4423; margin-top: 0; }"
                + ".content p { line-height: 1.6; font-size: 16px; }"
                + ".button { display: inline-block; padding: 15px 40px; background: linear-gradient(135deg, #6B4423 0%, #8B6F47 100%); color: white !important; text-decoration: none; border-radius: 5px; font-weight: bold; margin: 20px 0; }"
                + ".button:hover { background: linear-gradient(135deg, #8B6F47 0%, #6B4423 100%); }"
                + ".warning { background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0; }"
                + ".footer { background-color: #f8f9fa; padding: 20px; text-align: center; color: #666666; font-size: 14px; }"
                + ".icon { font-size: 48px; margin-bottom: 20px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'>"
                + "<div class='icon'>â˜•</div>"
                + "<h1>Coffee Shop Management</h1>"
                + "</div>"
                + "<div class='content'>"
                + "<h2>Password Reset Request</h2>"
                + "<p>Hello,</p>"
                + "<p>We received a request to reset the password for your account. If this was you, please click the button below to create a new password:</p>"
                + "<center>"
                + "<a href='" + resetLink + "' class='button'>Reset Password</a>"
                + "</center>"
                + "<div class='warning'>"
                + "<strong>âš ï¸ Important Note:</strong>"
                + "<ul>"
                + "<li>This link is valid for <strong>2 hours</strong> only.</li>"
                + "<li>It can only be used <strong>once</strong>.</li>"
                + "<li>If you did not request a password reset, please ignore this email.</li>"
                + "</ul>"
                + "</div>"
                + "<p>If the button doesn't work, you can copy and paste the following link into your browser:</p>"
                + "<p style='word-break: break-all; color: #007bff;'>" + resetLink + "</p>"
                + "<p>Regards,<br><strong>The Coffee Shop Management Team</strong></p>"            
                + "</div>"
                + "<div class='footer'>"
                + "<p>Â© 2025 Coffee Shop Management. All rights reserved.</p>"
                + "<p>This is an automated email, please do not reply.</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    /**
     * Send welcome email with login credentials to new user
     */
    public boolean sendWelcomeEmail(String toEmail, String fullName,String password) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true"); 
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.ssl.trust", SMTP_HOST);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            
            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
                }
            };
            
            Session session = Session.getInstance(props, auth);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM, "Coffee Shop System"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Chao mung den voi Coffee Shop - Thong tin dang nhap");
            message.setSentDate(new Date());
            
          String htmlContent = "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body style='font-family:Arial,sans-serif;margin:0;padding:20px;background:#f4f4f4'>"
                + "<div style='max-width:600px;margin:0 auto;background:#fff;border-radius:10px;box-shadow:0 4px 6px rgba(0,0,0,0.1)'>"
                + "<div style='background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);color:#fff;padding:30px;text-align:center'><h1> Welcome to the Coffee Shop!</h1></div>"
                + "<div style='padding:40px 30px'><h2 style='color:#667eea'>Hello</h2>"
                + "<p>Your account has been successfully created in the Coffee Shop system.</p>"
                + "<div style='background:#f5f5f5;padding:20px;border-left:4px solid #667eea;margin:20px 0'>"
                + "<h3 style='color:#667eea;margin-top:0'> Login Information:</h3>"
                + "<p><strong>Password:</strong> <strong style='color:#333;font-size:18px'>" + password + "</strong></p></div>"
                + "<div style='background:#fff3cd;border-left:4px solid #ffc107;padding:15px;margin:20px 0'>"
                + "<p>Sincerely,<br><strong>Coffee Shop Team</strong></p></div>"
                + "<div style='background:#f8f9fa;padding:20px;text-align:center;color:#666'>"
                + "<p> 2025 Coffee Shop Management</p></div></div></body></html>";
            
            message.setContent(htmlContent, "text/html; charset=utf-8");
            Transport.send(message);
            System.out.println(" Welcome email sent to: " + toEmail);
            return true;
        } catch (Exception e) {
            System.err.println(" Failed to send welcome email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
