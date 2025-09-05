package com.ifsc.tarefas.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tarefa_id")
    
    @NotBlank(message = "O campo título é obrigatório")

    @Size(min = 3, max = 100, message = "O campo título deve ter entre 3 e 100 caracteres")
    private String titulo;

    @NotBlank(message = "O campo título é obrigatório")

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @NotBlank(message = "O campo responsável é obrigatório")
    private String responsavel;

    private LocalDate dataCriacao = LocalDate.now();

    @FutureOrPresent(message = "A data limite deve ser futura ou no presente")
    private LocalDate dataLimite;

    //Cria uma relação de muitos para muitos
    @ManyToMany
    //Criar uma tabela intermediaria que tem o id da tarefa e categoria
    @JoinTable(
        // nome da tabela
        name = "tarefa_categoria",
        // o atributo desse objeto juntara a tarefa
        joinColumns =  @JoinColumn(name = "tarefa_id"),
        //o atributo de outro objeto que se juntara a tabela intermediaria
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )

    private Set<Categoria> categorias = new HashSet<>();
    
    @NotNull(message = "O campo status é obrigatório")
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull(message = "O campo prioridade é obrigatório")
    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public Prioridade getPrioridade() {
        return prioridade;
    }
    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getResponsavel() {
        return responsavel;
    }
    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }
    public LocalDate getDataCriacao() {
        return dataCriacao;
    }
    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    public LocalDate getDataLimite() {
        return dataLimite;
    }
    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }
    public Set<Categoria> getCategorias() {
        return categorias;
    }
    public void setCategorias(Set<Categoria> categorias) {
        this.categorias = categorias;
    }

}
