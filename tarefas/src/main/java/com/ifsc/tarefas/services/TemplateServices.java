package com.ifsc.tarefas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ifsc.tarefas.model.Prioridade;
import com.ifsc.tarefas.model.Status;
import com.ifsc.tarefas.model.Tarefa;
import com.ifsc.tarefas.auth.RequestAuth;
import com.ifsc.tarefas.model.Categoria;
import com.ifsc.tarefas.repository.CatRepository;
import com.ifsc.tarefas.repository.TarefaRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

// anotação pra dizer que é um controller
@Controller
@RequestMapping("/templates")
public class TemplateServices {
    private final TarefaRepository tarefaRepository;
    // adicionando o repositorio de categorias pra associar
    private final CatRepository catRepository;

    // colocar o nome dessa função o mesmo nome do arquivo que estamos atualmente:
    public TemplateServices(TarefaRepository tarefaRepository, CatRepository catRepository) {
        this.tarefaRepository = tarefaRepository;
        this.catRepository = catRepository;
    }

    // pagina de listagem de tarefas
    @GetMapping("/listar")
    // O model para adicionar atributos para minha view
    // o html no caso
    String listarTarefas(Model model, 
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String responsavel, 
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Prioridade prioridade, 
            HttpServletRequest req) {
        // o primeiro argumento é para como esse objeto sera chamado dentro da view
        // o segundo argumento é o objeto(s) que irei passar
        // se eu tenho titulo e ele não é vazio
        String user = RequestAuth.getUser(req);
        String role = RequestAuth.getRole(req);
            
        var tarefas = role.equals("ADMIN") ? 
            tarefaRepository.findAll() :
            tarefaRepository.findByResponsavel(user);



        if (titulo != null && !titulo.trim().isEmpty()) {
            // Java transformando em stream
            // filtra as tarefas pelo titulo
            tarefas = tarefas.stream().filter(t -> t.getTitulo().toLowerCase().contains(titulo.toLowerCase())).toList();
        }

        if (responsavel != null && !responsavel.trim().isEmpty()) {
            tarefas = tarefas.stream().filter(t -> t.getResponsavel().toLowerCase().contains(responsavel.toLowerCase()))
                    .toList();
        }

        if (status != null) {
            tarefas = tarefas.stream().filter(t -> t.getStatus() == status).collect(Collectors.toList());
        }

        if (prioridade != null) {
            tarefas = tarefas.stream().filter(t -> t.getPrioridade() == prioridade).collect(Collectors.toList());
        }

        model.addAttribute("tarefas", tarefas);
        model.addAttribute("listaPrioridade", Prioridade.values());
        model.addAttribute("listaStatus", Status.values());

        model.addAttribute("titulo", titulo);
        model.addAttribute("status", status);
        model.addAttribute("responsavel", responsavel);
        model.addAttribute("prioridade", prioridade);

        // Vai dizer qual template eu quero usar
        return "lista";
    }

    // pagina de criar nova tarefa
    @GetMapping("/nova-tarefa")
    String novaTarefa(Model model) {
        // quem vamos salvar no banco
        model.addAttribute("tarefa", new Tarefa());
        // pegamos os valores e prioridade
        model.addAttribute("prioridades", Prioridade.values());
        // e dos status
        model.addAttribute("listaStatus", Status.values());
        return "nova-tarefa";
    }

    // Api que vai salvar o formulario
    @PostMapping("/salvar")
    // modelAttribute vai pegar os dados do objeto do nome que passamos
    // e que veio na view
    String salvar(@Valid @ModelAttribute("tarefa") Tarefa tarefa, BindingResult br, Model model,
            RedirectAttributes ra) {
        // redireciona depois de salvar direto pra listagem

        // validação :
        // vou ver se tem algum erro no formulario
        if (br.hasErrors()) {
            model.addAttribute("tarefa", tarefa);
            model.addAttribute("prioridades", Prioridade.values());
            model.addAttribute("listaStatus", Status.values());
            model.addAttribute("erros", "Erro ao salvar tarefa, preencha os campos corretamente.");
            // se tiver erro volta pra pagina
            return "nova-tarefa";
        }

        // Adiciona atributos quando voltar para a pagina listar
        ra.addFlashAttribute("sucesso", "Tarefa salva com sucesso!");

        tarefaRepository.save(tarefa);
        return "redirect:/templates/listar";
    }

    // deletar uma terefa com thymelaf
    @PostMapping("/{id}/excluir")
    String excluir(@PathVariable Long id) {
        tarefaRepository.deleteById(id);
        return "redirect:/templates/listar";
    }

    // pagina de editar
    @GetMapping("/{id}/editar-tarefa")
    String editar(@PathVariable Long id, Model model) {
        // vai procurar tarefas pelo id, se n achar
        var tarefa = tarefaRepository.findById(id).orElse(null);
        if (tarefa == null) {
            // retornar para pagina inicial
            return "redirect:/templates/listar";
        }
        // adiciona a tarefa que vai ser editada todo o resto ao template do
        // formulario
        model.addAttribute("tarefa", tarefa);
        model.addAttribute("prioridades", Prioridade.values());
        model.addAttribute("listaStatus", Status.values());
        return "editar-tarefa";
    }

    // tela de associar tarefas e categorias
    @GetMapping("/{tarefaId}/associar-categoria")
    String associarTarefaParaUmaCategoria(Model model, @PathVariable Long tarefaId) {
        // passar categorias
        // uso o repository pra buscar todas as categorias
        List<Categoria> categorias = catRepository.findAll();
        model.addAttribute("categorias", categorias);
        for (Categoria categoria : categorias) {
            System.out.println("todas as categorias " + categoria.getNome() + " - " + categoria.getId());
        }
        // procuro pelo id da terefa pra ter ela salvo
        var tarefa = tarefaRepository.findById(tarefaId);
        model.addAttribute("tarefa", tarefa.get());

        return "gerenciar-categoria";

    }

    @PostMapping("/{tarefaId}/associar-categoria/{categoriaId}")
    String associarTarefaParaUmaCategoria(@PathVariable Long tarefaId, @PathVariable Long categoriaId) {
        var tarefa = tarefaRepository.findById(tarefaId);
        var categoria = catRepository.findById(categoriaId);

        // se não achar a tarefa ou a categoria retorna que não encontrou nada
        if (tarefa.isEmpty() || categoria.isEmpty()) {
            return "redirect:/templates/listar";
        }

        tarefa.get().getCategorias().add(categoria.get());
        // salva no banco
        tarefaRepository.save(tarefa.get());
        return "redirect:/templates/listar";

    }

}