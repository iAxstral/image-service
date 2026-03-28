# image-service

Microservicio de imágenes con Spring Boot y MongoDB.

## Funcionalidades

- Cargar imágenes
- Consultar imagen por id
- Listar imágenes
- Buscar por referencia externa
- Eliminar imágenes

## Tecnologías

- Java 17
- Spring Boot
- MongoDB
- Maven


## Pruebas realizadas

Se realizaron pruebas funcionales del microservicio utilizando Postman, con el esto se logra validar el correcto funcionamiento de los endpoints definidos para la gestión de imágenes.

### Evidencia de subida de imagen

A continuación la evidencia de la prueba realizada mediante Postman, donde se verifica el correcto funcionamiento del endpoint `POST /imagenes`, en esta prueba se envía una solicitud de tipo `multipart/form-data` que incluye un archivo de imagen y una referencia externa, permitiendo validar el proceso completo de carga y almacenamiento en MongoDB. El resultado exitoso confirma que el microservicio recibe, procesa y persiste correctamente la imagen.

![Postman](./Postman.png)






