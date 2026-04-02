package com.eci.imageservice.controller;

import com.eci.imageservice.model.document.ImagenDocument;
import com.eci.imageservice.service.ImagenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/imagenes")
public class ImagenController {

    private final ImagenService imagenService;

    public ImagenController(ImagenService imagenService) {
        this.imagenService = imagenService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImagenDocument subirImagen(@RequestParam("archivo") MultipartFile archivo,
                                    @RequestParam("referenciaExterna") String referenciaExterna) throws IOException {
        return imagenService.guardar(archivo, referenciaExterna);
    }

    @GetMapping
    public List<ImagenDocument> listar() {
        return imagenService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable @NonNull String id) {
        ImagenDocument imagen = imagenService.buscarPorId(id);

        String tipoContenido = imagen.getTipoContenido();
        if (tipoContenido == null || tipoContenido.isBlank()) {
            tipoContenido = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imagen.getNombre() + "\"")
                .contentType(MediaType.parseMediaType(tipoContenido))
                .body(imagen.getDatos());
    }

    @GetMapping("/referencia/{referenciaExterna}")
    public List<ImagenDocument> listarPorReferencia(@PathVariable String referenciaExterna) {
        return imagenService.listarPorReferencia(referenciaExterna);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable @NonNull String id) {
        imagenService.eliminar(id);
    }
}