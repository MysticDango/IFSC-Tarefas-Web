package com.ifsc.tarefas.services;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ifsc.tarefas.repository.CatRepository;
import com.ifsc.tarefas.model.Categoria;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/templates-cat")
public class CatTemplateService {

    private final CatRepository catRepository;

    public CatTemplateService(CatRepository catRepository){
        this.catRepository = catRepository;
    }


    @GetMapping("/listar-cat")
    // O model é para adicionar atributos para minha view
    // O html no caso
    String listarTarefas(Model model){
        model.addAttribute("categorias", catRepository.findAll());

        // Vai dizer qual template vai usar
        return "categoria";
    }

    @GetMapping("/nova-categoria")
    String novaTarefa(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "nova-categoria";
    }

    @PostMapping("/salvar")
    // modelAttribute vai pegar os dados do objeto do nome que passamos
    // e que veio na view
    String salvar(@ModelAttribute("categoria")Categoria categoria){
        catRepository.save(categoria);
        //Redireciona depois de salvar direto para listagem
        return "redirect:/templates-cat/listar-cat";
    }

    //Deletar uma tarefa
    @PostMapping("{id}/excluir")
    String excluir(@PathVariable Long id) {
        catRepository.deleteById(id);
        return "redirect:/templates-cat/listar-cat";
    }

    @GetMapping("{id}/editar")
        String editar(@PathVariable Long id, Model model) {
        // vai procurar tarefas pelo id, se n achar
        var categoria = catRepository.findById(id).orElse(null);
        if(categoria == null) {
            // retornar para pagina inicial
            return "redirect:/templates-cat/listar-cat";
        }
        model.addAttribute("categoria", categoria);
        return "categoria";
    }
    
    
}
    
    
