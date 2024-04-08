package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Consult;
import com.example.demo.model.entity.Customer;
import com.example.demo.model.entity.Post;

@Repository
public interface ConsultRepository extends JpaRepository<Consult, Integer>{
	
	void deleteAllByCustomer_CusCode(int cusCode);
	
	//consultId가 가장 큰놈 담기
	@Query("select p from Consult p join fetch p.customer where p.customer.cusCode =:cusCode Order by consultId DESC limit 1")
	 Optional<Consult> findByCustomer_CusCodeDesc(@Param("cusCode") int cusCode);

	@Query("select p from Consult p join fetch p.customer where p.customer.cusCode =:cusCode")
	List<Consult> findByCusCode(@Param("cusCode") int cusCode);
	
	
	
}
