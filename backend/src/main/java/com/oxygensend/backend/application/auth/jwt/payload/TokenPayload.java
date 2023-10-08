package com.oxygensend.backend.application.auth.jwt.payload;


import com.oxygensend.backend.domain.auth.TokenType;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@AllArgsConstructor
@Accessors(fluent = true, chain = true)
@EqualsAndHashCode
@ToString
abstract public class TokenPayload implements ClaimsPayload {
    protected final TokenType type;
    protected final Date iat;
    protected final Date exp;

}
