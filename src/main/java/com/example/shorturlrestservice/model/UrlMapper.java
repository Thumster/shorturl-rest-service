package com.example.shorturlrestservice.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UrlMapper {

    @Id
    private String id;
    @Column
    private String originalUrl;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdDateTime;
}
