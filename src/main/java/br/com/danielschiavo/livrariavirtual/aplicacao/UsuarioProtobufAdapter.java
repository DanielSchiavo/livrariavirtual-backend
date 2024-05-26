package br.com.danielschiavo.livrariavirtual.aplicacao;

import br.com.danielschiavo.livrariavirtual.modelo.Usuario;

public interface UsuarioProtobufAdapter extends EbookProtobufAdapter {

    br.com.danielschiavo.livrariavirtual.protos.Usuario toProto(Usuario usuario);

    Usuario fromProto(br.com.danielschiavo.livrariavirtual.protos.Usuario usuarioProto);

}
