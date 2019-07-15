package org.rdk.CarSell;

import android.util.Log;

import javax.activation.DataHandler;

import javax.activation.DataSource;

import javax.activation.FileDataSource;

import javax.mail.BodyPart;

import javax.mail.Message;

import javax.mail.Multipart;

import javax.mail.PasswordAuthentication;

import javax.mail.Session;

import javax.mail.Transport;

import javax.mail.internet.InternetAddress;

import javax.mail.internet.MimeBodyPart;

import javax.mail.internet.MimeMessage;

import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import java.io.ByteArrayInputStream;

import java.io.IOException;

import java.io.InputStream;

import java.io.OutputStream;

import java.security.AccessController;
import java.security.Security;

import java.util.Properties;



public class GMailSender extends javax.mail.Authenticator{

    private String mailhost = "smtp.gmail.com";
    private String user, password;
    private Session session;

    private Multipart _multipart = new MimeMultipart();

    static { Security.addProvider(new org.rdk.CarSell.JSSEProvider());}

    public GMailSender(String user, String password){

        this.user = user;
        this.password = password;

        Properties props = new Properties();

        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, this);
    }

    protected PasswordAuthentication getPasswordAuthentication() {

        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String subject, String body, String sender, String recipients,
                                      String filename1, String filename2, String filename3, String filename4) throws Exception {
        try {
            MimeMessage message = new MimeMessage(session);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(
                    body.getBytes(), "text/plain"));
            message.setSender(new InternetAddress(sender));
            message.setSubject(subject);
            message.setDataHandler(handler);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            _multipart.addBodyPart(messageBodyPart);

            // Put parts in message
            message.setContent(_multipart);

            if (recipients.indexOf(',') > 0) {
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(recipients));
            }else {
                message.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(recipients));
            }
            addAttachment(filename1);
            addAttachment(filename2);
            addAttachment(filename3);
            addAttachment(filename4);

            Transport.send(message);
        } catch (Exception e) {
            Log.d("mylog", "Error in sending: " + e.toString());
        }
    }

    public void addAttachment(String filename1) throws Exception {

        BodyPart messageBodyPart2 = new MimeBodyPart();
        DataSource source1 = new FileDataSource(filename1);
        messageBodyPart2.setDataHandler(new DataHandler(source1));
        messageBodyPart2.setFileName(filename1);
        _multipart.addBodyPart(messageBodyPart2);
    }



    public class ByteArrayDataSource implements DataSource {

        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {

            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {

            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }



        public String getContentType() {

            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {

            return new ByteArrayInputStream(data);
        }

        public String getName() {

            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {

            throw new IOException("Not Supported");
        }
    }
}



