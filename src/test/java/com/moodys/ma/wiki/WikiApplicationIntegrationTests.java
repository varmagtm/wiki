package com.moodys.ma.wiki;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moodys.ma.wiki.api.model.DocumentDTO;
import com.moodys.ma.wiki.api.model.ErrorResponseDTO;
import com.moodys.ma.wiki.api.model.RevisionResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class WikiApplicationIntegrationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        // Set up your in-memory H2 database and initialize test data
        // You can use documentRepository to save test data before each test if needed
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetAllTitles() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/documents"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<String> list = objectMapper.readValue(content, List.class);
        assertEquals(4, list.size());
    }

    @Test
    void testDocRevisions() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/documents/document1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<RevisionResponseDTO> list = objectMapper.readValue(content, List.class);
        assertEquals(2, list.size());


        // No revisions should be found for the below title
        result = mockMvc.perform(MockMvcRequestBuilders.get("/documents/title999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        ErrorResponseDTO error = objectMapper.readValue(result.getResponse().getContentAsString(),
                ErrorResponseDTO.class);
        assertEquals(404, error.getStatus());

        result = mockMvc.perform(MockMvcRequestBuilders.get("/documents/document3"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        content = result.getResponse().getContentAsString();
        list = objectMapper.readValue(content, List.class);
        assertEquals(1, list.size());
    }

    @Test
    void testGetLatestDocumentRevision() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/documents/document1/latest"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        DocumentDTO documentDTO = objectMapper.readValue(content, DocumentDTO.class);
        assertEquals(1, documentDTO.getDocumentId());
        assertEquals(2, documentDTO.getRevisionId());
        assertEquals("Revision 2 for Document 1", documentDTO.getContent());

        // Update the document
        mockMvc.perform(MockMvcRequestBuilders.put("/documents/document1").content("new content"))
                .andExpect(status().isCreated())
                .andDo(print());

        result = mockMvc.perform(MockMvcRequestBuilders.get("/documents/document1/latest"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        content = result.getResponse().getContentAsString();
        documentDTO = objectMapper.readValue(content, DocumentDTO.class);
        assertEquals(1, documentDTO.getDocumentId());
        assertEquals("new content", documentDTO.getContent());
    }

    @Test
    void testGetDocumentRevisionAtTimestamp() throws Exception {
        // Create a revision for document 1
        String firstRevContent = "new content at " + LocalDateTime.now();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/documents/document4")
                        .content(firstRevContent))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
        Long firstRevisionId = Long.valueOf(result.getResponse().getContentAsString());
        LocalDateTime timestampAfterFirstRev = LocalDateTime.now();
        result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/documents/document4/" + timestampAfterFirstRev))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        DocumentDTO documentDTO = objectMapper.readValue(content, DocumentDTO.class);
        assertEquals(4, documentDTO.getDocumentId());
        assertEquals(firstRevisionId, documentDTO.getRevisionId());
        assertEquals(firstRevContent, documentDTO.getContent());

        String secondRevContent = "new content at " + LocalDateTime.now();
        result = mockMvc.perform(MockMvcRequestBuilders.put("/documents/document4")
                        .content(secondRevContent))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
        Long secondRevisionId = Long.valueOf(result.getResponse().getContentAsString());
        LocalDateTime timestampAfterSecondRev = LocalDateTime.now();
        result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/documents/document4/" + timestampAfterSecondRev))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        content = result.getResponse().getContentAsString();
        documentDTO = objectMapper.readValue(content, DocumentDTO.class);
        assertEquals(4, documentDTO.getDocumentId());
        assertEquals(secondRevisionId, documentDTO.getRevisionId());
        assertEquals(secondRevContent, documentDTO.getContent());
    }
}
