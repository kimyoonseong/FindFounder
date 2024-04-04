package com.example.demo.util;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailProvider {
	
	private final JavaMailSender javaMailSender;
	private final String Subject = "[안녕 안녕 반가워 힘내 친구야]";
	
	public boolean sendCertifiactionMail(String email, String content) {
		
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message,true);
			
			String htmlContent = content;
			
			messageHelper.setTo(email);
			messageHelper.setSubject(Subject);
			messageHelper.setText(htmlContent, true);
			
			javaMailSender.send(message);
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
//	private String getCertificationMessage(String certificationNumber) {
//		
//		String certificationMessage = "";
//		certificationMessage += "<h1 style='text-align: center;'>[집가고싶다]</h1>";
//		certificationMessage += "<h3 style='text-align: center'>집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다"
//				+ "집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다"
//				+ "집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다집가고싶다</h3>";
//		return certificationMessage;
//				
//		
//	}
}
