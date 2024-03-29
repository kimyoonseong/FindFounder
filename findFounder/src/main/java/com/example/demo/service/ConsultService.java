package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.ConsultDto;
import com.example.demo.model.entity.Consult;
import com.example.demo.model.entity.Customer;
import com.example.demo.repository.ConsultRepository;
import com.example.demo.repository.CustomerRepository;

@Service
public class ConsultService {
	private ConsultRepository consultRepo;
	private CustomerRepository customerRepo;
	@Autowired
	public ConsultService(ConsultRepository crepo) {
		this.consultRepo=consultRepo;
		this.customerRepo=customerRepo;
	}
	//2024-03-29 고객 답변 db에 저장
	public ConsultDto consult(ConsultDto dto) {
		Consult consult=dto.toEntity();
		// 고객의 보유 쿠폰 갯수 확인
		Customer customer = customerRepo.findByCusCode(consult.getCustomer().getCus_code());
		if (customer != null && customer.getCus_cupons() >= 1) {
            // 쿠폰 갯수가 1개 이상이면 상담 정보를 저장
            consultRepo.save(consult);
            
            // 고객의 컨설팅 참여 여부를 true로 변경
            if(!customer.getCus_isconsult()) customer.setCus_isconsult(true);
            customerRepo.save(customer); // 변경된 내용을 저장
        } else {
            throw new RuntimeException("고객의 보유 쿠폰 갯수가 1개 미만입니다.");
        }
		return dto;
	}
}


