package com.autobots.automanager.controles;

import com.autobots.automanager.componentes.MercadoriaSelecionador;
import com.autobots.automanager.componentes.UsuariosSelecionador;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.servicos.MercadoriaServico;
import com.autobots.automanager.servicos.UsuarioServico;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mercadoria")
public class MercadoriaControle {

  @Autowired
  private MercadoriaServico servico;

  @Autowired
  private UsuarioServico servicoUsuario;

  @Autowired
  private UsuariosSelecionador usuarioSelecionador;

  @Autowired
  private MercadoriaSelecionador selecionador;

  @GetMapping("/mercadorias")
  public ResponseEntity<List<Mercadoria>> pegarTodos() {
    List<Mercadoria> todos = servico.pegarTodos();
    HttpStatus status = HttpStatus.CONFLICT;
    if (todos.isEmpty()) {
      status = HttpStatus.NOT_FOUND;
      return new ResponseEntity<List<Mercadoria>>(status);
    } else {
      status = HttpStatus.FOUND;
      ResponseEntity<List<Mercadoria>> resposta = new ResponseEntity<List<Mercadoria>>(
        todos,
        status
      );
      return resposta;
    }
  }

  @GetMapping("/mercadorias/{id}")
  public ResponseEntity<Mercadoria> pegarMercadoriaEspecifica(
    @PathVariable Long id
  ) {
    List<Mercadoria> todasEmpresas = servico.pegarTodos();
    Mercadoria select = selecionador.selecionar(todasEmpresas, id);
    if (select == null) {
      return new ResponseEntity<Mercadoria>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<Mercadoria>(select, HttpStatus.FOUND);
    }
  }

  @PutMapping("/atualizar/{id}")
  public ResponseEntity<?> atualizarMercadoria(
    @PathVariable Long id,
    @RequestBody Mercadoria atualizador
  ) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    List<Mercadoria> usuarios = servico.pegarTodos();
    atualizador.setId(id);
    Mercadoria usuario = selecionador.selecionar(usuarios, id);
    if (usuario != null) {
      status = HttpStatus.OK;
    }
    return new ResponseEntity<>(status);
  }

  @PostMapping("/cadastro/{idCliente}")
  public ResponseEntity<?> cadastroMercadorias(
    @RequestBody Mercadoria cadastro,
    @PathVariable Long idCliente
  ) {
    servico.salvar(cadastro);
    List<Usuario> getAllUsuarios = servicoUsuario.pegarTodos();
    Usuario select = usuarioSelecionador.selecionar(getAllUsuarios, idCliente);
    if (select == null) {
      return new ResponseEntity<>(
        "Usuario não encontrado",
        HttpStatus.NOT_FOUND
      );
    } else {
      select.getMercadorias().add(cadastro);
      servicoUsuario.salvarUsuario(select);
      return new ResponseEntity<>(
        "Mercadoria Cadastrada com sucesso",
        HttpStatus.CREATED
      );
    }
  }

  @DeleteMapping("/deletar/{idCliente}/{idMercadoria}")
  public ResponseEntity<?> deletarMercadoria(
    @PathVariable Long idCliente,
    @PathVariable Long idMercadoria
  ) {
	List<Usuario> usuarios = servicoUsuario.pegarTodos();
	Usuario selecionado = usuarioSelecionador.selecionar(usuarios, idMercadoria);
	if(selecionado == null) {
		return null;
	}else {
		servicoUsuario.deletarMercadoria(idCliente, idMercadoria);
		servico.delete(idMercadoria);
	    return null;
	}
  }

  @PutMapping("/atualizar/{idCliente}/{idMercadoria}")
  public ResponseEntity<?> atualizarMercadoria(
    @PathVariable Long idCliente,
    @PathVariable Long idMercadoria,
    @RequestBody Mercadoria atualizador
  ) {
    List<Usuario> getAllClientes = servicoUsuario.pegarTodos();
    List<Mercadoria> getAllMercadorias = servico.pegarTodos();
    Mercadoria selectMercadoria = selecionador.selecionar(
      getAllMercadorias,
      idMercadoria
    );
    Usuario selectUsuario = usuarioSelecionador.selecionar(
      getAllClientes,
      idCliente
    );
    if (selectMercadoria == null || selectUsuario == null) {
      return new ResponseEntity<>(
        "Usuario ou mercadoria não encontrado",
        HttpStatus.NOT_FOUND
      );
    } else {
      for (Mercadoria listagemMercadoria : getAllMercadorias) {
        if (listagemMercadoria.getId() == idMercadoria.longValue()) {
          atualizador.setId(idMercadoria);
          servico.update(atualizador);
        }
      }
      return new ResponseEntity<>("Atualizado com sucesso", HttpStatus.OK);
    }
  }
}
