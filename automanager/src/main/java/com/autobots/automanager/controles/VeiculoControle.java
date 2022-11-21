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

import com.autobots.automanager.componentes.VeiculoSelecionador;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.servicos.VeiculoServico;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControle {
	
	@Autowired
	private VeiculoServico veiculoServico;
	
	@Autowired
	private VeiculoSelecionador selecionador;
	
	@GetMapping("/veiculos")
	public ResponseEntity<List<Veiculo>> pegarTodos(){
		List<Veiculo> todos = veiculoServico.pegarTodos();
		HttpStatus status = HttpStatus.CONFLICT;
		if(todos.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<List<Veiculo>>(status);
		}else {
			status = HttpStatus.FOUND;
			ResponseEntity<List<Veiculo>> resposta = new ResponseEntity<List<Veiculo>>(todos, status);
			return resposta;
		}
	}

	@GetMapping("/veiculos/{id}")
	public ResponseEntity<Veiculo> pegarUsuarioEspecifico(@PathVariable Long id){
		List<Veiculo> todasEmpresas = veiculoServico.pegarTodos();
		Veiculo select = selecionador.selecionar(todasEmpresas, id);
		if(select == null) {
			return new ResponseEntity<Veiculo>(HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<Veiculo>(select, HttpStatus.FOUND);
		}
	}
	
	@PutMapping("/atualizar/{id}")
	public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @RequestBody Empresa atualizador){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		List<Veiculo> usuarios = veiculoServico.pegarTodos();
		atualizador.setId(id);
		Veiculo usuario = selecionador.selecionar(usuarios, id);
		if (usuario != null) {
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}