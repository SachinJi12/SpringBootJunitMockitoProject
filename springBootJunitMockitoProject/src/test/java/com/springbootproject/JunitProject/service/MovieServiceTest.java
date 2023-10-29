package com.springbootproject.JunitProject.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.springbootproject.JunitProject.model.Movie;
import com.springbootproject.JunitProject.repository.MovieRepository;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

	@InjectMocks
	private MovieService movieService;
	@Mock
	private MovieRepository movieRepository;

	private Movie avatarMovie;
	private Movie titanicMovie;

	@BeforeEach
	void init() {
		avatarMovie = new Movie();
		avatarMovie.setId(1L);
		avatarMovie.setName("Avatar");
		avatarMovie.setGenera("Action");
		avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 23));

		titanicMovie = new Movie();
		titanicMovie.setId(2L);
		titanicMovie.setName("Titanic");
		titanicMovie.setGenera("Romance");
		titanicMovie.setReleaseDate(LocalDate.of(2004, Month.JANUARY, 10));
	}

	// To save the movie
	@Test
	@DisplayName("test to save the info")
	void save() {
		
		//Arrange the data that is stub the save method of mocked Repos
		when(movieRepository.save(any(Movie.class))).thenReturn(avatarMovie);
		//Act
		Movie newMovie = movieService.save(avatarMovie);
		//Assert
		assertNotNull(newMovie);
		assertThat(newMovie.getName()).isEqualTo("Avatar");
	}

	// get all movies
	@Test
	@DisplayName("get all movies")
	void getMovies() {

		// Arrange
		List<Movie> list = new ArrayList<>();
		list.add(avatarMovie);
		list.add(titanicMovie);
		// stub
		when(movieRepository.findAll()).thenReturn(list);
		// Act
		List<Movie> movies = movieService.getAllMovies();
		// Assert
		assertEquals(2, movies.size());
		assertNotNull(movies);
	}

	// get the movie by id
	@Test
	@DisplayName("get the movie by id")
		void getMovieById() {
		// Arrange and stub
		when(movieRepository.findById(anyLong())).thenReturn(Optional.of(avatarMovie));
		// Act
		Movie existingMovie = movieService.getMovieById(avatarMovie.getId());
		// Assert
		assertNotNull(existingMovie);
		assertThat(existingMovie.getId()).isNotEqualTo(null);
		}

	// get the movie by id for handling exception
	@Test
	@DisplayName("get the movie by id for handling exception")
		void getMovieByIdForException() {
		// Arrange and stub
			when(movieRepository.findById(2L)).thenReturn(Optional.of(avatarMovie));
			// Act and //Assert
			assertThrows(RuntimeException.class, () -> {
				movieService.getMovieById(avatarMovie.getId());
			});
		}

	// Update the movie by id
	@Test
	@DisplayName("Update the movie by id")
		void updateMovie() {
		// Arrange and stub both the methods used in service as well
			when(movieRepository.findById(anyLong())).thenReturn(Optional.of(avatarMovie));
			
			when(movieRepository.save(any(Movie.class))).thenReturn(avatarMovie);
			avatarMovie.setGenera("Fantacy");
			//Act
			Movie exisitingMovie = movieService.updateMovie(avatarMovie, avatarMovie.getId());
			//Assert
			assertNotNull(exisitingMovie);
			assertEquals("Fantacy", avatarMovie.getGenera());
		}

	// delete the movie by id
	@Test
	@DisplayName("delete the movie by id")
	void deleteMovie() {
		// Arrange
		Long movieId = 1L;
		// stub
		when(movieRepository.findById(anyLong())).thenReturn(Optional.of(avatarMovie));
		//delete do not return anything so we use doNothing.
		doNothing().when(movieRepository).delete(any(Movie.class));

		// Act
		movieService.deleteMovie(movieId);
		// we can verify the the call of delete method by verify
		verify(movieRepository, times(1)).delete(avatarMovie);

	}

}
