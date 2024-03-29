package com.example.demo.model.dto;

import java.util.Date;

import com.example.demo.model.entity.Consult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data//밑에 둘다 포함하고있다.
//@AllArgsConstructor
@NoArgsConstructor
public class ConsultDto {
	private Integer cunsult_id;
	private Integer age;//int 는 null허용 x
	private boolean sex;
	private String residence;
	private Integer init_fund;
	private Integer competitive;
	private String prefer_industry;
	private String prefer_loc;
	private Date consult_date;
	
	public Consult toEntity() {
		Consult consult= new Consult();
		consult.setConsult_id(this.getCunsult_id());
		consult.setAge(this.getAge());
		consult.setSex(this.isSex());
		consult.setResidence(this.getResidence());
		consult.setInit_fund(this.getAge());
		consult.setCompetitive(this.getAge());
		consult.setPrefer_industry(this.getPrefer_industry());
		consult.setPrefer_loc(this.getPrefer_loc());
		consult.setConsult_date(this.getConsult_date());
		return consult;
	}
}
