package com.ifsc.tarefas.services;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ifsc.tarefas.model.Prioridade;
import com.ifsc.tarefas.model.Tarefa;
import com.ifsc.tarefas.model.Status;
import com.ifsc.tarefas.repository.TarefaRepository;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/templates")
public class TemplateService {

    private final TarefaRepository tarefaRepository;

    public TemplateService(TarefaRepository tarefaRepository){
        this.tarefaRepository = tarefaRepository;
    }


    @GetMapping("/listar")
    // O model é para adicionar atributos para minha view
    // O html no caso
    String listarTarefas(Model model){
        model.addAttribute("tarefas", tarefaRepository.findAll());

        // Vai dizer qual template vai usar
        return "lista";
    }

    @GetMapping("/nova-tarefa")
    String novaTarefa(Model model) {
        model.addAttribute("tarefa", new Tarefa());
        model.addAttribute("prioridades", Prioridade.values());
        model.addAttribute("listaStatus", Status.values());
        return "nova-tarefa";
    }

    @PostMapping("/salvar")
    // modelAttribute vai pegar os dados do objeto do nome que passamos
    // e que veio na view
    String salvar(@ModelAttribute("tarefa")Tarefa tarefa){
        tarefaRepository.save(tarefa);
        //Redireciona depois de salvar direto para listagem
        return "redirect:/templates/listar";
    }

    //Deletar uma tarefa
    @PostMapping("{id}/excluir")
    String excluir(@PathVariable Long id) {
        tarefaRepository.deleteById(id);
        return "redirect:/templates/listar";
    }

    @GetMapping("{id}/editar")
        String editar(@PathVariable Long id, Model model) {
        // vai procurar tarefas pelo id, se n achar
        var tarefa = tarefaRepository.findById(id).orElse(null);
        if(tarefa == null) {
            // retornar para pagina inicial
            return "redirect:/templates/listar";
        }
        model.addAttribute("tarefa", tarefa);
        model.addAttribute("prioridades", Prioridade.values());
        model.addAttribute("listaStatus", Status.values());
        return "tarefa";
    }
    
    
}
    
    
