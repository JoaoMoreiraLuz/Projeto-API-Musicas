package me.dio.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.dio.domain.model.Song;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    boolean existsByTitle(String title);
}
