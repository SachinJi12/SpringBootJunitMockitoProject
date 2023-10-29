package com.springbootproject.JunitProject.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.springbootproject.JunitProject.model.Movie;

@DataJpaTest
public class MovieRepositoryTest {

	@Autowired
	private MovieRepository movieRepos;
	
	// refactor the suppling data in //Arrange in junit lifecycle methods
	// intialize reference variabbles
	private Movie avatarMovie;
	private Movie sholeMovie;
	
	//Lifecycle Method to setup the data that will run this method before each test method
	@BeforeEach
	void init() {
		// Arrange data
		avatarMovie = new Movie();
		avatarMovie.setName("Avator");
		avatarMovie.setGenera("Action");
		avatarMovie.setReleaseDate(LocalDate.of(2002, Month.APRIL, 22));
		// add another movie
		sholeMovie = new Movie();
		sholeMovie.setName("SHOLE");
		sholeMovie.setGenera("Romatic");
		sholeMovie.setReleaseDate(LocalDate.of(1988, Month.MAY, 19));
	}

	// should save all repos item in DB
	@Test
	@DisplayName("It should save the movie to the database")
	void save() {
		// Arrange is to setup the data required for testing
		// beforeEach will be called
		
		// Act is to for actual method that need to be tested i.e save()
		Movie newMovie = movieRepos.save(avatarMovie);

		// Assert is to check the expected results
		// Assertion have many inbuilt methods.
		assertNotNull(newMovie);
		assertThat(newMovie.getId()).isNotEqualTo(null);
	}

	// should return all movies in DB checked by its list size
	@Test
	@DisplayName("It should return the movie list with 2 size")
	void getAllMovies() {
		// Arrange data
		// beforeEach will be called
		movieRepos.save(avatarMovie);
		movieRepos.save(sholeMovie);
		
		// Act that is the method need to be test is findAll
		List<Movie> list = movieRepos.findAll();

		// Assert
		assertNotNull(list);
		assertThat(list).isNotNull();
		assertEquals(2, list.size());
	}

	// should get the ite by id

	@Test
	@DisplayName("It should return the movie By id")
	void getMovieById() {
		// Arrange
		// beforeEach will be called
		movieRepos.save(avatarMovie);
		// Act
		Movie exsitMovieId = movieRepos.findById(avatarMovie.getId()).get();
		// Assert
		assertNotNull(exsitMovieId);
		assertEquals("Action", exsitMovieId.getGenera());
		assertThat(avatarMovie.getReleaseDate().isBefore(LocalDate.of(2000, Month.APRIL, 23)));

	}

	// To update the movie
	@Test
	@DisplayName("It should update the movie info")
	void getMovieUpdate() {
		// Arrange
		// beforeEach will be called
		movieRepos.save(avatarMovie);
		Movie exsitMovie = movieRepos.findById(avatarMovie.getId()).get();

		// Act
		exsitMovie.setGenera("Fantacy");
		exsitMovie.setName("Titanic");
		Movie newMovie = movieRepos.save(exsitMovie);
		// Assert
		assertEquals("Fantacy", newMovie.getGenera());
		assertEquals("Titanic", newMovie.getName());

	}

	// To delete the movie
	@Test
	@DisplayName("It should delete the exsisting movie info")
	void deleteMovie() {
		// Arrange data
		// beforeEach will be called
		movieRepos.save(avatarMovie);
		// add another movie
		// beforeEach will be called
		movieRepos.save(sholeMovie);
		Long Id = avatarMovie.getId();

		// Act
		movieRepos.delete(avatarMovie);
		// check if the deleted movie id is still there
		Optional<Movie> exsitingMovie = movieRepos.findById(Id);
		List<Movie> movieList = movieRepos.findAll();

		// Assert
		assertEquals(1, movieList.size());
		assertThat(exsitingMovie).isEmpty();

	}

	// To get the movie by genra for query methods or finders methods
	@Test
	@DisplayName("It should get the movie info by genera")
	void getMoviegenera() {
		// Arrange data
		// beforeEach will be called
		movieRepos.save(avatarMovie);
		movieRepos.save(sholeMovie);

		// Act
		List<Movie> listgenera = movieRepos.findByGenera("Romatic");

		// Assert
		assertNotNull(listgenera);
		assertThat(listgenera.size()).isEqualTo(1);

	}
}
