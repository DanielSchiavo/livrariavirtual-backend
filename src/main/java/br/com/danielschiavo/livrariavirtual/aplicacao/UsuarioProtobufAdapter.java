package br.com.danielschiavo.livrariavirtual.aplicacao;

public interface UsuarioProtobufAdapter {

    br.com.danielschiavo.livrariavirtual.protos.Usuario toProto(br.com.danielschiavo.livrariavirtual.modelo.Usuario usuario);

    br.com.danielschiavo.livrariavirtual.modelo.Usuario fromProto(br.com.danielschiavo.livrariavirtual.protos.Usuario usuarioProto);

}
