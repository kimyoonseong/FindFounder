package com.example.demo.model.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.demo.model.dto.ConsultDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@ToString(exclude="customer")
@EntityListeners(AuditingEntityListener.class)
public class Consult {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer consultId;
    
    @Column(nullable = false)
    private Integer age;
    
    @Column(nullable = false)
    private boolean sex;
    
    @Column(nullable = false, columnDefinition = "VARCHAR(10)")
    private String residence;
    
    @Column(nullable = false)
    private Integer initFund;
    
    @Column(nullable = false)
    private Integer competitive;
    
    @Column(nullable = true, columnDefinition = "VARCHAR(10)")
    private String preferIndustry;
    
    @Column(nullable = true, columnDefinition = "VARCHAR(10)")
    private String preferLoc;
    

    @Column(nullable = false)
    @CreatedDate
    private Date consultDate;

    @ManyToOne
    @JoinColumn(name="cus_code")
    private Customer customer;
    
    public ConsultDto toDto() {
        ConsultDto dto = new ConsultDto();
        dto.setConsultId(this.getConsultId());
        dto.setAge(this.getAge());
        dto.setSex(this.isSex());
        dto.setResidence(this.getResidence());
        dto.setInitFund(this.getInitFund());
        dto.setCompetitive(this.getCompetitive());
        dto.setPreferIndustry(this.getPreferIndustry());
        dto.setPreferLoc(this.getPreferLoc());
        dto.setConsultDate(this.getConsultDate());
        return dto;
    }
}
