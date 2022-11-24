package com.autobots.automanager.servicos;

import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioVenda;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendaServico {

  @Autowired
  private RepositorioVenda repositorio;

  public List<Venda> pegarTodos() {
    List<Venda> pegarTodas = repositorio.findAll();
    return pegarTodas;
  }

  public void salvar(Venda cadastro) {
    repositorio.save(cadastro);
  }

  public Venda pegarPeloId(Long id) {
    Venda achar = repositorio.getById(id);
    return achar;
  }

  public Venda update(Venda obj) {
    Venda newObj = pegarPeloId(obj.getId());
    updateData(newObj, obj);
    return repositorio.save(newObj);
  }

  private void updateData(Venda newObj, Venda obj) {
    newObj.setCadastro(new Date());
    newObj.setFuncionario(obj.getFuncionario());
    newObj.setIdentificacao(obj.getIdentificacao());
    newObj.setVeiculo(obj.getVeiculo());
    newObj.setCliente(obj.getCliente());
  }
}
