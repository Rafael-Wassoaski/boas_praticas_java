package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ValidacaoTutorComLimiteDeAdocoesTest {
    @InjectMocks
    private ValidacaoTutorComLimiteDeAdocoes validacaoTutorComLimiteDeAdocoes;
    @Mock
    private AdocaoRepository adocaoRepository;
    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private Tutor tutor;
    @Mock
    private Pet pet;

    @Test
    public void shouldThrowExceptionWhenTutorHaveMoreThenFiveAdoptionsInProgress (){
        List<Adocao> adocaos = List.of(new Adocao(tutor, pet, "Motivo"),
                new Adocao(tutor, pet, "Motivo"),
                new Adocao(tutor, pet, "Motivo"),
                new Adocao(tutor, pet, "Motivo"),
                new Adocao(tutor, pet, "Motivo"));

        for(Adocao adocao : adocaos){
            adocao.marcarComoAprovada();
        }

        given(adocaoRepository.findAll()).willReturn(adocaos);
        given(tutorRepository.getReferenceById(tutor.getId())).willReturn(tutor);
        SolicitacaoAdocaoDto adocaoDto = new SolicitacaoAdocaoDto(pet.getId(), tutor.getId(), "Motivo");

        assertThrows(ValidacaoException.class, () -> validacaoTutorComLimiteDeAdocoes.validar(adocaoDto));
    }

    @Test
    public void shouldNotThrowExceptionWhenTutorHaveOneFiveAdoptionsInProgress (){
        List<Adocao> adocaos = List.of(new Adocao(tutor, pet, "Motivo"));

        for(Adocao adocao : adocaos){
            adocao.marcarComoAprovada();
        }

        given(adocaoRepository.findAll()).willReturn(adocaos);
        given(tutorRepository.getReferenceById(tutor.getId())).willReturn(tutor);
        SolicitacaoAdocaoDto adocaoDto = new SolicitacaoAdocaoDto(pet.getId(), tutor.getId(), "Motivo");

        assertDoesNotThrow(() -> validacaoTutorComLimiteDeAdocoes.validar(adocaoDto));
    }

}