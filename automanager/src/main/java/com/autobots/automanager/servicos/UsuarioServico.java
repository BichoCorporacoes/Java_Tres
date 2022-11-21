package com.autobots.automanager.servicos;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioDocumento;
import com.autobots.automanager.repositorios.RepositorioEmail;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class UsuarioServico {

	@Autowired
	private RepositorioUsuario repositorio;
	
	@Autowired
	private RepositorioEmail repositorioEmail;
	
	@Autowired
	private RepositorioDocumento repositorioDocumento;
	
	public List<Usuario> pegarTodos() {
		List<Usuario> pegarTodos = repositorio.findAll();
		return pegarTodos;
	}
	
	public Usuario pegarPeloId(Long id) {
		Usuario achar = repositorio.getById(id);
		return achar;
	}
	
	public void salvarUsuario(Usuario usuario) {
		repositorio.save(usuario);
	}
	
	public Usuario update(Usuario obj) {
		Usuario newObj = pegarPeloId(obj.getId());
		updateData(newObj, obj);
		for(Documento docs: obj.getDocumentos()) {
			docs.setId(docs.getId());
			updateDocumento(docs);
		}
		for(Email emails: obj.getEmails()) {
			emails.setId(emails.getId());
			updateDocumento(emails);
		}
		return repositorio.save(newObj);
	}
	
	private void updateData(Usuario newObj, Usuario obj) {
		newObj.setNome(obj.getNome());
		newObj.setNomeSocial(obj.getNomeSocial());
	}
	
	// Emails
	
	public List<Email> pegarEmails(){
		List<Email> emails = repositorioEmail.findAll();
		return emails;
	}
	
	public Email pegarPeloIdEmail(Long id) {
		Email achar = repositorioEmail.getById(id);
		return achar;
	}
	
	public void salvarEmail(Email email) {
		repositorioEmail.save(email);
	}
	
	
	public Email updateDocumento(Email obj) {
		Email newObj = pegarPeloIdEmail(obj.getId());
		updateDataEmail(newObj, obj);
		return repositorioEmail.save(newObj);
	}
	
	private void updateDataEmail(Email newObj, Email obj) {
		newObj.setEndereco(obj.getEndereco());
	}
	
	// Documentos
	

	
	public List<Documento> pegarDocumentos(){
		List<Documento> documentos = repositorioDocumento.findAll();
		return documentos;
	}
	
	public Documento pegarPeloIdDoc(Long id) {
		Documento achar = repositorioDocumento.getById(id);
		return achar;
	}
	
	public void salvarDocumento(Documento documento) {
		repositorioDocumento.save(documento);
	}
	
	
	public Documento updateDocumento(Documento obj) {
		Documento newObj = pegarPeloIdDoc(obj.getId());
		updateDataDocumento(newObj, obj);
		return repositorioDocumento.save(newObj);
	}
	
	private void updateDataDocumento(Documento newObj, Documento obj) {
		newObj.setNumero(obj.getNumero());
		newObj.setDataEmissao(new Date());
	}
	
	
	
}
