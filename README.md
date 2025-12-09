# SGHSS – Sistema de Gestão Hospitalar e de Serviços de Saúde

> **Projeto acadêmico** desenvolvido como atividade final de Projetos, com o objetivo de integrar conhecimentos adquiridos ao longo do curso de **Análise e Desenvolvimento de Sistemas**.

## 📌 Visão Geral
O **SGHSS** é uma solução voltada à gestão de instituições de saúde (hospitais, clínicas, laboratórios e home care). O projeto foca em **cadastro e atendimento de pacientes**, **gestão de profissionais**, **agendamento de consultas/procedimentos**, **controle operacional** e **indicadores gerenciais**.

## 🗂 Estrutura do Repositório
```
SGHSS/
└── sghss-api/          # Módulo principal da API (Java)
```

- **Linguagem predominante**: Java (**100%** reportado pelo GitHub)
- **Releases**: até o momento, nenhuma release publicada.

## 🎯 Objetivos do Sistema
- Centralizar o **cadastro e atendimento** de pacientes.
- Organizar a **gestão de profissionais** de saúde.
- Dar suporte à **administração hospitalar**.
- Prover **segurança, compliance e auditoria**.

## ✅ Funcionalidades (planejadas / em implementação)
- Autenticação e Autorização (JWT)
- Cadastros básicos: pacientes, profissionais, usuários
- Agenda: agendamento, reagendamento e cancelamento
- Prontuário: registro de atendimentos
- Relatórios: indicadores operacionais
- Integrações: APIs externas

## 🛠️ Tecnologias
- **Linguagem**: Java
- **Framework**: Spring Boot
- **Banco de Dados**: PostgreSQL / MySQL
- **Segurança**: Spring Security + JWT
- **Build**: Maven ou Gradle

## 📋 Pré-requisitos
- JDK 17+
- Maven ou Gradle
- Banco de Dados configurado

## ⚙️ Configuração
Arquivo `application.properties`:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/sghss
spring.datasource.username=usuario
spring.datasource.password=senha
spring.jpa.hibernate.ddl-auto=update
jwt.secret=troque-por-uma-chave-segura
jwt.expiration=3600000
```

## 🚀 Instalação e Execução
```bash
git clone https://github.com/Alan-Patrik/SGHSS.git
cd SGHSS/sghss-api
mvn clean install
mvn spring-boot:run
```

## ▶️ Uso (Endpoints)
Documente endpoints reais conforme implementação.

## 📂 Estrutura de Código
```
sghss-api/
├── src/
│   ├── main/java/com/sghss/
│   │   ├── config/
│   │   ├── controllers/
│   │   ├── services/
│   │   ├── repositories/
│   │   ├── entities/
│   │   └── dtos/
│   └── resources/
│       ├── application.properties
│       └── data.sql
└── pom.xml
```

## 🔐 Segurança
- Autenticação via JWT
- Criptografia de senhas (BCrypt)
- LGPD compliance

## 🧩 Roadmap
- [ ] Autenticação
- [ ] Módulo Pacientes
- [ ] Agenda
- [ ] Relatórios
- [ ] Integrações

## 👤 Autor
- Alan Patrik Fragozo dos Santos
- GitHub: [Alan-Patrik](https://github.com/Alan-Patrik)
