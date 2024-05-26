package br.com.danielschiavo.livrariavirtual.aplicacao;

import br.com.danielschiavo.livrariavirtual.modelo.Ebook;

import java.util.List;

public interface EbookProtobufAdapter {

    List<br.com.danielschiavo.livrariavirtual.protos.Ebook> toProto(List<Ebook> ebook);

    br.com.danielschiavo.livrariavirtual.protos.Ebook toProto(Ebook ebook);

    Ebook fromProto(br.com.danielschiavo.livrariavirtual.protos.Ebook ebook);

    List<Ebook> fromProto(List<br.com.danielschiavo.livrariavirtual.protos.Ebook> ebooksProto);
}
