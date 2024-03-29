package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Consult;

@Repository
public interface ConsultRepository extends JpaRepository<Consult, Integer>{

}
