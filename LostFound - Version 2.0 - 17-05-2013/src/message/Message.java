package message;

public class Message {
	private String subject, body; 
	
	public Message(){}
	
	public Message(String s, String b){ 
		subject = s; 
		body = b; 
	}
	
	public void setSubject(String s){ 
		subject = s; 
	} 
	
	public void setBody(String b){ 
		body = b; 
	} 
	
	public String getSubject(){ 
		return subject; 
	} 
	
	public String getBody(){ 
		return body; 
	} 
}
