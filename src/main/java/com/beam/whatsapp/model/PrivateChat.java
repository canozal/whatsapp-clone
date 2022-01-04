package com.beam.whatsapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
//@AllArgsConstructor
//@NoArgsConstructor
@Accessors(chain = true) // If true, generated setters return this instead of void.
@SuperBuilder
@Document(collection = "PrivateChat")
@TypeAlias("PrivateChat")
public class PrivateChat extends Chat {


    public PrivateChat() {
        setType(Type.PRIVATE);
    }

}
