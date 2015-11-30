package com.lyj.lolplayer.play.report;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


/**
 * 结果处理器
 * @author lili
 *
 */
public class ReportHandler {

	private Session session;
	
	public static ReportHandler defaultReportHandler = new ReportHandler();
	
	public static ReportHandler getInstance(){
		return defaultReportHandler;
	}
	
	public ReportHandler() {
		initEmailServer();
	}
	
	public void initEmailServer(){
		Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.163.com");
        properties.put("mail.smtp.port", "25");
//        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        
       Authenticator emailAuthenticator = new Email_Authenticator("liliyoyo.2009@163.com", "!liyijiang123;");
       session = javax.mail.Session.getDefaultInstance(properties,emailAuthenticator);
	}

	/**
	 * 发送邮件到qq
	 */
	public void sendEmailToQQ(String attchPath){
		
		String fileName = attchPath.substring(attchPath.lastIndexOf("/")+1);
		
		Message msg = new MimeMessage(session);
		try {
			msg.setText("游戏结果");
			msg.setSubject("游戏结果"+fileName);
			msg.setFrom(new InternetAddress("liliyoyo.2009@163.com"));
			msg.setRecipient(RecipientType.TO, new InternetAddress("756643101@qq.com"));
			msg.setSentDate(new Date());
			
			//附件
			MimeMultipart msgMimeMultipart = new MimeMultipart("mixed");
			msg.setContent(msgMimeMultipart);
			
			MimeBodyPart attch1 = new MimeBodyPart();//
			MimeBodyPart content = new MimeBodyPart();//
			
			
			msgMimeMultipart.addBodyPart(attch1);
			msgMimeMultipart.addBodyPart(content);
			
			//组装附件
			attch1.setFileName(fileName);
			DataSource ds1 =  new FileDataSource(attchPath);
			DataHandler dh1 = new DataHandler(ds1);
			attch1.setDataHandler(dh1);
			
			//组装正文
			MimeMultipart bodyMimeMultipart = new MimeMultipart("related");
			content.setContent(bodyMimeMultipart);
			MimeBodyPart htmlPart = new MimeBodyPart();	
			MimeBodyPart gifPart = new MimeBodyPart();
			bodyMimeMultipart.addBodyPart(htmlPart);
			bodyMimeMultipart.addBodyPart(gifPart);
			DataSource gifds =  new FileDataSource(attchPath);
			DataHandler gifdh = new DataHandler(gifds);
			gifPart.setDataHandler(gifdh);
//			gifPart.setHeader("Content-Location", "http://ww w.lyj.lolplayer/report.gif");
			gifPart.setHeader("Content-ID", "<IMG1>");
			
			htmlPart.setContent("邮件正文！<img src='cid:IMG1'>", "text/html;charset=utf-8"); 
			msg.saveChanges(); 
			
			Transport.send(msg);
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	/**
	 * 添加邮件附件
	 */
	public void addEmailAttachment(){
		
	}
	
	
	
}
