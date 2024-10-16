package me.dio.controller.dto;

import me.dio.domain.model.Song;

public record SongDto(Long id, String title, String artist, String songDuration) {

    public SongDto(Song model) {
        this(model.getId(), model.getTitle(), model.getArtist(), model.getSongDuration());
    }

    public Song toModel() {
        Song model = new Song();
        model.setId(this.id);
        model.setTitle(this.title);
        model.setArtist(this.artist);
        model.setSongDuration(this.songDuration);
        return model;
    }
}
