package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@Service
public class EmpresaServico {
	
	@Autowired
	private RepositorioEmpresa repositorio;
	
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
}
