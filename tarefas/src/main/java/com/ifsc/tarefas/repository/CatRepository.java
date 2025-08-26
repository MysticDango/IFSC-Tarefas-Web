package com.ifsc.tarefas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifsc.tarefas.model.Categoria;


public interface CatRepository extends JpaRepository<Categoria, Long>{
        //Optional = pode ser nulo o nome
    // findByNome = procurar alguem pro nome
    Optional<Categoria> findByNome(String nome);

    boolean existsByNome(String nome);

}
