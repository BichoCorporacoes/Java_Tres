package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.componentes.MercadoriaSelecionador;
import com.autobots.automanager.dto.MercadoriaDto;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.servicos.MercadoriaServico;

@RestController
@RequestMapping("/mercadoria")
public class MercadoriaControle {

	@Autowired
	private MercadoriaServico servico;
	
	@Autowired
	private MercadoriaSelecionador selecionador;
	
	@GetMapping("/mercadorias")
	public ResponseEntity<List<Mercadoria>> pegarTodos(){
		List<Mercadoria> todos = servico.pegarTodos();
		HttpStatus status = HttpStatus.CONFLICT;
		if(todos.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<List<Mercadoria>>(status);
		}else {
			status = HttpStatus.FOUND;
			ResponseEntity<List<Mercadoria>> resposta = new ResponseEntity<List<Mercadoria>>(todos, status);
			return resposta;
		}
	}

	@GetMapping("/mercadorias/{id}")
	public ResponseEntity<Mercadoria> pegarMercadoriaEspecifica(@PathVariable Long id){
		List<Mercadoria> todasEmpresas = servico.pegarTodos();
		Mercadoria select = selecionador.selecionar(todasEmpresas, id);
		if(select == null) {
			return new ResponseEntity<Mercadoria>(HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<Mercadoria>(select, HttpStatus.FOUND);
		}
	}
	
	@PutMapping("/atualizar/{id}")
	public ResponseEntity<?> atualizarMercadoria(@PathVariable Long id, @RequestBody Mercadoria atualizador){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		List<Mercadoria> usuarios = servico.pegarTodos();
		atualizador.setId(id);
		Mercadoria usuario = selecionador.selecionar(usuarios, id);
		if (usuario != null) {
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
	
	@PostMapping("/cadastro")
	public ResponseEntity<Mercadoria> cadastroMercadorias(@RequestBody Mercadoria cadastro){
		servico.salvar(cadastro);
		return null;
	}
}
