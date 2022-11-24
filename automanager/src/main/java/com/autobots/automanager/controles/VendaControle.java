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

import com.autobots.automanager.componentes.VendaSelecionador;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.servicos.MercadoriaServico;
import com.autobots.automanager.servicos.ServicoServico;
import com.autobots.automanager.servicos.UsuarioServico;
import com.autobots.automanager.servicos.VeiculoServico;
import com.autobots.automanager.servicos.VendaServico;

@RestController
@RequestMapping("/venda")
public class VendaControle {

	@Autowired
	private VendaServico servico;
	
	@Autowired
	private VendaSelecionador selecionador;
	
	@Autowired
	private UsuarioServico servicoUsuario;
	
	@Autowired
	private VeiculoServico servicoVeiculo;
	
	@Autowired
	private MercadoriaServico servicoMercadoria;
	
	@Autowired
	private ServicoServico servicoServico;
	
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
	
	@PostMapping("/cadastro")
	public void cadastroVenda(@RequestBody Venda vendas){
		/*
		 * Cliente / Funcionario , Mercadoria , Servicos , Veiculo
		 * */
		Usuario clienteSelecionado = servicoUsuario.pegarPeloId(vendas.getCliente().getId());
		Usuario funcionarioSelecionado = servicoUsuario.pegarPeloId(vendas.getFuncionario().getId());
		Veiculo veiculoSelecionador = servicoVeiculo.pegarPeloId(vendas.getVeiculo().getId());
		for(Mercadoria bodyMercadoria: vendas.getMercadorias()) {
			Mercadoria findMercadoria = servicoMercadoria.pegarPeloId(bodyMercadoria.getId());
			vendas.getMercadorias().add(findMercadoria);
		}
		for(Servico bodyServico : vendas.getServicos() ) {
			Servico findServico = servicoServico.pegarPeloId(bodyServico.getId());
			vendas.getServicos().add(findServico);
		}
		vendas.setCliente(clienteSelecionado);
		vendas.setFuncionario(funcionarioSelecionado);
		vendas.setVeiculo(veiculoSelecionador);
		servico.salvar(vendas);
	}
}
