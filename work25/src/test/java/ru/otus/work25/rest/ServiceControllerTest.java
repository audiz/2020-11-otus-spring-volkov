package ru.otus.work25.rest;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTypeExcludeFilter;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// From @DataMongoTest
@ExtendWith({SpringExtension.class})
@OverrideAutoConfiguration(
        enabled = false
)
@TypeExcludeFilters({DataMongoTypeExcludeFilter.class})
@AutoConfigureCache
@AutoConfigureDataMongo
@ImportAutoConfiguration
// From @DataMongoTest

@EnableConfigurationProperties
@ComponentScan({"ru.otus.work25.service", "ru.otus.work25.domain", "ru.otus.work25.changelog"})
@RunWith(SpringRunner.class)
@WebMvcTest(ServiceController.class)
public class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAuthinfo() throws Exception {
        mockMvc.perform(get("/api/authinfo")).andExpect(status().isOk());
    }

    @Test
    public void testSummary() throws Exception {
        mockMvc.perform(get("/api/summary")).andExpect(status().isOk());
    }
}
