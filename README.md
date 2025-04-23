# 📚 Library System - Projeto em Spark Java

Este é um sistema simples de gerenciamento de biblioteca, desenvolvido com **Spark Java** e **Gradle**, com foco em práticas de desenvolvimento limpo, organização em camadas e uso de conceitos como enums e logs com SLF4J.

## 🚀 Funcionalidades

- Cadastro de livros
- Cadastro de exemplares (cópias) dos livros
- Consulta de livros e seus respectivos exemplares
- Enum para representar gêneros literários
- Estrutura organizada em camadas (`domain`, `controller`, `repository`, etc.)
- Logs utilizando **SLF4J** com `slf4j-simple`

## 🛠 Tecnologias Utilizadas

- Java 17+
- H2 Database
- Spark Java
- Gradle
- SLF4J (`slf4j-simple`)
- JSON (Gson ou Jackson)
- GitHub (controle de versão)

## 📁 Estrutura do Projeto

/src 
  └── main 
    └── java 
      └── app 
      ├── domain 
        # Entidades (Book, Copy, etc.) 
      ├── repository 
        # Repositórios e persistência em memória 
      ├── controller 
        # Lógica de rotas e controle
      └── infrastructure 
        # Utilitários (ex: json, database, router, util.)
