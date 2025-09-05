package com.ifsc.tarefas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifsc.tarefas.model.Prioridade;
import com.ifsc.tarefas.model.Status;
import com.ifsc.tarefas.model.Tarefa;
import java.time.LocalDate;


// Fala direto com o nosso banco de dados
// Com o JpaRepository ja criamos o CRUD inicial

public interface TarefaRepository extends JpaRepository<Tarefa, Long>{
    //exigente com nomenclatura
    //vai buscar todos as tarefas que são iguais ao titulo passado

    List<Tarefa> findByTitulo(String titulo);

    List<Tarefa> findByStatus(Status status);

    List<Tarefa> findByResponsavel(String responsavel);

    List<Tarefa> findByDataLimite(LocalDate dataLimite);

    List<Tarefa> findByStatusAndPrioridade(Status status, Prioridade prioridade);
        
}
// insert
// select
// select por id
// delete
