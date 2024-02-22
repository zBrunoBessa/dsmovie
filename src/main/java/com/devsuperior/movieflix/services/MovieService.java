package com.devsuperior.movieflix.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.GenreDTO;
import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class MovieService {

	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private GenreRepository genreRepository;
	
	@Transactional(readOnly = true)
	public MovieDetailsDTO findById(Long id) {
			Optional<Movie> obj = movieRepository.findById(id);
			Movie entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
			return new MovieDetailsDTO(entity, new GenreDTO(entity.getGenre()));
		}


	@Transactional(readOnly = true)
	public Page<MovieCardDTO> findAllPaged(Long genreId, Pageable pageable) {
		Genre genre =  (genreId == 0) ? null : genreRepository.getReferenceById(genreId);
		Page<Movie> page = movieRepository.findByGenre(genre, pageable);
		
		return page.map(x -> new MovieCardDTO(x));
	}
	}

