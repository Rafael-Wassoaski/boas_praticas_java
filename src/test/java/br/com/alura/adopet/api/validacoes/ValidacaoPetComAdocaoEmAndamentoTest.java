package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ValidacaoPetComAdocaoEmAndamentoTest {
    @InjectMocks
    private ValidacaoPetComAdocaoEmAndamento petComAdocaoEmAndamento;

    @Mock
    private AdocaoRepository adocaoRepository;
    @Mock
    private Pet pet;

    @Test
    public void shouldNotThrowExceptionWhenPetIsNotInAdoptionProcess(){
        given(adocaoRepository.existsByPetIdAndStatus(any(), any())).willReturn(false);
        given(pet.getId()).willReturn(1L);
        SolicitacaoAdocaoDto solicitacaoAdocaoDto = new SolicitacaoAdocaoDto(pet.getId(), 1L, "motivo");

        assertDoesNotThrow(() -> petComAdocaoEmAndamento.validar(solicitacaoAdocaoDto));
    }

    @Test
    public void shouldThrowExceptionWhenPetIsInAdoptionProcess(){
        given(adocaoRepository.existsByPetIdAndStatus(any(), any())).willReturn(true);
        given(pet.getId()).willReturn(1L);
        SolicitacaoAdocaoDto solicitacaoAdocaoDto = new SolicitacaoAdocaoDto(pet.getId(), 1L, "motivo");

        assertThrows(ValidacaoException.class, () -> petComAdocaoEmAndamento.validar(solicitacaoAdocaoDto));
    }

}