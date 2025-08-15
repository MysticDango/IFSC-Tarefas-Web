package com.ifsc.tarefas.services;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifsc.tarefas.model.Categoria;
import com.ifsc.tarefas.repository.CatRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/categorias")
public class CatService {
    private final CatRepository catRepository;

    public CatService(CatRepository catRepository){
        this.catRepository = catRepository;
    }
    
    @GetMapping("/buscar-todos")
    public ResponseEntity<?> buscarTodas() {
        return ResponseEntity.ok(catRepository.findAll());
    }
    
    @PostMapping("/inserir")
    public ResponseEntity<Categoria> criarNovaCategoria(@RequestBody Categoria categoria){
        return ResponseEntity.ok(catRepository.save(categoria));
    }
    
    @PutMapping("editar/{id}")
    public ResponseEntity<Categoria> editarCategoria(@PathVariable Long id, @RequestBody Categoria novaCategoria){
        
        return catRepository.findById(id).map(

        categoria ->
        {
            categoria.setNome(novaCategoria.getNome());
            return ResponseEntity.ok(catRepository.save(categoria));
        }

        ).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("deletar/{id}")
    public ResponseEntity<Categoria> deletarCategoria(@PathVariable Long id){
        if(!catRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        catRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
