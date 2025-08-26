package com.ifsc.tarefas.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifsc.tarefas.model.Tarefa;
import com.ifsc.tarefas.repository.TarefaRepository;
import com.ifsc.tarefas.repository.CatRepository;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController //anotação que indica que essa classe é um service
@RequestMapping("/tarefas") //anotação que define padrão url exemplo: /tarefas/inserir
public class TarefaService {
    private final TarefaRepository tarefaRepository;
    private final CatRepository catRepository;

    //Injetando o repositorio de tarefa pra usar no service e buscar coisas no banco
    public TarefaService(TarefaRepository tarefaRepository, CatRepository catRepository){
        this.tarefaRepository = tarefaRepository;
        this.catRepository = catRepository;
    }

    //Anotação para GET
    // para chamar minha api de buscar ttodas eu uso --> /tarefas/buscar-todos <--
    @GetMapping("/buscar-todos")
    public ResponseEntity<?> buscarTodas(){
        //uso o repository para buscar todas as tarefas
        return ResponseEntity.ok(tarefaRepository.findAll());
    }

    //Api para criar uma nova tarefa
    //anotação para post --> /tarefas/inserir:
    //preciso informar a anotação @RequestBody para informar que vou enviar um body
    @PostMapping("/inserir")
    public ResponseEntity<Tarefa> criarNovaTarefa(@RequestBody Tarefa tarefa){
        return ResponseEntity.ok(tarefaRepository.save(tarefa));
    }

    @PutMapping("editar/{id}")
    public ResponseEntity<Tarefa> editarTarefa(@PathVariable Long id, @RequestBody Tarefa novaTarefa) {
        
        return tarefaRepository.findById(id).map
            (
            tarefa ->
            {
                tarefa.setTitulo(novaTarefa.getTitulo());
                tarefa.setDescricao(novaTarefa.getDescricao());
                tarefa.setResponsavel(novaTarefa.getResponsavel());
                tarefa.setDataLimite(novaTarefa.getDataLimite());
                tarefa.setStatus(novaTarefa.getStatus());
                tarefa.setPrioridade(novaTarefa.getPrioridade());
                return ResponseEntity.ok(tarefaRepository.save(tarefa));
            }
            
            ).orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("deletar/{id}")
    public ResponseEntity<Tarefa> deletarTarefa(@PathVariable Long id) {
        if(!tarefaRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        tarefaRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{tarefaId}/associar-categoria/{categoriaId}")
    
    @Transactional 
    public ResponseEntity<Void> associarTarefaParaUmaCategoria(
        @PathVariable Long tarefaId,
        @PathVariable Long categoriaId
    ){
        var tarefa = tarefaRepository.findById(tarefaId);
        var categoria = catRepository.findById(categoriaId);

        if(tarefa.isEmpty() || categoria.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        tarefa.get().getCategorias().add(categoria.get());

        tarefaRepository.save(tarefa.get());

        return ResponseEntity.ok().build();
    }
}