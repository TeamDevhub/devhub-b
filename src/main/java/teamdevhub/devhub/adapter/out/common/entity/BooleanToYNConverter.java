package teamdevhub.devhub.adapter.out.common.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import teamdevhub.devhub.adapter.out.common.exception.DataAccessException;
import teamdevhub.devhub.common.enums.ErrorCode;

@Converter
public class BooleanToYNConverter implements AttributeConverter<Boolean, String> {

    private static final String YES = "Y";
    private static final String NO  = "N";

    @Override
    public String convertToDatabaseColumn(Boolean domainField) {
        if (domainField == null) {
            return NO;
        }

        if (domainField) {
            return YES;
        }

        return NO;
    }

    @Override
    public Boolean convertToEntityAttribute(String databaseColumnValue) {
        if (databaseColumnValue == null) {
            return false;
        }

        if (YES.equals(databaseColumnValue)) {
            return true;
        }

        if (NO.equals(databaseColumnValue)) {
            return false;
        }
        throw DataAccessException.of(ErrorCode.BOOLEAN_CONVERT_FAIL);
    }
}
