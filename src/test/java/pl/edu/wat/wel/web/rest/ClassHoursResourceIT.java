package pl.edu.wat.wel.web.rest;

import pl.edu.wat.wel.SrsApp;
import pl.edu.wat.wel.domain.ClassHours;
import pl.edu.wat.wel.repository.ClassHoursRepository;
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
 * Integration tests for the {@link ClassHoursResource} REST controller.
 */
@SpringBootTest(classes = SrsApp.class)
public class ClassHoursResourceIT {

    private static final String DEFAULT_START_TIME = "AAAAAAAAAA";
    private static final String UPDATED_START_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_END_TIME = "AAAAAAAAAA";
    private static final String UPDATED_END_TIME = "BBBBBBBBBB";

    @Autowired
    private ClassHoursRepository classHoursRepository;

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

    private MockMvc restClassHoursMockMvc;

    private ClassHours classHours;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClassHoursResource classHoursResource = new ClassHoursResource(classHoursRepository);
        this.restClassHoursMockMvc = MockMvcBuilders.standaloneSetup(classHoursResource)
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
    public static ClassHours createEntity(EntityManager em) {
        ClassHours classHours = new ClassHours()
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME);
        return classHours;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassHours createUpdatedEntity(EntityManager em) {
        ClassHours classHours = new ClassHours()
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME);
        return classHours;
    }

    @BeforeEach
    public void initTest() {
        classHours = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassHours() throws Exception {
        int databaseSizeBeforeCreate = classHoursRepository.findAll().size();

        // Create the ClassHours
        restClassHoursMockMvc.perform(post("/api/class-hours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classHours)))
            .andExpect(status().isCreated());

        // Validate the ClassHours in the database
        List<ClassHours> classHoursList = classHoursRepository.findAll();
        assertThat(classHoursList).hasSize(databaseSizeBeforeCreate + 1);
        ClassHours testClassHours = classHoursList.get(classHoursList.size() - 1);
        assertThat(testClassHours.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testClassHours.getEndTime()).isEqualTo(DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    public void createClassHoursWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classHoursRepository.findAll().size();

        // Create the ClassHours with an existing ID
        classHours.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassHoursMockMvc.perform(post("/api/class-hours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classHours)))
            .andExpect(status().isBadRequest());

        // Validate the ClassHours in the database
        List<ClassHours> classHoursList = classHoursRepository.findAll();
        assertThat(classHoursList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = classHoursRepository.findAll().size();
        // set the field null
        classHours.setStartTime(null);

        // Create the ClassHours, which fails.

        restClassHoursMockMvc.perform(post("/api/class-hours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classHours)))
            .andExpect(status().isBadRequest());

        List<ClassHours> classHoursList = classHoursRepository.findAll();
        assertThat(classHoursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = classHoursRepository.findAll().size();
        // set the field null
        classHours.setEndTime(null);

        // Create the ClassHours, which fails.

        restClassHoursMockMvc.perform(post("/api/class-hours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classHours)))
            .andExpect(status().isBadRequest());

        List<ClassHours> classHoursList = classHoursRepository.findAll();
        assertThat(classHoursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClassHours() throws Exception {
        // Initialize the database
        classHoursRepository.saveAndFlush(classHours);

        // Get all the classHoursList
        restClassHoursMockMvc.perform(get("/api/class-hours?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classHours.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME)));
    }
    
    @Test
    @Transactional
    public void getClassHours() throws Exception {
        // Initialize the database
        classHoursRepository.saveAndFlush(classHours);

        // Get the classHours
        restClassHoursMockMvc.perform(get("/api/class-hours/{id}", classHours.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(classHours.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME));
    }

    @Test
    @Transactional
    public void getNonExistingClassHours() throws Exception {
        // Get the classHours
        restClassHoursMockMvc.perform(get("/api/class-hours/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassHours() throws Exception {
        // Initialize the database
        classHoursRepository.saveAndFlush(classHours);

        int databaseSizeBeforeUpdate = classHoursRepository.findAll().size();

        // Update the classHours
        ClassHours updatedClassHours = classHoursRepository.findById(classHours.getId()).get();
        // Disconnect from session so that the updates on updatedClassHours are not directly saved in db
        em.detach(updatedClassHours);
        updatedClassHours
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME);

        restClassHoursMockMvc.perform(put("/api/class-hours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedClassHours)))
            .andExpect(status().isOk());

        // Validate the ClassHours in the database
        List<ClassHours> classHoursList = classHoursRepository.findAll();
        assertThat(classHoursList).hasSize(databaseSizeBeforeUpdate);
        ClassHours testClassHours = classHoursList.get(classHoursList.size() - 1);
        assertThat(testClassHours.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testClassHours.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingClassHours() throws Exception {
        int databaseSizeBeforeUpdate = classHoursRepository.findAll().size();

        // Create the ClassHours

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassHoursMockMvc.perform(put("/api/class-hours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classHours)))
            .andExpect(status().isBadRequest());

        // Validate the ClassHours in the database
        List<ClassHours> classHoursList = classHoursRepository.findAll();
        assertThat(classHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClassHours() throws Exception {
        // Initialize the database
        classHoursRepository.saveAndFlush(classHours);

        int databaseSizeBeforeDelete = classHoursRepository.findAll().size();

        // Delete the classHours
        restClassHoursMockMvc.perform(delete("/api/class-hours/{id}", classHours.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassHours> classHoursList = classHoursRepository.findAll();
        assertThat(classHoursList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassHours.class);
        ClassHours classHours1 = new ClassHours();
        classHours1.setId(1L);
        ClassHours classHours2 = new ClassHours();
        classHours2.setId(classHours1.getId());
        assertThat(classHours1).isEqualTo(classHours2);
        classHours2.setId(2L);
        assertThat(classHours1).isNotEqualTo(classHours2);
        classHours1.setId(null);
        assertThat(classHours1).isNotEqualTo(classHours2);
    }
}
