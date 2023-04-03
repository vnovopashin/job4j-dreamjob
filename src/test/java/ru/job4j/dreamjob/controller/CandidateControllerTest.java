package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.service.CandidateService;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Класс тестирует методы контроллера CandidateController, тестируемые методы имеющие внешние
 * зависимости, заменяются mock объектами из библиотеки Mockito,
 * что позволяет настроить внешнюю зависимость нужным образом.
 *
 * @author Vasiliy Novopashin
 * @version 1.0
 * {@code @date} 02.04.2023
 */

class CandidateControllerTest {
    private CandidateService candidateService;
    private CandidateController candidateController;
    private MultipartFile testFile;

    @BeforeEach
    public void initService() {
        candidateService = mock(CandidateService.class);
        candidateController = new CandidateController(candidateService);
        testFile = new MockMultipartFile("testFile.img", new byte[]{1, 2, 3});
    }

    @Test
    public void whenRequestCandidateListPageThenGetPageWithCandidates() {
        var candidate1 = new Candidate(1, "name1", "description1", now(), 1);
        var candidate2 = new Candidate(2, "name2", "description2", now(), 2);
        var expectedCandidates = List.of(candidate1, candidate2);
        when(candidateService.findAll()).thenReturn(expectedCandidates);

        var model = new ConcurrentModel();
        var view = candidateController.getAll(model);
        var actualCandidates = model.getAttribute("candidates");

        assertThat(view).isEqualTo("candidates/list");
        assertThat(actualCandidates).isEqualTo(expectedCandidates);
    }

    @Test
    public void whenPostCandidateWithFileThenSameDataAndRedirectToCandidatesPage() throws Exception {
        var candidate = new Candidate(1, "name1", "description1", now(), 1);
        var fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        var candidateArgumentCaptor = ArgumentCaptor.forClass(Candidate.class);
        var fileDtoArgumentCaptor = ArgumentCaptor.forClass(FileDto.class);
        when(candidateService.save(candidateArgumentCaptor.capture(), fileDtoArgumentCaptor.capture())).thenReturn(candidate);

        var model = new ConcurrentModel();
        var view = candidateController.create(candidate, testFile, model);
        var actualCandidate = candidateArgumentCaptor.getValue();
        var actualFileDto = fileDtoArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/candidates");
        assertThat(actualCandidate).isEqualTo(candidate);
        assertThat(fileDto).usingRecursiveComparison().isEqualTo(actualFileDto);
    }

    @Test
    public void whenSomeExceptionThrownThenGetErrorPageWithMessage() {
        var expectedException = new RuntimeException("Filed to write file");
        when(candidateService.save(any(), any())).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = candidateController.create(new Candidate(), testFile, model);
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenUpdateCandidateWithFileThenSameDataAndRedirectToCandidatesPage() throws Exception {
        var candidate = new Candidate(1, "name1", "description1", now(), 1);
        var fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        var candidateArgumentCaptor = ArgumentCaptor.forClass(Candidate.class);
        var fileDtoArgumentCaptor = ArgumentCaptor.forClass(FileDto.class);
        when(candidateService.update(candidateArgumentCaptor.capture(), fileDtoArgumentCaptor.capture())).thenReturn(true);

        var model = new ConcurrentModel();
        var view = candidateController.update(candidate, testFile, model);
        var actualCandidate = candidateArgumentCaptor.getValue();
        var actualFileDto = fileDtoArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/candidates");
        assertThat(actualCandidate).isEqualTo(candidate);
        assertThat(fileDto).usingRecursiveComparison().isEqualTo(actualFileDto);
    }

    @Test
    public void whenUpdateCandidatePageThenGetPageWithError() throws Exception {
        var candidate = new Candidate(1, "name1", "description1", now(), 1);
        var fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        when(candidateService.update(candidate, fileDto)).thenReturn(false);

        var model = new ConcurrentModel();
        var view = candidateController.update(candidate, testFile, model);
        var actualCandidate = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualCandidate).isEqualTo("Резюме с указанным идентификатором не найдено");
    }

    @Test
    public void whenUpdateCandidatePageThenExceptionGetPageWithError() {
        var expectedException = new RuntimeException("Failed to update file");
        when(candidateService.update(any(), any())).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = candidateController.update(new Candidate(), testFile, model);
        var actualCandidate = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualCandidate).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenFindByIdThenGetPageWithCandidateOne() {
        var candidate = new Candidate(1, "name1", "description1", now(), 1);

        when(candidateService.findById(any(Integer.class))).thenReturn(Optional.of(candidate));

        var model = new ConcurrentModel();
        var view = candidateController.getById(model, any(Integer.class));
        var actualCandidate = model.getAttribute("candidate");

        assertThat(view).isEqualTo("candidates/one");
        assertThat(actualCandidate).isEqualTo(candidate);
    }

    @Test
    public void whenFindByIdThenGetPageWithError() {
        when(candidateService.findById(any(Integer.class))).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = candidateController.getById(model, any(Integer.class));
        var actualCandidate = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualCandidate).isEqualTo("Резюме с указанным идентификатором не найдено");
    }

    @Test
    public void whenRequestVacancyPageThenGetPageWithVacancies() {
        when(candidateService.deleteById(any(Integer.class))).thenReturn(true);

        var model = new ConcurrentModel();
        var view = candidateController.delete(model, any(Integer.class));
        var actualCandidate = model.getAttribute("candidate");

        assertThat(view).isEqualTo("redirect:/candidates");
        assertThat(actualCandidate).isNull();
    }

    @Test
    public void whenRequestCreateThenGetPageCreate() {
        assertThat(candidateController.getCreationPage()).isEqualTo("candidates/create");
    }
}
