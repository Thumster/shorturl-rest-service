package com.example.shorturlrestservice.repository;

import com.example.shorturlrestservice.model.UrlMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlMapperRepository extends JpaRepository<UrlMapper, String> {
}
