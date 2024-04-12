package com.example.demo.service;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.res.CommonRes;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.util.EmailProvider;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@PropertySource("classpath:application.properties")
@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

	private  EmailProvider emailProvider;
	private CustomerRepository customerRepository;
	
	@Autowired
	public EmailService(EmailProvider emailProvider, CustomerRepository customerRepository) {
		this.emailProvider = emailProvider;
		this.customerRepository = customerRepository;
	}
	
	
	public CommonRes sendEmail(String email) {
		System.out.println("@@@@@@@@@@@@@@@ e mail" + email);
		// 1. CustomerRepository -> Input : cusEmail return Customer 
		String cusId = customerRepository.findByCusEmail(email).getCusId();
		String msg = "<h1 style='text-align: center;'>[" + "안녕하세요. 요청하신 아이디입니다."+ "]</h1>" + "<h3 style='text-align: center'>"
		+ "아이디: " + cusId + "</h3>\"";
		// 2. cusRepository 생성자
		if (msg.equals("")) {
			msg = "일치하는 이메일이 없습니다.";
		}
		// 3. cusRepostiroy
		
		

		emailProvider.sendCertifiactionMail(email, msg);
		return CommonRes.builder().code(200).msg("이메일이 전송되었습니다.").build();

	}
	
//	final JavaMailSender javaMailSender;
//	
//	public void sendSimpleMessage(String to, String content) throws Exception {
//		MimeMessage message = createMessage(to, content);
//        try{
//            javaMailSender.send(message); // 메일 발송
//        }catch(MailException es){
//            es.printStackTrace();
//            throw new IllegalArgumentException();
//        }
//}
//
//
//	public MimeMessage createMessage(String to, String content) throws MessagingException, UnsupportedEncodingException {
//        log.info("보내는 대상 : " + to);
//        log.info("content : " + content);
//        MimeMessage  message = javaMailSender.createMimeMessage();
//
//        message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
//        message.setSubject("메일 제목"); //메일 제목
//
//        // 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
//        String msg="";
//        // 생략
//        msg += "<div>" + content + "</div>";
//
//        message.setText(msg, "utf-8", "html"); //내용, charset타입, subtype
//        message.setFrom(new InternetAddress("woorifis.lab39@gmail.com","gyk")); //보내는 사람의 메일 주소, 보내는 사람 이름
//        return message;
//	}

}
