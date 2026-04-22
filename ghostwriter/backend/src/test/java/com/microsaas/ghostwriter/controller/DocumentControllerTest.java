package com.microsaas.ghostwriter.controller;

import com.microsaas.ghostwriter.model.Document;
import com.microsaas.ghostwriter.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DocumentControllerTest {

    @Mock
    private DocumentService service;

    @InjectMocks
    private DocumentController controller;

    @Test
    void testGetAll() {
        when(service.getAll("tenant-1")).thenReturn(List.of(new Document()));
        ResponseEntity<List<Document>> response = controller.getAll("tenant-1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testCreate() {
        Document doc = new Document();
        when(service.create(doc, "tenant-1")).thenReturn(doc);
        ResponseEntity<Document> response = controller.create(doc, "tenant-1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
