package com.eci.imageservice.service;

import com.eci.imageservice.model.document.ImagenDocument;
import com.eci.imageservice.repository.ImagenRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ImagenService {

    private final ImagenRepository imagenRepository;

    public ImagenService(ImagenRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    public ImagenDocument guardar(MultipartFile archivo, String referenciaExterna) throws IOException {
        ImagenDocument imagen = new ImagenDocument(
                archivo.getOriginalFilename(),
                archivo.getContentType(),
                archivo.getSize(),
                archivo.getBytes(),
                LocalDateTime.now(),
                referenciaExterna
        );

        return imagenRepository.save(imagen);
    }

    public ImagenDocument buscarPorId(@NonNull String id) {
        return imagenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
    }

    public List<ImagenDocument> listar() {
        return imagenRepository.findAll();
    }

    public List<ImagenDocument> listarPorReferencia(String referenciaExterna) {
        return imagenRepository.findByReferenciaExterna(referenciaExterna);
    }

    public void eliminar(@NonNull String id) {
        imagenRepository.deleteById(id);
    }
}