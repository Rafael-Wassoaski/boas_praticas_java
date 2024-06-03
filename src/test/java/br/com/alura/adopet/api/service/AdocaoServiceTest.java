package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoSolicitacaoAdocao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdocaoServiceTest {


    @InjectMocks
    private AdocaoService service;

    @Mock
    private AdocaoRepository repository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private EmailService emailService;

    @Spy
    private List<ValidacaoSolicitacaoAdocao> validacoes = new ArrayList<>();
    @Mock
    private ValidacaoSolicitacaoAdocao validacaoSolicitacaoAdocao1;

    @Mock
    private ValidacaoSolicitacaoAdocao validacaoSolicitacaoAdocao2;

    @Mock
    private Pet pet;

    @Mock
    private Tutor tutor;

    @Mock
    private Abrigo abrigo;

    private SolicitacaoAdocaoDto dto;

    @Captor
    private ArgumentCaptor<Adocao> adocaoCaptor;

    @Test
    public void shouldSaveAdocaoWhenRequested(){
        SolicitacaoAdocaoDto solicitacaoAdocaoDto = new SolicitacaoAdocaoDto(10l, 20l, "MotivoQualquer");
        given(petRepository.getReferenceById(solicitacaoAdocaoDto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(solicitacaoAdocaoDto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        service.solicitar(solicitacaoAdocaoDto);

        then(repository).should().save(adocaoCaptor.capture());
        Adocao adocaoSalva = adocaoCaptor.getValue();
        assertEquals(pet.getId(), adocaoSalva.getPet().getId());
        assertEquals(tutor.getId(), adocaoSalva.getTutor().getId());
        assertEquals(solicitacaoAdocaoDto.motivo(), adocaoSalva.getMotivo());
    }

    @Test
    public void shouldIterateOverValidatorListWhenRequestAdocao(){
        SolicitacaoAdocaoDto solicitacaoAdocaoDto = new SolicitacaoAdocaoDto(10l, 20l, "MotivoQualquer");
        given(petRepository.getReferenceById(solicitacaoAdocaoDto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(solicitacaoAdocaoDto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);
        validacoes.add(validacaoSolicitacaoAdocao1);
        validacoes.add(validacaoSolicitacaoAdocao2);

        service.solicitar(solicitacaoAdocaoDto);

        then(validacaoSolicitacaoAdocao1).should().validar(solicitacaoAdocaoDto);
        then(validacaoSolicitacaoAdocao2).should().validar(solicitacaoAdocaoDto);
    }

}