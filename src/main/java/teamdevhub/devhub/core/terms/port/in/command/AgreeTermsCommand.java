package teamdevhub.devhub.core.terms.port.in.command;

import teamdevhub.devhub.core.terms.domain.TermsAgreementItem;

import java.util.List;

public record AgreeTermsCommand(String userGuid, List<TermsAgreementItem> termsAgreementItemList) {}