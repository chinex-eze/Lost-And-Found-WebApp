package message;

import javax.mail.Message.RecipientType;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.codemonkey.vesijama.Email;
import org.codemonkey.vesijama.Mailer;

public class EmailMessenger { 
	
	static { 
		// normally you would do this in the log4j.xml
		final Logger rootLogger = Logger.getRootLogger();
		rootLogger.addAppender(new ConsoleAppender(new SimpleLayout())); 
		rootLogger.setLevel(Level.INFO);
	}
	
	private static String  host		= "smtp.mail.yahoo.co.in"; 
	private static Integer port  	= 587; 
	private static String  username	= "lostfoundapp@yahoo.com"; 
	private static String  pWord	= "PHP000jQ";  
	
	private String name = ""; 
	private String toAddress = ""; 
	private Message msg; 
	
	public void sendMessage(String name, String toAddr, Message message){ 
		final Email email = new Email(); 
		email.setFromAddress("Lost and Found App", username); 
		email.addRecipient(name, toAddr, RecipientType.TO); 
		 
		//email.setText(message.getBody()); 
		email.setTextHTML(message.getBody());
		email.setSubject(message.getSubject()); 
		
		sendMail(email); 
	} 
	
	public void sendMessage(){ 
		sendMessage(name, toAddress, msg); 
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public void setMessage(Message msg) {
		this.msg = msg;
	}


	private static void sendMail(final Email email){ 
		new Mailer(host, port, username, pWord).sendMail(email); 
	} 
	
	public static Message getConfirmationMessage(String name){ 
		String subjectStr = "Lost and Found confirmation"; 
		String msgStr = "<p>Hello " + name + ",<br /></p>" +
				"<p>This is to confirm that we have received your report " +
				"of lost item as reported on our website. " +
				"A message would be sent to you as soon as your item " +
				"is found and entered on our database.</p>" +
				"<p>Regards <br /> The lost and found team.</p>" +
				"<p><br /><br />This an automatically generated message. " +
				"Please do not reply.</p>"; 
		
		return new Message(subjectStr, msgStr); 
	} 
	
	public static Message getFoundMessage(String name, String city, String code){ 
		String subjectStr = "Your item has been found."; 
		String msgStr = "<h3>Hello " + name + ",</h3>" +
				"<p> This is to inform that your reported missing item has " +
				"been found. </p>" +
				"<p>Please show up at our " + city + " Ofice for collection.<br />" +
						"Please present the followind code: '<em>" + code + "</em>' " +
						"to make locating your a lot faster." +
						"<p>For further equirees please call 000-555-000<br /></p>" + 
						"<p>Regards <br />The lost and found team</p>" +
						"<p><br /><br />This is an automatically generated message." +
						"Please, do not reply.</p>"; 
		
		return new Message(subjectStr, msgStr);  
	}
}
