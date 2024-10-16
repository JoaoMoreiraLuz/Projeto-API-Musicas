package me.dio.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.dio.controller.dto.SongDto;
import me.dio.service.SongService;

@CrossOrigin
@RestController
@RequestMapping("/songs")
@Tag(name = "Songs Controller", description = "RESTful API para músicas.")
public record SongController(SongService songService) {

    @GetMapping
    @Operation(summary = "Get all songs", description = "Busca todas as músicas registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operação concluída.")
    })
    public ResponseEntity<List<SongDto>> findAll() {
        var songs = songService.findAll();
        var songsDto = songs.stream().map(SongDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(songsDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a song by ID", description = "Busca uma música específica pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operação concluída."),
        @ApiResponse(responseCode = "404", description = "Música não encontrada.")
    })
    public ResponseEntity<SongDto> findById(@PathVariable Long id) {
        var song = songService.findById(id);
        return ResponseEntity.ok(new SongDto(song));
    }

    @PostMapping
    @Operation(summary = "Create a new song", description = "Cria uma música nova e retorna as informações da música criada.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Música criada com sucesso."),
        @ApiResponse(responseCode = "422", description = "Informação inválida.")
    })
    public ResponseEntity<SongDto> create(@RequestBody SongDto songDto) {
        var song = songService.create(songDto.toModel());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(song.getId())
            .toUri();
        return ResponseEntity.created(location).body(new SongDto(song));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a song", description = "Atualiza as informações de uma música existente a base de seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Música atualizada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Música não encontrada"),
        @ApiResponse(responseCode = "422", description = "Informação inválida.")
    })
    public ResponseEntity<SongDto> update(@PathVariable Long id, @RequestBody SongDto songDto) {
        var song = songService.update(id, songDto.toModel());
        return ResponseEntity.ok(new SongDto(song));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a song", description = "Deleta uma música por base de seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Música deletada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Música não encontrada")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        songService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
