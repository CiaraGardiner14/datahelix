package com.scottlogic.deg.schemas.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ProfileDeserialiser {
    public BaseProfile deserialise(String json, String desiredSchemaType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        BaseProfile baseProfile = mapper.readerFor(BaseProfile.class).readValue(json);

        if (!baseProfile.schemaVersion.equals(desiredSchemaType))
            throw new IOException();

        return baseProfile;
    }
}
