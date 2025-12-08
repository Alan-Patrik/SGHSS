package com.alanpatrik.sghss.api.comum;

public interface Constantes {
    /*
     * Mensagens
     * */
    String OK_MESSAGE = "SUCCESS";
    String NO_CONTENT_MESSAGE = "SUCCESS";
    String CREATE_MESSAGE = "SUCCESS";
    String ERRO_MESSAGE = "ERROR";
    String BAD_REQUEST_MESSAGE = "Paramêtro(s) de entrada inválido(s).";
    String NOT_FOUND_MESSAGE = "Registro não encontrado.";
    String CONFLICT_MESSAGE = "Registro já possui cadastro.";
    String UNIDADE_SAUDE_NOT_FOUND_BY_ID_MESSAGE = "Unidade de Saúde com o id: %s não encontrado.";
    String UNIDADE_SAUDE_NOT_FOUND_BY_NAME_MESSAGE = "Unidade de Saúde com o nome: %s não encontrado.";
    String LEITO_NOT_FOUND_BY_ID_MESSAGE = "Leito com o id: %s não encontrado.";
    String LEITO_NOT_FOUND_BY_NUMERO_MESSAGE = "Leito com o número: %s não encontrado.";
    String PROFISSIONAL_SAUDE_NOT_FOUND_BY_ID_MESSAGE = "Profissional de saúde com o id: %s não encontrado.";
    String PROFISSIONAL_SAUDE_NOT_FOUND_BY_CRM_MESSAGE = "Profissional de saúde com o CRM: %s não encontrado.";
    String PACIENTE_NOT_FOUND_BY_ID_MESSAGE = "Paciente com o id: %s não encontrado.";
    String PACIENTE_NOT_FOUND_BY_NAME_MESSAGE = "Paciente com o nome: %s não encontrado.";
    String PRONTUARIO_NOT_FOUND_BY_ID_MESSAGE = "Prontuário com o id: %s não encontrado.";
    String PRESCRICAO_NOT_FOUND_BY_ID_MESSAGE = "Prescrição com o id: %s não encontrada.";
    String EXAME_NOT_FOUND_BY_ID_MESSAGE = "Exame com o id: %s não encontrado.";
    String EXAME_CONFLICT_BY_PAGAMENTO_MESSAGE = "Exame com o id: %s já está pago.";
    String CONSULTA_NOT_FOUND_BY_ID_MESSAGE = "Consulta com o id: %s não encontrada.";
    String CONSULTA_NOT_FOUND_BY_DATA_HORA_MESSAGE = "Consulta pela data/hora: %s não encontrada.";
    String CONSULTA_BAD_REQUEST_MESSAGE = "Não foi possível prosseguir. Consulta se encontra disponível.";
    String CONSULTA_CONFLICT_BY_PAGAMENTO_MESSAGE = "Consulta com o id: %s já está paga.";
    String AGENDA_NOT_FOUND_BY_ID_MESSAGE = "Agenda pelo id: %s não encontrada.";
    String AGENDA_NOT_FOUND_BY_HORARIO_MESSAGE = "Horário %s não encontrada.";
    String AGENDA_NOT_FOUND_BY_HORARIO_DISPONIVEL_MESSAGE = "Horário da nova consulta: %s não encontrada ou indisponível.";
    String AGENDA_CONFLICT_BY_HORARIO_MESSAGE = "Horário informado já se encontra disponível.";
    String AUDITORIA_NOT_FOUND_BY_ID_MESSAGE = "Auditoria com o id: %s não encontrada.";
    String PRIVILEGIO_NOT_FOUND_BY_NAME_MESSAGE = "Privilégio com o nome: %s não encontrado.";
    String PRIVILEGIO_NOT_FOUND_BY_ID_MESSAGE = "Privilégio com o id: %s não encontrado.";
    String PRIVILEGIO_CONFLICT_BY_NAME_MESSAGE = "Privilegio com o nome: %s já existe.";
    String USUARIO_NOT_FOUND_BY_ID_MESSAGE = "Usuário com o id: %s não encontrado.";
    String USUARIO_NOT_FOUND_BY_USERNAME_MESSAGE = "Usuário com o userename: %s não encontrado.";
    String ROLE_NOT_FOUND_BY_ID_MESSAGE = "Role com o id: %s não encontrada.";
    String ROLE_NOT_FOUND_BY_AUTHORITY_MESSAGE = "Role com o authority: %s não encontrada.";
    String ROLE_CONFLICT_MESSAGE = "Role com o authority: %s já existe.";

    /**
     * Roles
     *
     */
    String LOGON_ROLE_ADMIN_SISTEMA = "SGHSS.ADMIN.SISTEMA";
    String LOGON_ROLE_TELEMEDICINA = "SGHSS.TELEMEDICINA";
    String LOGON_ROLE_ENFERMEIRO = "SGHSS.ENFERMEIRO";
    String LOGON_ROLE_PACIENTE = "SGHSS.PACIENTE";
    String LOGON_ROLE_TECNICO = "SGHSS.TECNICO";
    String LOGON_ROLE_MEDICO = "SGHSS.MEDICO";

    /**
     * Privilégios
     *
     */
    String PRIV_VISUALIZAR_HISTORICO = "SGHSS.VISUALIZAR_HISTORICO";
    String PRIV_ATUALIZAR_PRONTUARIO = "SGHSS.ATUALIZAR_PRONTUARIO";
    String PRIV_RECEBER_NOTIFICACOES = "SGHSS.RECEBER_NOTIFICACOES";
    String PRIV_ACESSAR_TELECONSULTA = "SGHSS.ACESSAR_TELECONSULTA";
    String PRIV_GERENCIAR_CADASTROS = "SGHSS.GERENCIAR_CADASTROS";
    String PRIV_GERENCIAR_SEGURANCA = "SGHSS.GERENCIAR_SEGURANCA";
    String PRIV_CONTROLAR_INTER = "SGHSS.CONTROLAR_INTERNACOES";
    String PRIV_CADASTRAR_PACIENTE = "SGHSS.CADASTRAR_PACIENTE";
    String PRIV_GERENCIAR_AGENDAS = "SGHSS.GERENCIAR_AGENDAS";
    String PRIV_AGENDAR_CONSULTA = "SGHSS.AGENDAR_CONSULTA";
    String PRIV_GERAR_RELATORIOS = "SGHSS.GERAR_RELATORIOS";
    String PRIV_EMITIR_RECEITA = "SGHSS.EMITIR_RECEITA";
    String PRIV_ATUALIZAR_PACIENTE = "SGHSS.ATUALIZAR_PACIENTE";
    String PRIV_CANCELAR_CONSULTA = "SGHSS.CANCELAR_CONSULTA";
    String PRIV_VISUALIZAR_PACIENTE = "SGHSS.VISUALIZAR_PACIENTE";
    String PRIV_PAGAR_CONSULTA = "SGHSS.PAGAR_CONSULTA";
    String PRIV_PAGAR_EXAME = "SGHSS.PAGAR_EXAME";
}
