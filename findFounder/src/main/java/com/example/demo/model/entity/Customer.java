package com.example.demo.model.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.demo.model.dto.CustomerDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int cusCode;

    @Column(nullable = false, length = 16)
    private String cusId;

    @Column(nullable = false, length = 100)
    private String cusPw;

    @Column(nullable = false, length = 5)
    private String cusName;

    @ColumnDefault("0")
    private int cusCupons;

    @Column(nullable = false, length = 8)
    private String cusPwAnswer;

    private Boolean cusIsConsult;

    @Column(nullable = false, length = 48)
    private String cusEmail;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cusQuestionId")
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private Question question;

    public CustomerDto toDto() {
        return CustomerDto.builder()
                .cusCode(this.getCusCode())
                .cusId(this.getCusId())
                .cusPw(this.getCusPw())
                .cusName(this.getCusName())
                .cusCupons(this.getCusCupons())
                .cusPwAnswer(this.getCusPwAnswer())
                .cusIsConsult(this.getCusIsConsult())
                .cusEmail(this.getCusEmail())
                .build();
    }
}
