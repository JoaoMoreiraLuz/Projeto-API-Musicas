package me.dio.service.impl;

import static java.util.Optional.ofNullable;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.dio.domain.model.Song;
import me.dio.domain.repository.SongRepository;
import me.dio.service.SongService;
import me.dio.service.exception.BusinessException;
import me.dio.service.exception.NotFoundException;

@Service
public class SongServiceImpl implements SongService {

    private static final Long UNCHANGEABLE_SONG_ID = 1L;

    private final SongRepository songRepository;

    public SongServiceImpl(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Transactional(readOnly = true)
    public List<Song> findAll() {
        return this.songRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Song findById(Long id) {
        return this.songRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Song create(Song songToCreate) {
        ofNullable(songToCreate).orElseThrow(() -> new BusinessException("User to create must not be null."));
        ofNullable(songToCreate.getTitle()).orElseThrow(() -> new BusinessException("The song title must not be null."));
        ofNullable(songToCreate.getArtist()).orElseThrow(() -> new BusinessException("The song artist must not be null."));

        this.validateChangeableId(songToCreate.getId(), "created");
        if (songRepository.existsByTitle(songToCreate.getTitle())) {
            throw new BusinessException("This account title already exists.");
        }
        return this.songRepository.save(songToCreate);
    }

    @Transactional
    public Song update(Long id, Song songToUpdate) {
        this.validateChangeableId(id, "updated");
        Song dbSong = this.findById(id);
        if (!dbSong.getId().equals(songToUpdate.getId())) {
            throw new BusinessException("Update IDs must be the same.");
        }

        dbSong.setTitle(songToUpdate.getTitle());
        dbSong.setArtist(songToUpdate.getArtist());
        dbSong.setSongDuration(songToUpdate.getSongDuration());

        return this.songRepository.save(dbSong);
    }

    @Transactional
    public void delete(Long id) {
        this.validateChangeableId(id, "deleted");
        Song dbSong = this.findById(id);
        this.songRepository.delete(dbSong);
    }

    private void validateChangeableId(long id, String operation) {
        if (UNCHANGEABLE_SONG_ID.equals(id)) {
            throw new BusinessException("Song with ID %d can not be %s.".formatted(UNCHANGEABLE_SONG_ID, operation));
        }
    }
}
