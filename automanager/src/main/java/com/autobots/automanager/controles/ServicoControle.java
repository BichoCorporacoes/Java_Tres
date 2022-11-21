package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.componentes.ServicoSelecionador;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.servicos.ServicoServico;

@RestController
@RequestMapping("/servico")
public class ServicoControle {

	@Autowired
	private ServicoServico servico;
	
	@Autowired
	private ServicoSelecionador selecionador;
	
	@GetMapping("/servicos")
	public ResponseEntity<List<Servico>> pegarTodos(){
		List<Servico> todos = servico.pegarTodos();
		HttpStatus status = HttpStatus.CONFLICT;
		if(todos.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<List<Servico>>(status);
		}else {
			status = HttpStatus.FOUND;
			ResponseEntity<List<Servico>> resposta = new ResponseEntity<List<Servico>>(todos, status);
			return resposta;
		}
	}

	@GetMapping("/servicos/{id}")
	public ResponseEntity<Servico> pegarUsuarioEspecifico(@PathVariable Long id){
		List<Servico> todasEmpresas = servico.pegarTodos();
		Servico select = selecionador.selecionar(todasEmpresas, id);
		if(select == null) {
			return new ResponseEntity<Servico>(HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<Servico>(select, HttpStatus.FOUND);
		}
	}
	
	@PutMapping("/atualizar/{id}")
	public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @RequestBody Servico atualizador){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		List<Servico> usuarios = servico.pegarTodos();
		atualizador.setId(id);
		Servico usuario = selecionador.selecionar(usuarios, id);
		if (usuario != null) {
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}
