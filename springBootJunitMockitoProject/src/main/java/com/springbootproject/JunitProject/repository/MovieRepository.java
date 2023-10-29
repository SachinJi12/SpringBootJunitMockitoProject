package com.springbootproject.JunitProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springbootproject.JunitProject.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
	
	// created a finder method as query method of JPA repos
	List<Movie> findByGenera(String genera);

}
