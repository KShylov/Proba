import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Sender
{
  public void sendMessage(String to,String text) throws MessagingException
  {
    final Properties pr = new Properties();
    try (InputStream stream = this.getClass().getResourceAsStream("mail.properties"))
    {
      pr.load(stream);
      Session mailSession = Session.getDefaultInstance(pr);
      MimeMessage message = new MimeMessage(mailSession);
      message.setFrom(new InternetAddress(pr.getProperty("mail.smtps.user")));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
      message.setSubject("Test");
      message.setText(text);

      Transport tr = mailSession.getTransport();
      tr.connect(pr.getProperty("mail.smtps.user"), pr.getProperty("password"));
      tr.sendMessage(message, message.getAllRecipients());
      tr.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}


