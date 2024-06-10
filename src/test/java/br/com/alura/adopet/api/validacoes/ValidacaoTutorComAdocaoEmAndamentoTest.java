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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ValidacaoTutorComAdocaoEmAndamentoTest {

    @InjectMocks
    private ValidacaoTutorComAdocaoEmAndamento validacaoTutorComAdocaoEmAndamento;
    @Mock
    private AdocaoRepository adocaoRepository;
    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private Tutor tutor;
    @Mock
    private Pet pet;


    @Test
    public void shouldNotThrowExceptionWhenTutorDoesntHaveAdoptionsInProgress (){
        given(adocaoRepository.findAll()).willReturn(Collections.emptyList());
        given(tutorRepository.getReferenceById(tutor.getId())).willReturn(tutor);
        SolicitacaoAdocaoDto adocaoDto = new SolicitacaoAdocaoDto(pet.getId(), tutor.getId(), "Motivo");

        assertDoesNotThrow(() -> validacaoTutorComAdocaoEmAndamento.validar(adocaoDto));
    }

    @Test
    public void shouldThrowExceptionWhenTutorHaveAdoptionsInProgress (){
        Adocao adocao = new Adocao(tutor, pet, "Motivo");
        given(adocaoRepository.findAll()).willReturn(List.of(adocao));
        given(tutorRepository.getReferenceById(tutor.getId())).willReturn(tutor);
        SolicitacaoAdocaoDto adocaoDto = new SolicitacaoAdocaoDto(pet.getId(), tutor.getId(), "Motivo");

        assertThrows(ValidacaoException.class, () -> validacaoTutorComAdocaoEmAndamento.validar(adocaoDto));
    }

}