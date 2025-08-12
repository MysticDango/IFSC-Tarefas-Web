package com.ifsc.tarefas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifsc.tarefas.model.Tarefa;

// Fala direto com o nosso banco de dados
// Com o JpaRepository ja criamos o CRUD inicial

public interface TarefaRepository extends JpaRepository<Tarefa, Long>{}
// insert
// select
// select por id
// delete
