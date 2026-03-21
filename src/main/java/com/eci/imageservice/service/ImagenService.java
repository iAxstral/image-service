package com.eci.imageservice.service;

import com.eci.imageservice.model.document.ImagenDocument;
import com.eci.imageservice.repository.ImagenRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ImagenService {

    private final ImagenRepository imagenRepository;

    public ImagenService(ImagenRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    public ImagenDocument guardarImagen(MultipartFile archivo, String referenciaExterna) throws IOException {
        ImagenDocument imagen = new ImagenDocument();
        imagen.setNombre(archivo.getOriginalFilename());
        imagen.setTipoContenido(archivo.getContentType());
        imagen.setTamano(archivo.getSize());
        imagen.setDatos(archivo.getBytes());
        imagen.setFechaCarga(LocalDateTime.now());
        imagen.setReferenciaExterna(referenciaExterna);

        return imagenRepository.save(imagen);
    }

    public List<ImagenDocument> listarImagenes() {
        return imagenRepository.findAll();
    }

    public Optional<ImagenDocument> obtenerPorId(String id) {
        return imagenRepository.findById(id);
    }

    public List<ImagenDocument> obtenerPorReferencia(String referenciaExterna) {
        return imagenRepository.findByReferenciaExterna(referenciaExterna);
    }

    public void eliminarPorId(String id) {
        imagenRepository.deleteById(id);
    }
}