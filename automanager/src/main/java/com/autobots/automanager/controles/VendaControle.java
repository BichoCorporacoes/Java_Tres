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

import com.autobots.automanager.componentes.VendaSelecionador;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.servicos.VendaServico;

@RestController
@RequestMapping("/venda")
public class VendaControle {

	@Autowired
	private VendaServico servico;
	
	@Autowired
	private VendaSelecionador selecionador;
	
	@GetMapping("/vendas")
	public ResponseEntity<List<Venda>> pegarTodos(){
		List<Venda> todos = servico.pegarTodos();
		HttpStatus status = HttpStatus.CONFLICT;
		if(todos.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<List<Venda>>(status);
		}else {
			status = HttpStatus.FOUND;
			ResponseEntity<List<Venda>> resposta = new ResponseEntity<List<Venda>>(todos, status);
			return resposta;
		}
	}

	@GetMapping("/vendas/{id}")
	public ResponseEntity<Venda> pegarUsuarioEspecifico(@PathVariable Long id){
		List<Venda> todasEmpresas = servico.pegarTodos();
		Venda select = selecionador.selecionar(todasEmpresas, id);
		if(select == null) {
			return new ResponseEntity<Venda>(HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<Venda>(select, HttpStatus.FOUND);
		}
	}
	
	@PutMapping("/atualizar/{id}")
	public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @RequestBody Venda atualizador){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		List<Venda> usuarios = servico.pegarTodos();
		atualizador.setId(id);
		Venda usuario = selecionador.selecionar(usuarios, id);
		if (usuario != null) {
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
	
}
