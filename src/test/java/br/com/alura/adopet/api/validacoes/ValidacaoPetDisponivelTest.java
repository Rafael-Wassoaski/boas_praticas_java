package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ValidacaoPetDisponivelTest {

    @InjectMocks
    private ValidacaoPetDisponivel petDisponivel;

    @Mock
    private PetRepository petRepository;
    @Mock
    private Pet pet;


    @Test
    public void shouldValidateDisponiblePet(){
        SolicitacaoAdocaoDto solicitacaoAdocaoDto = new SolicitacaoAdocaoDto(7l, 32l, "Motivo qualquerr");
        Mockito.when(petRepository.getReferenceById(any())).thenReturn(pet);
        Mockito.when(pet.getAdotado()).thenReturn(false);

        assertDoesNotThrow(() -> petDisponivel.validar(solicitacaoAdocaoDto));
    }

    @Test
    public void shouldNotValidateIndisponiblePet(){
        SolicitacaoAdocaoDto solicitacaoAdocaoDto = new SolicitacaoAdocaoDto(7l, 32l, "Motivo qualquerr");
        Mockito.when(petRepository.getReferenceById(any())).thenReturn(pet);
        Mockito.when(pet.getAdotado()).thenReturn(true);

        assertThrows(ValidacaoException.class, () -> petDisponivel.validar(solicitacaoAdocaoDto));
    }

}