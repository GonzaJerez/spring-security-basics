# Proyecto de Seguridad con Spring Boot y Spring Security

Este proyecto es una implementación de un sistema de gestión de usuarios y roles, desarrollado para afianzar conocimientos en **Spring Boot** y **Spring Security**. La estructura de seguridad está basada en un modelo de permisos similar al utilizado por **AWS IAM**, donde los usuarios pueden tener permisos propios y heredar permisos de roles asociados.

## Características

- **Gestión de Usuarios**: Creación y administración de usuarios con permisos específicos.
- **Gestión de Roles**: Definición de roles con permisos asignados que pueden ser asociados a uno o más usuarios.
- **Seguridad a Nivel de Método**: Aplicación de restricciones de acceso en cada endpoint, basadas en los permisos asignados a usuarios y roles.
- **OAuth**: Permite autenticación mediante Google o Github, además de usuario y contraseña.

## Tecnologías Utilizadas

- **Java 22**
- **Spring Boot**
- **Spring Security**
- **JPA / Hibernate**
- **Postgres 16.4**
- **JWT**
- **Maven**
- **Docker**

## Motivación

Este proyecto fue desarrollado como un ejercicio integral para consolidar mis conocimientos en Spring Boot y, en particular, en Spring Security. Al tener experiencia previa con **NestJS** (framework de Node.js inspirado en Spring), la transición a Spring fue relativamente sencilla, exceptuando la configuración de Spring Security, que requería un manejo detallado de permisos y roles. Este proyecto me permitió enfrentar ese reto y profundizar en la seguridad a nivel de backend.

---

*Desarrollado por [Gonzalo Jerez](https://www.linkedin.com/in/gonzalo-jerez/)*
