package teamdevhub.devhub.core.terms.port.in.command;

import lombok.Builder;
import teamdevhub.devhub.core.terms.domain.TermsAgreementItem;

import java.util.List;

@Builder
public record AgreeTermsCommand(String userGuid, List<TermsAgreementItem> termsAgreementItemList) {}