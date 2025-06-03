CREATE TABLE usuario
(
    cpf  VARCHAR(11) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
);

CREATE TABLE pauta
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome         VARCHAR(255) NOT NULL,
    data_criacao DATETIME     NOT NULL
);

CREATE TABLE controle_sessao
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    pauta_id        BIGINT   NOT NULL UNIQUE,
    data_abertura   DATETIME NOT NULL,
    data_fechamento DATETIME NOT NULL,
    FOREIGN KEY (pauta_id) REFERENCES pauta (id) ON DELETE CASCADE
);

CREATE TABLE voto
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    pauta_id    BIGINT      NOT NULL,
    cpf_usuario VARCHAR(11) NOT NULL,
    voto        BOOLEAN     NOT NULL, -- TRUE = SIM, FALSE = NÃO
    data_voto   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pauta_id) REFERENCES pauta (id) ON DELETE CASCADE,
    FOREIGN KEY (cpf_usuario) REFERENCES usuario (cpf) ON DELETE CASCADE,
    UNIQUE (pauta_id, cpf_usuario)
);