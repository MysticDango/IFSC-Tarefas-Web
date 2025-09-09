package com.ifsc.tarefas.services;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ifsc.tarefas.model.Prioridade;
import com.ifsc.tarefas.model.Tarefa;
import com.ifsc.tarefas.model.Categoria;
import com.ifsc.tarefas.model.Status;
import com.ifsc.tarefas.repository.CatRepository;
import com.ifsc.tarefas.repository.TarefaRepository;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/templates")
public class TemplateService {

    private final TarefaRepository tarefaRepository;
    private final CatRepository catRepository;

    public TemplateService(TarefaRepository tarefaRepository, CatRepository catRepository) {
        this.tarefaRepository = tarefaRepository;
        this.catRepository = catRepository;
    }

    @GetMapping("/listar")
    // O model é para adicionar atributos para minha view
    // O html no caso
    String listarTarefas(Model model, @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String responsavel,
            @RequestParam(required = false) Prioridade prioridade,
            @RequestParam(required = false) Status status) {
        var tarefas = tarefaRepository.findAll();
        if (titulo != null && !titulo.trim().isEmpty()) {
            // Java transformando em stream
            // filtra as tarefas pelo título
            tarefas = tarefas.stream().filter(t -> t.getTitulo().contains(titulo)).toList();
        }

        if (responsavel != null && !responsavel.trim().isEmpty()) {
            tarefas = tarefas.stream().filter(t -> t.getResponsavel().contains(responsavel)).toList();
        }

        if (prioridade != null) {
            tarefas = tarefas.stream().filter(t -> t.getPrioridade().equals(prioridade)).toList();
        }

        if (status != null) {
            tarefas = tarefas.stream().filter(t -> t.getStatus().equals(status)).toList();
        }

        model.addAttribute("tarefas", tarefas);
        model.addAttribute("responsavel", responsavel);
        model.addAttribute("prioridade", prioridade);
        model.addAttribute("status", status);

        model.addAttribute("listaPrioridade", Prioridade.values());
        model.addAttribute("listaStatus", Status.values());

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
    String salvar(@ModelAttribute("tarefa") Tarefa tarefa, BindingResult br, Model model, RedirectAttributes ra) {
        // redireciona depois de salvar direto pra listagem

        // Validação:
        // vou ver se tem algum erro no formulário
        if (br.hasErrors()) {
            model.addAttribute("tarefa", tarefa);
            model.addAttribute("prioridades", Prioridade.values());
            model.addAttribute("listaStatus", Status.values());
            model.addAttribute("erros", "Erro ao salvar tarefa, preencha os campos corretamente");
            // se tiver erro volta pra pagina
            return "tarefa";
        }

        // Redireciona depois de salvar direto para listagem
        ra.addFlashAttribute("sucesso", "Tarefa salva com sucesso!");
        tarefaRepository.save(tarefa);
        return "redirect:/templates/listar";
    }

    // Deletar uma tarefa
    @PostMapping("{id}/excluir")
    String excluir(@PathVariable Long id) {
        tarefaRepository.deleteById(id);
        return "redirect:/templates/listar";
    }

    @GetMapping("/{id}/editar")
    String editar(@PathVariable Long id, Model model) {
        // vai procurar tarefas pelo id, se n achar
        var tarefa = tarefaRepository.findById(id).orElse(null);
        if (tarefa == null) {
            // retornar para pagina inicial
            return "redirect:/templates/listar";
        }
        model.addAttribute("tarefa", tarefa);
        model.addAttribute("prioridades", Prioridade.values());
        model.addAttribute("listaStatus", Status.values());
        return "tarefa";
    }

    // tela de associar tarefas e categorias
    @GetMapping("/{tarefaId}/associar-categoria/{categoriaId}")
    String associarTarefaParaUmaCategoria(Model model, @PathVariable Long tarefaId) {

        List<Categoria> categorias = catRepository.findAll();
        model.addAttribute("categorias", catRepository.findAll());
        for (Categoria categoria : categorias) {
            System.out.println("todas as categorias " + categoria.getNome() + " - " + categoria.getId());
        }

        var tarefa = tarefaRepository.findById(tarefaId);
        model.addAttribute("tarefa", tarefa.get());

        return "gerenciar-categoria";

    }

    @PostMapping("/{tarefaId}/associar-categoria/{categoriaId}")
    String associarTarefaParaUmaCategoria(@PathVariable Long tarefaId, @PathVariable Long categoriaId) {
        var tarefa = tarefaRepository.findById(tarefaId);
        var categoria = catRepository.findById(categoriaId);

        if (tarefa.isEmpty() || categoria.isEmpty()) {
            return "redirect:/templates/listar";
        }

        tarefa.get().getCategorias().add(categoria.get());

        tarefaRepository.save(tarefa.get());
        return "redirect:/templates/listar";
    }

}