package org.cs2021.eris.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * The state for the {@link ErisAggregate} entity.
 */
@SuppressWarnings("serial")
@Value
@JsonDeserialize
public final class ErisState implements CompressedJsonable {
    public static final ErisState INITIAL = new ErisState("Hello", LocalDateTime.now().toString());
    public final String message;
    public final String timestamp;

    @JsonCreator
    ErisState(String message, String timestamp) {
        this.message = Preconditions.checkNotNull(message, "message");
        this.timestamp = Preconditions.checkNotNull(timestamp, "timestamp");
    }

    public ErisState withMessage(String message) {
        return new ErisState(message, LocalDateTime.now().toString());
    }


}
