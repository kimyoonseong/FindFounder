package com.example.demo.model.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.example.demo.model.entity.Consult;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConsultDto {
    private Integer consultId;
    private Integer age;
    private boolean sex;
    private String residence;
    private Integer initFund;
    private Integer competitive;
    private String preferIndustry;
    private String preferLoc;
    @JsonIgnore
    private Date consultDate;
    
    public Consult toEntity() {
        Consult consult = new Consult();
        consult.setConsultId(this.getConsultId());
        consult.setAge(this.getAge());
        consult.setSex(this.isSex());
        consult.setResidence(this.getResidence());
        consult.setInitFund(this.getInitFund());
        consult.setCompetitive(this.getCompetitive());
        consult.setPreferIndustry(this.getPreferIndustry());
        consult.setPreferLoc(this.getPreferLoc());
        consult.setConsultDate(this.getConsultDate());
        return consult;
    }
}
