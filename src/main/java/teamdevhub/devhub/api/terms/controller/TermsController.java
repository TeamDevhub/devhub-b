package teamdevhub.devhub.api.terms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.api.terms.model.CreateTermsRequestDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.port.in.facade.TermsFacade;
import teamdevhub.devhub.core.terms.port.in.facade.model.TermsResponseDto;
import teamdevhub.devhub.shared.enums.SuccessCode;

import java.util.List;

@RestController
@RequestMapping("/terms")
@RequiredArgsConstructor
public class TermsController {

    private final TermsFacade termsFacade;

    @GetMapping()
    public ResponseEntity<DataListApiResponseDto<TermsResponseDto>> list(@RequestParam int page, @RequestParam int size) {
        List<TermsResponseDto> termsList = termsFacade.listTerms().stream()
                .map(TermsResponseDto::fromDomain)
                .toList();

        return ResponseEntity.ok(
                DataListApiResponseDto.successWithDataList(
                        SuccessCode.READ_SUCCESS,
                        termsList
                )
        );
    }

    @PostMapping()
    public ResponseEntity<DataApiResponseDto<Void>> register(@Valid @RequestBody CreateTermsRequestDto createTermsRequestDto) {
        termsFacade.registerTerms(createTermsRequestDto.toCreateTermsCommand());
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.CREATE_SUCCESS
                )
        );
    }
}
