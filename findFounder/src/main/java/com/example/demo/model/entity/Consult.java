package com.example.demo.model.entity;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import com.example.demo.model.dto.ConsultDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor //둘다 data에 포함되어있음
@Builder
@NoArgsConstructor  // JPA 엔티티 클래스에서 기본 생성자가 필요한 경우
@Entity
@ToString(exclude="customer")
public class Consult {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)//자동 글번호 생성
	private Integer consult_id;
	@Column(nullable = false)
	private Integer age;//int 는 null허용 x
	@Column(nullable = false)
	private boolean sex;
	@Column(nullable = false,columnDefinition = "VARCHAR(4)")
	private String residence;
	@Column(nullable = false)
	private Integer init_fund;
	@Column(nullable = false)
	private Integer competitive;
	@Column(nullable = false,columnDefinition = "VARCHAR(10)")
	private String prefer_industry;
	
	@Column(nullable = false,columnDefinition = "VARCHAR(10)")
	private String prefer_loc;
	@Column(nullable = false)
	@CreatedDate
	private Date consult_date;
	// Customer와 연결
	@ManyToOne
	@JoinColumn(name="cus_code")//Select * from user where id=??
	Customer customer;
	public ConsultDto toDto() {
		ConsultDto dto= new ConsultDto();//->noargsconstructor때문에 가능
		dto.setCunsult_id(this.getConsult_id());
		dto.setAge(this.getAge());
		dto.setSex(this.isSex());
		dto.setResidence(this.getResidence());
		dto.setInit_fund(this.getInit_fund());
		dto.setCompetitive(this.getCompetitive());
		dto.setPrefer_industry(this.getPrefer_industry());
		dto.setPrefer_loc(this.getPrefer_loc());
		dto.setConsult_date(this.getConsult_date());
		return dto;
	}
	
}
