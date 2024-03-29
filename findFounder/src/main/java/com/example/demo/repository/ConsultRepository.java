package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.Consult;


public interface ConsultRepository extends JpaRepository<Consult, Integer>{

}
