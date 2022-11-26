package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.componentes.EmpresaSelecionadora;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@Service
public class EmpresaServico {
	
	@Autowired
	private RepositorioEmpresa repositorio;
	
	@Autowired
	private EmpresaSelecionadora select;
	
	@Autowired
	private MercadoriaServico servicoMercadoria;
	@Autowired
	private VendaServico servicoVenda;
	
	public List<Empresa> pegarTodas(){
		List<Empresa> pegarTodas = repositorio.findAll();
		return pegarTodas;
	}
	
	public void Salvar(Empresa salvar) {
		repositorio.save(salvar);
	}
	
	public Empresa pegarPeloId(Long id) {
		Empresa achar = repositorio.getById(id);
		return achar;
	}
	
	public Empresa update(Empresa obj) {
		Empresa newObj = pegarPeloId(obj.getId());
		updateData(newObj, obj);
		return repositorio.save(newObj);
	}
	
	private void updateData(Empresa newObj, Empresa obj) {
		newObj.setEndereco(null);
		newObj.setNomeFantasia(null);
		
	}
	
	public void deletarMercadoria(Long idEmpresa, Long idMercadoria) {
		List<Empresa> todos = pegarTodas();
		Empresa selecionado = select.selecionar(todos, idMercadoria);
		Mercadoria mercadoria = servicoMercadoria.pegarPeloId(idMercadoria); 
		if(selecionado.getId() == idEmpresa) {
			selecionado.getMercadorias().remove(mercadoria);
		}
	}
	public void deletarVendas(Long idEmpresa, Long idMercadoria) {
		List<Empresa> todos = pegarTodas();
		Empresa selecionado = select.selecionar(todos, idMercadoria);
		Venda venda = servicoVenda.pegarPeloId(idMercadoria); 
		if(selecionado.getId() == idEmpresa) {
			selecionado.getVendas().remove(venda);
		}
	}
}
