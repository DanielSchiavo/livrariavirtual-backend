package br.com.danielschiavo.livrariavirtual.infra;

import br.com.danielschiavo.livrariavirtual.aplicacao.GeradorUUID;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GeradorUUIDImpl implements GeradorUUID {

    @Override
    public UUID gerar() {
        return UUID.randomUUID();
    }

}
