package pl.edu.wat.wel.web.rest;

import pl.edu.wat.wel.SrsApp;
import pl.edu.wat.wel.domain.ClassDuration;
import pl.edu.wat.wel.repository.ClassDurationRepository;
import pl.edu.wat.wel.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static pl.edu.wat.wel.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ClassDurationResource} REST controller.
 */
@SpringBootTest(classes = SrsApp.class)
public class ClassDurationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ClassDurationRepository classDurationRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restClassDurationMockMvc;

    private ClassDuration classDuration;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClassDurationResource classDurationResource = new ClassDurationResource(classDurationRepository);
        this.restClassDurationMockMvc = MockMvcBuilders.standaloneSetup(classDurationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassDuration createEntity(EntityManager em) {
        ClassDuration classDuration = new ClassDuration()
            .name(DEFAULT_NAME);
        return classDuration;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassDuration createUpdatedEntity(EntityManager em) {
        ClassDuration classDuration = new ClassDuration()
            .name(UPDATED_NAME);
        return classDuration;
    }

    @BeforeEach
    public void initTest() {
        classDuration = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassDuration() throws Exception {
        int databaseSizeBeforeCreate = classDurationRepository.findAll().size();

        // Create the ClassDuration
        restClassDurationMockMvc.perform(post("/api/class-durations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classDuration)))
            .andExpect(status().isCreated());

        // Validate the ClassDuration in the database
        List<ClassDuration> classDurationList = classDurationRepository.findAll();
        assertThat(classDurationList).hasSize(databaseSizeBeforeCreate + 1);
        ClassDuration testClassDuration = classDurationList.get(classDurationList.size() - 1);
        assertThat(testClassDuration.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createClassDurationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classDurationRepository.findAll().size();

        // Create the ClassDuration with an existing ID
        classDuration.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassDurationMockMvc.perform(post("/api/class-durations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classDuration)))
            .andExpect(status().isBadRequest());

        // Validate the ClassDuration in the database
        List<ClassDuration> classDurationList = classDurationRepository.findAll();
        assertThat(classDurationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = classDurationRepository.findAll().size();
        // set the field null
        classDuration.setName(null);

        // Create the ClassDuration, which fails.

        restClassDurationMockMvc.perform(post("/api/class-durations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classDuration)))
            .andExpect(status().isBadRequest());

        List<ClassDuration> classDurationList = classDurationRepository.findAll();
        assertThat(classDurationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClassDurations() throws Exception {
        // Initialize the database
        classDurationRepository.saveAndFlush(classDuration);

        // Get all the classDurationList
        restClassDurationMockMvc.perform(get("/api/class-durations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classDuration.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getClassDuration() throws Exception {
        // Initialize the database
        classDurationRepository.saveAndFlush(classDuration);

        // Get the classDuration
        restClassDurationMockMvc.perform(get("/api/class-durations/{id}", classDuration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(classDuration.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingClassDuration() throws Exception {
        // Get the classDuration
        restClassDurationMockMvc.perform(get("/api/class-durations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassDuration() throws Exception {
        // Initialize the database
        classDurationRepository.saveAndFlush(classDuration);

        int databaseSizeBeforeUpdate = classDurationRepository.findAll().size();

        // Update the classDuration
        ClassDuration updatedClassDuration = classDurationRepository.findById(classDuration.getId()).get();
        // Disconnect from session so that the updates on updatedClassDuration are not directly saved in db
        em.detach(updatedClassDuration);
        updatedClassDuration
            .name(UPDATED_NAME);

        restClassDurationMockMvc.perform(put("/api/class-durations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedClassDuration)))
            .andExpect(status().isOk());

        // Validate the ClassDuration in the database
        List<ClassDuration> classDurationList = classDurationRepository.findAll();
        assertThat(classDurationList).hasSize(databaseSizeBeforeUpdate);
        ClassDuration testClassDuration = classDurationList.get(classDurationList.size() - 1);
        assertThat(testClassDuration.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingClassDuration() throws Exception {
        int databaseSizeBeforeUpdate = classDurationRepository.findAll().size();

        // Create the ClassDuration

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassDurationMockMvc.perform(put("/api/class-durations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classDuration)))
            .andExpect(status().isBadRequest());

        // Validate the ClassDuration in the database
        List<ClassDuration> classDurationList = classDurationRepository.findAll();
        assertThat(classDurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClassDuration() throws Exception {
        // Initialize the database
        classDurationRepository.saveAndFlush(classDuration);

        int databaseSizeBeforeDelete = classDurationRepository.findAll().size();

        // Delete the classDuration
        restClassDurationMockMvc.perform(delete("/api/class-durations/{id}", classDuration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassDuration> classDurationList = classDurationRepository.findAll();
        assertThat(classDurationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassDuration.class);
        ClassDuration classDuration1 = new ClassDuration();
        classDuration1.setId(1L);
        ClassDuration classDuration2 = new ClassDuration();
        classDuration2.setId(classDuration1.getId());
        assertThat(classDuration1).isEqualTo(classDuration2);
        classDuration2.setId(2L);
        assertThat(classDuration1).isNotEqualTo(classDuration2);
        classDuration1.setId(null);
        assertThat(classDuration1).isNotEqualTo(classDuration2);
    }
}
