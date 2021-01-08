package org.cs2021.eris.api;

import lombok.NonNull;
import lombok.Value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

@Value
@JsonDeserialize
public final class GreetingMessage {
    @NonNull
    public String message;

    @JsonCreator
    public GreetingMessage(@NonNull String message) {
        this.message = Preconditions.checkNotNull(message, "message field MUST not be null");
    }
}