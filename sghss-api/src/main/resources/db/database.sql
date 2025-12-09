-- =============================================================================
-- SGHSS - DDL MySQL 8
-- =============================================================================
CREATE DATABASE IF NOT EXISTS sghss
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_0900_ai_ci
;

USE sghss;


-- =============================================================================
-- ROLE
-- =============================================================================
CREATE TABLE IF NOT EXISTS role (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome         ENUM('ADMIN','PROFISSIONAL','PACIENTE') NOT NULL,
    descricao    VARCHAR(120) NULL,
    UNIQUE KEY uk_role_nome (nome)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;


-- =============================================================================
-- USUARIO
-- =============================================================================
CREATE TABLE IF NOT EXISTS usuario (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome          VARCHAR(120) NOT NULL,
    email         VARCHAR(180) NOT NULL,
    senha_hash    VARCHAR(255) NOT NULL,
    ativo         TINYINT(1) NOT NULL DEFAULT 1,
    criado_em     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_usuario_email (email),
    INDEX ix_usuario_ativo (ativo)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;


-- =============================================================================
-- USUARIO_ROLE
-- =============================================================================
CREATE TABLE IF NOT EXISTS usuario_role (
    usuario_id BIGINT NOT NULL,
    role_id    BIGINT NOT NULL,
    atribuido_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (usuario_id, role_id),
    CONSTRAINT fk_ur_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_ur_role
    FOREIGN KEY (role_id) REFERENCES role(id)
    ON UPDATE CASCADE ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;


-- =============================================================================
-- UNIDADE_SAUDE
-- =============================================================================
CREATE TABLE IF NOT EXISTS Unidade_Saude (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(150) NOT NULL,
    endereco  VARCHAR(255) NOT NULL,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_unidade_nome (nome)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;


-- =============================================================================
-- PACIENTE
-- =============================================================================
CREATE TABLE IF NOT EXISTS Paciente (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome             VARCHAR(150) NOT NULL,
    cpf              CHAR(11)     NOT NULL,
    data_nascimento  DATE         NOT NULL,
    telefone         VARCHAR(20)  NULL,
    email            VARCHAR(180) NULL,
    criado_em        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_paciente_cpf (cpf),
    INDEX ix_paciente_nome (nome),
    INDEX ix_paciente_email (email)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;


-- =============================================================================
-- PROFISSIONAL
-- =============================================================================
CREATE TABLE IF NOT EXISTS Profissional (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome         VARCHAR(150) NOT NULL,
    registro     VARCHAR(30)  NOT NULL,
    especialidade VARCHAR(80) NOT NULL,
    unidade_id   BIGINT       NOT NULL,
    ativo        TINYINT(1)   NOT NULL DEFAULT 1,
    criado_em    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_prof_registro (registro),
    INDEX ix_prof_nome (nome),
    INDEX ix_prof_unidade (unidade_id),
    CONSTRAINT fk_prof_unidade
    FOREIGN KEY (unidade_id) REFERENCES unidade(id)
                                                                 ON UPDATE CASCADE ON DELETE RESTRICT
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;


-- =============================================================================
-- LEITO
-- =============================================================================
CREATE TABLE IF NOT EXISTS Leito (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo       VARCHAR(20) NOT NULL,
    tipo         VARCHAR(40) NOT NULL,
    status       ENUM('LIVRE','OCUPADO','MANUTENCAO') NOT NULL DEFAULT 'LIVRE',
    unidade_id   BIGINT NOT NULL,
    paciente_id  BIGINT NULL,
    criado_em    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_leito_codigo (codigo),
    INDEX ix_leito_unidade (unidade_id),
    INDEX ix_leito_paciente (paciente_id),
    CONSTRAINT fk_leito_unidade
    FOREIGN KEY (unidade_id) REFERENCES unidade(id)
                                                              ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_leito_paciente
    FOREIGN KEY (paciente_id) REFERENCES paciente(id)
                                                              ON UPDATE CASCADE ON DELETE SET NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;


-- =============================================================================
-- CONSULTA
-- =============================================================================
CREATE TABLE IF NOT EXISTS Consulta (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_hora        DATETIME     NOT NULL,
    status           ENUM('AGENDADA','REALIZADA','CANCELADA') NOT NULL DEFAULT 'AGENDADA',
    observacoes      TEXT         NULL,
    paciente_id      BIGINT       NOT NULL,
    profissional_id  BIGINT       NOT NULL,
    criado_em        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX ix_consulta_datahora (data_hora),
    INDEX ix_consulta_paciente (paciente_id),
    INDEX ix_consulta_profissional (profissional_id),
    CONSTRAINT fk_consulta_paciente
    FOREIGN KEY (paciente_id) REFERENCES paciente(id)
                                                                     ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_consulta_profissional
    FOREIGN KEY (profissional_id) REFERENCES profissional(id)
                                                                     ON UPDATE CASCADE ON DELETE RESTRICT
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

CREATE UNIQUE INDEX ux_consulta_prof_datahora
    ON consulta (profissional_id, data_hora)
;


-- =============================================================================
-- AUDITORIA
-- =============================================================================
CREATE TABLE IF NOT EXISTS Auditoria (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_hora      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario_email  VARCHAR(180) NOT NULL,
    acao           VARCHAR(120) NOT NULL,
    ip             VARCHAR(45)  NULL,
    detalhes       JSON         NULL,
    INDEX ix_log_data (data_hora),
    INDEX ix_log_usuario (usuario_email)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

