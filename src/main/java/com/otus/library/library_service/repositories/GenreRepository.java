package com.otus.library.library_service.repositories;

import com.otus.library.library_service.model.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findAllByIdIn(Set<Long> ids);

    Optional<Genre> findByName(String name);

    boolean existsByName(String name);

}
