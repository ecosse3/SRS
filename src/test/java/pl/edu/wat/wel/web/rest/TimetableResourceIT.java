package pl.edu.wat.wel.web.rest;

import pl.edu.wat.wel.SrsApp;
import pl.edu.wat.wel.domain.Timetable;
import pl.edu.wat.wel.repository.TimetableRepository;
import pl.edu.wat.wel.service.TimetableService;
import pl.edu.wat.wel.web.rest.errors.ExceptionTranslator;
import pl.edu.wat.wel.service.dto.TimetableCriteria;
import pl.edu.wat.wel.service.TimetableQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static pl.edu.wat.wel.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TimetableResource} REST controller.
 */
@SpringBootTest(classes = SrsApp.class)
public class TimetableResourceIT {

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CLASS_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CLASS_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CLASS_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private TimetableService timetableService;

    @Autowired
    private TimetableQueryService timetableQueryService;

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

    private MockMvc restTimetableMockMvc;

    private Timetable timetable;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TimetableResource timetableResource = new TimetableResource(timetableService, timetableQueryService);
        this.restTimetableMockMvc = MockMvcBuilders.standaloneSetup(timetableResource)
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
    public static Timetable createEntity(EntityManager em) {
        Timetable timetable = new Timetable()
            .subject(DEFAULT_SUBJECT)
            .classDate(DEFAULT_CLASS_DATE);
        return timetable;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Timetable createUpdatedEntity(EntityManager em) {
        Timetable timetable = new Timetable()
            .subject(UPDATED_SUBJECT)
            .classDate(UPDATED_CLASS_DATE);
        return timetable;
    }

    @BeforeEach
    public void initTest() {
        timetable = createEntity(em);
    }

    @Test
    @Transactional
    public void createTimetable() throws Exception {
        int databaseSizeBeforeCreate = timetableRepository.findAll().size();

        // Create the Timetable
        restTimetableMockMvc.perform(post("/api/timetables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timetable)))
            .andExpect(status().isCreated());

        // Validate the Timetable in the database
        List<Timetable> timetableList = timetableRepository.findAll();
        assertThat(timetableList).hasSize(databaseSizeBeforeCreate + 1);
        Timetable testTimetable = timetableList.get(timetableList.size() - 1);
        assertThat(testTimetable.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testTimetable.getClassDate()).isEqualTo(DEFAULT_CLASS_DATE);
    }

    @Test
    @Transactional
    public void createTimetableWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = timetableRepository.findAll().size();

        // Create the Timetable with an existing ID
        timetable.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimetableMockMvc.perform(post("/api/timetables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timetable)))
            .andExpect(status().isBadRequest());

        // Validate the Timetable in the database
        List<Timetable> timetableList = timetableRepository.findAll();
        assertThat(timetableList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = timetableRepository.findAll().size();
        // set the field null
        timetable.setSubject(null);

        // Create the Timetable, which fails.

        restTimetableMockMvc.perform(post("/api/timetables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timetable)))
            .andExpect(status().isBadRequest());

        List<Timetable> timetableList = timetableRepository.findAll();
        assertThat(timetableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClassDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = timetableRepository.findAll().size();
        // set the field null
        timetable.setClassDate(null);

        // Create the Timetable, which fails.

        restTimetableMockMvc.perform(post("/api/timetables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timetable)))
            .andExpect(status().isBadRequest());

        List<Timetable> timetableList = timetableRepository.findAll();
        assertThat(timetableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTimetables() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);

        // Get all the timetableList
        restTimetableMockMvc.perform(get("/api/timetables?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timetable.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].classDate").value(hasItem(DEFAULT_CLASS_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getTimetable() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);

        // Get the timetable
        restTimetableMockMvc.perform(get("/api/timetables/{id}", timetable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(timetable.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.classDate").value(DEFAULT_CLASS_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllTimetablesBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);

        // Get all the timetableList where subject equals to DEFAULT_SUBJECT
        defaultTimetableShouldBeFound("subject.equals=" + DEFAULT_SUBJECT);

        // Get all the timetableList where subject equals to UPDATED_SUBJECT
        defaultTimetableShouldNotBeFound("subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllTimetablesBySubjectIsNotEqualToSomething() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);

        // Get all the timetableList where subject not equals to DEFAULT_SUBJECT
        defaultTimetableShouldNotBeFound("subject.notEquals=" + DEFAULT_SUBJECT);

        // Get all the timetableList where subject not equals to UPDATED_SUBJECT
        defaultTimetableShouldBeFound("subject.notEquals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllTimetablesBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);

        // Get all the timetableList where subject in DEFAULT_SUBJECT or UPDATED_SUBJECT
        defaultTimetableShouldBeFound("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT);

        // Get all the timetableList where subject equals to UPDATED_SUBJECT
        defaultTimetableShouldNotBeFound("subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllTimetablesBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);

        // Get all the timetableList where subject is not null
        defaultTimetableShouldBeFound("subject.specified=true");

        // Get all the timetableList where subject is null
        defaultTimetableShouldNotBeFound("subject.specified=false");
    }
                @Test
    @Transactional
    public void getAllTimetablesBySubjectContainsSomething() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);

        // Get all the timetableList where subject contains DEFAULT_SUBJECT
        defaultTimetableShouldBeFound("subject.contains=" + DEFAULT_SUBJECT);

        // Get all the timetableList where subject contains UPDATED_SUBJECT
        defaultTimetableShouldNotBeFound("subject.contains=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllTimetablesBySubjectNotContainsSomething() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);

        // Get all the timetableList where subject does not contain DEFAULT_SUBJECT
        defaultTimetableShouldNotBeFound("subject.doesNotContain=" + DEFAULT_SUBJECT);

        // Get all the timetableList where subject does not contain UPDATED_SUBJECT
        defaultTimetableShouldBeFound("subject.doesNotContain=" + UPDATED_SUBJECT);
    }


    @Test
    @Transactional
    public void getAllTimetablesByClassDateIsEqualToSomething() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);

        // Get all the timetableList where classDate equals to DEFAULT_CLASS_DATE
        defaultTimetableShouldBeFound("classDate.equals=" + DEFAULT_CLASS_DATE);

        // Get all the timetableList where classDate equals to UPDATED_CLASS_DATE
        defaultTimetableShouldNotBeFound("classDate.equals=" + UPDATED_CLASS_DATE);
    }

    @Test
    @Transactional
    public void getAllTimetablesByClassDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);

        // Get all the timetableList where classDate not equals to DEFAULT_CLASS_DATE
        defaultTimetableShouldNotBeFound("classDate.notEquals=" + DEFAULT_CLASS_DATE);

        // Get all the timetableList where classDate not equals to UPDATED_CLASS_DATE
        defaultTimetableShouldBeFound("classDate.notEquals=" + UPDATED_CLASS_DATE);
    }

    @Test
    @Transactional
    public void getAllTimetablesByClassDateIsInShouldWork() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);

        // Get all the timetableList where classDate in DEFAULT_CLASS_DATE or UPDATED_CLASS_DATE
        defaultTimetableShouldBeFound("classDate.in=" + DEFAULT_CLASS_DATE + "," + UPDATED_CLASS_DATE);

        // Get all the timetableList where classDate equals to UPDATED_CLASS_DATE
        defaultTimetableShouldNotBeFound("classDate.in=" + UPDATED_CLASS_DATE);
    }

    @Test
    @Transactional
    public void getAllTimetablesByClassDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);

        // Get all the timetableList where classDate is not null
        defaultTimetableShouldBeFound("classDate.specified=true");

        // Get all the timetableList where classDate is null
        defaultTimetableShouldNotBeFound("classDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllTimetablesByClassDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);

        // Get all the timetableList where classDate is greater than or equal to DEFAULT_CLASS_DATE
        defaultTimetableShouldBeFound("classDate.greaterThanOrEqual=" + DEFAULT_CLASS_DATE);

        // Get all the timetableList where classDate is greater than or equal to UPDATED_CLASS_DATE
        defaultTimetableShouldNotBeFound("classDate.greaterThanOrEqual=" + UPDATED_CLASS_DATE);
    }

    @Test
    @Transactional
    public void getAllTimetablesByClassDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);

        // Get all the timetableList where classDate is less than or equal to DEFAULT_CLASS_DATE
        defaultTimetableShouldBeFound("classDate.lessThanOrEqual=" + DEFAULT_CLASS_DATE);

        // Get all the timetableList where classDate is less than or equal to SMALLER_CLASS_DATE
        defaultTimetableShouldNotBeFound("classDate.lessThanOrEqual=" + SMALLER_CLASS_DATE);
    }

    @Test
    @Transactional
    public void getAllTimetablesByClassDateIsLessThanSomething() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);

        // Get all the timetableList where classDate is less than DEFAULT_CLASS_DATE
        defaultTimetableShouldNotBeFound("classDate.lessThan=" + DEFAULT_CLASS_DATE);

        // Get all the timetableList where classDate is less than UPDATED_CLASS_DATE
        defaultTimetableShouldBeFound("classDate.lessThan=" + UPDATED_CLASS_DATE);
    }

    @Test
    @Transactional
    public void getAllTimetablesByClassDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);

        // Get all the timetableList where classDate is greater than DEFAULT_CLASS_DATE
        defaultTimetableShouldNotBeFound("classDate.greaterThan=" + DEFAULT_CLASS_DATE);

        // Get all the timetableList where classDate is greater than SMALLER_CLASS_DATE
        defaultTimetableShouldBeFound("classDate.greaterThan=" + SMALLER_CLASS_DATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTimetableShouldBeFound(String filter) throws Exception {
        restTimetableMockMvc.perform(get("/api/timetables?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timetable.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].classDate").value(hasItem(DEFAULT_CLASS_DATE.toString())));

        // Check, that the count call also returns 1
        restTimetableMockMvc.perform(get("/api/timetables/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTimetableShouldNotBeFound(String filter) throws Exception {
        restTimetableMockMvc.perform(get("/api/timetables?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTimetableMockMvc.perform(get("/api/timetables/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTimetable() throws Exception {
        // Get the timetable
        restTimetableMockMvc.perform(get("/api/timetables/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTimetable() throws Exception {
        // Initialize the database
        timetableService.save(timetable);

        int databaseSizeBeforeUpdate = timetableRepository.findAll().size();

        // Update the timetable
        Timetable updatedTimetable = timetableRepository.findById(timetable.getId()).get();
        // Disconnect from session so that the updates on updatedTimetable are not directly saved in db
        em.detach(updatedTimetable);
        updatedTimetable
            .subject(UPDATED_SUBJECT)
            .classDate(UPDATED_CLASS_DATE);

        restTimetableMockMvc.perform(put("/api/timetables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTimetable)))
            .andExpect(status().isOk());

        // Validate the Timetable in the database
        List<Timetable> timetableList = timetableRepository.findAll();
        assertThat(timetableList).hasSize(databaseSizeBeforeUpdate);
        Timetable testTimetable = timetableList.get(timetableList.size() - 1);
        assertThat(testTimetable.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testTimetable.getClassDate()).isEqualTo(UPDATED_CLASS_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTimetable() throws Exception {
        int databaseSizeBeforeUpdate = timetableRepository.findAll().size();

        // Create the Timetable

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimetableMockMvc.perform(put("/api/timetables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timetable)))
            .andExpect(status().isBadRequest());

        // Validate the Timetable in the database
        List<Timetable> timetableList = timetableRepository.findAll();
        assertThat(timetableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTimetable() throws Exception {
        // Initialize the database
        timetableService.save(timetable);

        int databaseSizeBeforeDelete = timetableRepository.findAll().size();

        // Delete the timetable
        restTimetableMockMvc.perform(delete("/api/timetables/{id}", timetable.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Timetable> timetableList = timetableRepository.findAll();
        assertThat(timetableList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Timetable.class);
        Timetable timetable1 = new Timetable();
        timetable1.setId(1L);
        Timetable timetable2 = new Timetable();
        timetable2.setId(timetable1.getId());
        assertThat(timetable1).isEqualTo(timetable2);
        timetable2.setId(2L);
        assertThat(timetable1).isNotEqualTo(timetable2);
        timetable1.setId(null);
        assertThat(timetable1).isNotEqualTo(timetable2);
    }
}
