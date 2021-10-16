import java.sql.Timestamp;
import java.time.Instant;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//Thanks Rishabh Mishra, https://netcorecloud.com/tutorials/send-email-in-java-using-gmail-smtp/

public class MailSender {
  private final int WAIT_TIME = 7200000;    // 2 hours
  private String fromMail;
  private String fromPassword;
  private String toMail;
  private Session session;
  private Timestamp sendThreshold;
  private UserInput userInput;

  public MailSender(String from, String fromPassword, String to, UserInput userInput) {
    this.toMail = to;
    this.fromMail = from;
    this.fromPassword = fromPassword;
    this.sendThreshold = Timestamp.from(Instant.now());
    this.userInput = userInput;

    String host = "smtp.gmail.com";
    Properties properties = System.getProperties();

    properties.put("mail.smtp.host", host);
    properties.put("mail.smtp.port", "465");
    properties.put("mail.smtp.ssl.enable", "true");
    properties.put("mail.smtp.auth", "true");

    session = Session.getInstance(properties, new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(fromMail, fromPassword);
      }
    });
    session.setDebug(false);

  }

  public void sendMail(String subject, String text) {
    Timestamp now = Timestamp.from(Instant.now());
    String userSend = userInput.getUserInputMap().get("SEND_MAIL");
    if(now.after(sendThreshold) && userSend.equals("yes")) {
      try {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromMail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
        message.setSubject(subject);
        message.setText(text);

        Transport.send(message);
      } catch (MessagingException mex) {
        mex.printStackTrace();
      }
      sendThreshold.setTime(now.getTime() + WAIT_TIME);
    }
  }

//  public static void main(String[] args) {
//    MailSender sender = new MailSender("kubikopousada@gmail.com", "olho_zeca", "antoniorodrigo92@gmail.com");
//    sender.sendMail("isto é um teste 2", "teste de corpo, lol! 2");
//  }

}
