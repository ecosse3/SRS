package pl.edu.wat.wel.web.rest;

import pl.edu.wat.wel.SrsApp;
import pl.edu.wat.wel.domain.Major;
import pl.edu.wat.wel.domain.Department;
import pl.edu.wat.wel.repository.MajorRepository;
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
 * Integration tests for the {@link MajorResource} REST controller.
 */
@SpringBootTest(classes = SrsApp.class)
public class MajorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private MajorRepository majorRepository;

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

    private MockMvc restMajorMockMvc;

    private Major major;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MajorResource majorResource = new MajorResource(majorRepository);
        this.restMajorMockMvc = MockMvcBuilders.standaloneSetup(majorResource)
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
    public static Major createEntity(EntityManager em) {
        Major major = new Major()
            .name(DEFAULT_NAME);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        major.setDepartment(department);
        return major;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Major createUpdatedEntity(EntityManager em) {
        Major major = new Major()
            .name(UPDATED_NAME);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createUpdatedEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        major.setDepartment(department);
        return major;
    }

    @BeforeEach
    public void initTest() {
        major = createEntity(em);
    }

    @Test
    @Transactional
    public void createMajor() throws Exception {
        int databaseSizeBeforeCreate = majorRepository.findAll().size();

        // Create the Major
        restMajorMockMvc.perform(post("/api/majors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(major)))
            .andExpect(status().isCreated());

        // Validate the Major in the database
        List<Major> majorList = majorRepository.findAll();
        assertThat(majorList).hasSize(databaseSizeBeforeCreate + 1);
        Major testMajor = majorList.get(majorList.size() - 1);
        assertThat(testMajor.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createMajorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = majorRepository.findAll().size();

        // Create the Major with an existing ID
        major.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMajorMockMvc.perform(post("/api/majors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(major)))
            .andExpect(status().isBadRequest());

        // Validate the Major in the database
        List<Major> majorList = majorRepository.findAll();
        assertThat(majorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = majorRepository.findAll().size();
        // set the field null
        major.setName(null);

        // Create the Major, which fails.

        restMajorMockMvc.perform(post("/api/majors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(major)))
            .andExpect(status().isBadRequest());

        List<Major> majorList = majorRepository.findAll();
        assertThat(majorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMajors() throws Exception {
        // Initialize the database
        majorRepository.saveAndFlush(major);

        // Get all the majorList
        restMajorMockMvc.perform(get("/api/majors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(major.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getMajor() throws Exception {
        // Initialize the database
        majorRepository.saveAndFlush(major);

        // Get the major
        restMajorMockMvc.perform(get("/api/majors/{id}", major.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(major.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingMajor() throws Exception {
        // Get the major
        restMajorMockMvc.perform(get("/api/majors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMajor() throws Exception {
        // Initialize the database
        majorRepository.saveAndFlush(major);

        int databaseSizeBeforeUpdate = majorRepository.findAll().size();

        // Update the major
        Major updatedMajor = majorRepository.findById(major.getId()).get();
        // Disconnect from session so that the updates on updatedMajor are not directly saved in db
        em.detach(updatedMajor);
        updatedMajor
            .name(UPDATED_NAME);

        restMajorMockMvc.perform(put("/api/majors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMajor)))
            .andExpect(status().isOk());

        // Validate the Major in the database
        List<Major> majorList = majorRepository.findAll();
        assertThat(majorList).hasSize(databaseSizeBeforeUpdate);
        Major testMajor = majorList.get(majorList.size() - 1);
        assertThat(testMajor.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingMajor() throws Exception {
        int databaseSizeBeforeUpdate = majorRepository.findAll().size();

        // Create the Major

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMajorMockMvc.perform(put("/api/majors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(major)))
            .andExpect(status().isBadRequest());

        // Validate the Major in the database
        List<Major> majorList = majorRepository.findAll();
        assertThat(majorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMajor() throws Exception {
        // Initialize the database
        majorRepository.saveAndFlush(major);

        int databaseSizeBeforeDelete = majorRepository.findAll().size();

        // Delete the major
        restMajorMockMvc.perform(delete("/api/majors/{id}", major.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Major> majorList = majorRepository.findAll();
        assertThat(majorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Major.class);
        Major major1 = new Major();
        major1.setId(1L);
        Major major2 = new Major();
        major2.setId(major1.getId());
        assertThat(major1).isEqualTo(major2);
        major2.setId(2L);
        assertThat(major1).isNotEqualTo(major2);
        major1.setId(null);
        assertThat(major1).isNotEqualTo(major2);
    }
}
