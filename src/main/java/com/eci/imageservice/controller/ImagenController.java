package com.eci.imageservice.controller;

import com.eci.imageservice.model.document.ImagenDocument;
import com.eci.imageservice.service.ImagenService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/imagenes")
public class ImagenController {

    private final ImagenService imagenService;

    public ImagenController(ImagenService imagenService) {
        this.imagenService = imagenService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> cargarImagen(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam(value = "referenciaExterna", required = false) String referenciaExterna
    ) {
        try {
            ImagenDocument guardada = imagenService.guardarImagen(archivo, referenciaExterna);

            Map<String, Object> response = new HashMap<>();
            response.put("id", guardada.getId());
            response.put("nombre", guardada.getNombre());
            response.put("tipoContenido", guardada.getTipoContenido());
            response.put("tamano", guardada.getTamano());
            response.put("fechaCarga", guardada.getFechaCarga());
            response.put("referenciaExterna", guardada.getReferenciaExterna());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar la imagen: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ImagenDocument>> listarImagenes() {
        return ResponseEntity.ok(imagenService.listarImagenes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerImagen(@PathVariable String id) {
        Optional<ImagenDocument> imagenOpt = imagenService.obtenerPorId(id);

        if (imagenOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Imagen no encontrada");
        }

        ImagenDocument imagen = imagenOpt.get();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(imagen.getTipoContenido()));
        headers.setContentDisposition(ContentDisposition.inline().filename(imagen.getNombre()).build());

        return new ResponseEntity<>(imagen.getDatos(), headers, HttpStatus.OK);
    }

    @GetMapping("/metadata/{id}")
    public ResponseEntity<?> obtenerMetadata(@PathVariable String id) {
        Optional<ImagenDocument> imagenOpt = imagenService.obtenerPorId(id);

        if (imagenOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Imagen no encontrada");
        }

        ImagenDocument imagen = imagenOpt.get();

        Map<String, Object> response = new HashMap<>();
        response.put("id", imagen.getId());
        response.put("nombre", imagen.getNombre());
        response.put("tipoContenido", imagen.getTipoContenido());
        response.put("tamano", imagen.getTamano());
        response.put("fechaCarga", imagen.getFechaCarga());
        response.put("referenciaExterna", imagen.getReferenciaExterna());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/referencia/{ref}")
    public ResponseEntity<List<ImagenDocument>> obtenerPorReferencia(@PathVariable("ref") String referencia) {
        return ResponseEntity.ok(imagenService.obtenerPorReferencia(referencia));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarImagen(@PathVariable String id) {
        Optional<ImagenDocument> imagenOpt = imagenService.obtenerPorId(id);

        if (imagenOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Imagen no encontrada");
        }

        imagenService.eliminarPorId(id);
        return ResponseEntity.ok("Imagen eliminada correctamente");
    }
}