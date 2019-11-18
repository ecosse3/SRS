package pl.edu.wat.wel.web.rest;

import pl.edu.wat.wel.SrsApp;
import pl.edu.wat.wel.domain.SchoolGroup;
import pl.edu.wat.wel.domain.User;
import pl.edu.wat.wel.domain.Reservation;
import pl.edu.wat.wel.domain.Major;
import pl.edu.wat.wel.repository.SchoolGroupRepository;
import pl.edu.wat.wel.service.SchoolGroupService;
import pl.edu.wat.wel.web.rest.errors.ExceptionTranslator;
import pl.edu.wat.wel.service.dto.SchoolGroupCriteria;
import pl.edu.wat.wel.service.SchoolGroupQueryService;

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
 * Integration tests for the {@link SchoolGroupResource} REST controller.
 */
@SpringBootTest(classes = SrsApp.class)
public class SchoolGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private SchoolGroupRepository schoolGroupRepository;

    @Autowired
    private SchoolGroupService schoolGroupService;

    @Autowired
    private SchoolGroupQueryService schoolGroupQueryService;

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

    private MockMvc restSchoolGroupMockMvc;

    private SchoolGroup schoolGroup;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SchoolGroupResource schoolGroupResource = new SchoolGroupResource(schoolGroupService, schoolGroupQueryService);
        this.restSchoolGroupMockMvc = MockMvcBuilders.standaloneSetup(schoolGroupResource)
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
    public static SchoolGroup createEntity(EntityManager em) {
        SchoolGroup schoolGroup = new SchoolGroup()
            .name(DEFAULT_NAME);
        // Add required entity
        Major major;
        if (TestUtil.findAll(em, Major.class).isEmpty()) {
            major = MajorResourceIT.createEntity(em);
            em.persist(major);
            em.flush();
        } else {
            major = TestUtil.findAll(em, Major.class).get(0);
        }
        schoolGroup.setMajor(major);
        return schoolGroup;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolGroup createUpdatedEntity(EntityManager em) {
        SchoolGroup schoolGroup = new SchoolGroup()
            .name(UPDATED_NAME);
        // Add required entity
        Major major;
        if (TestUtil.findAll(em, Major.class).isEmpty()) {
            major = MajorResourceIT.createUpdatedEntity(em);
            em.persist(major);
            em.flush();
        } else {
            major = TestUtil.findAll(em, Major.class).get(0);
        }
        schoolGroup.setMajor(major);
        return schoolGroup;
    }

    @BeforeEach
    public void initTest() {
        schoolGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createSchoolGroup() throws Exception {
        int databaseSizeBeforeCreate = schoolGroupRepository.findAll().size();

        // Create the SchoolGroup
        restSchoolGroupMockMvc.perform(post("/api/school-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolGroup)))
            .andExpect(status().isCreated());

        // Validate the SchoolGroup in the database
        List<SchoolGroup> schoolGroupList = schoolGroupRepository.findAll();
        assertThat(schoolGroupList).hasSize(databaseSizeBeforeCreate + 1);
        SchoolGroup testSchoolGroup = schoolGroupList.get(schoolGroupList.size() - 1);
        assertThat(testSchoolGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createSchoolGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = schoolGroupRepository.findAll().size();

        // Create the SchoolGroup with an existing ID
        schoolGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolGroupMockMvc.perform(post("/api/school-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolGroup)))
            .andExpect(status().isBadRequest());

        // Validate the SchoolGroup in the database
        List<SchoolGroup> schoolGroupList = schoolGroupRepository.findAll();
        assertThat(schoolGroupList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolGroupRepository.findAll().size();
        // set the field null
        schoolGroup.setName(null);

        // Create the SchoolGroup, which fails.

        restSchoolGroupMockMvc.perform(post("/api/school-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolGroup)))
            .andExpect(status().isBadRequest());

        List<SchoolGroup> schoolGroupList = schoolGroupRepository.findAll();
        assertThat(schoolGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSchoolGroups() throws Exception {
        // Initialize the database
        schoolGroupRepository.saveAndFlush(schoolGroup);

        // Get all the schoolGroupList
        restSchoolGroupMockMvc.perform(get("/api/school-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getSchoolGroup() throws Exception {
        // Initialize the database
        schoolGroupRepository.saveAndFlush(schoolGroup);

        // Get the schoolGroup
        restSchoolGroupMockMvc.perform(get("/api/school-groups/{id}", schoolGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(schoolGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getAllSchoolGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolGroupRepository.saveAndFlush(schoolGroup);

        // Get all the schoolGroupList where name equals to DEFAULT_NAME
        defaultSchoolGroupShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the schoolGroupList where name equals to UPDATED_NAME
        defaultSchoolGroupShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSchoolGroupsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolGroupRepository.saveAndFlush(schoolGroup);

        // Get all the schoolGroupList where name not equals to DEFAULT_NAME
        defaultSchoolGroupShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the schoolGroupList where name not equals to UPDATED_NAME
        defaultSchoolGroupShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSchoolGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        schoolGroupRepository.saveAndFlush(schoolGroup);

        // Get all the schoolGroupList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSchoolGroupShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the schoolGroupList where name equals to UPDATED_NAME
        defaultSchoolGroupShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSchoolGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolGroupRepository.saveAndFlush(schoolGroup);

        // Get all the schoolGroupList where name is not null
        defaultSchoolGroupShouldBeFound("name.specified=true");

        // Get all the schoolGroupList where name is null
        defaultSchoolGroupShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllSchoolGroupsByNameContainsSomething() throws Exception {
        // Initialize the database
        schoolGroupRepository.saveAndFlush(schoolGroup);

        // Get all the schoolGroupList where name contains DEFAULT_NAME
        defaultSchoolGroupShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the schoolGroupList where name contains UPDATED_NAME
        defaultSchoolGroupShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSchoolGroupsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        schoolGroupRepository.saveAndFlush(schoolGroup);

        // Get all the schoolGroupList where name does not contain DEFAULT_NAME
        defaultSchoolGroupShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the schoolGroupList where name does not contain UPDATED_NAME
        defaultSchoolGroupShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllSchoolGroupsByStarostIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolGroupRepository.saveAndFlush(schoolGroup);
        User starost = UserResourceIT.createEntity(em);
        em.persist(starost);
        em.flush();
        schoolGroup.setStarost(starost);
        schoolGroupRepository.saveAndFlush(schoolGroup);
        Long starostId = starost.getId();

        // Get all the schoolGroupList where starost equals to starostId
        defaultSchoolGroupShouldBeFound("starostId.equals=" + starostId);

        // Get all the schoolGroupList where starost equals to starostId + 1
        defaultSchoolGroupShouldNotBeFound("starostId.equals=" + (starostId + 1));
    }


    @Test
    @Transactional
    public void getAllSchoolGroupsByReservationSIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolGroupRepository.saveAndFlush(schoolGroup);
        Reservation reservationS = ReservationResourceIT.createEntity(em);
        em.persist(reservationS);
        em.flush();
        schoolGroup.addReservationS(reservationS);
        schoolGroupRepository.saveAndFlush(schoolGroup);
        Long reservationSId = reservationS.getId();

        // Get all the schoolGroupList where reservationS equals to reservationSId
        defaultSchoolGroupShouldBeFound("reservationSId.equals=" + reservationSId);

        // Get all the schoolGroupList where reservationS equals to reservationSId + 1
        defaultSchoolGroupShouldNotBeFound("reservationSId.equals=" + (reservationSId + 1));
    }


    @Test
    @Transactional
    public void getAllSchoolGroupsByMajorIsEqualToSomething() throws Exception {
        // Get already existing entity
        Major major = schoolGroup.getMajor();
        schoolGroupRepository.saveAndFlush(schoolGroup);
        Long majorId = major.getId();

        // Get all the schoolGroupList where major equals to majorId
        defaultSchoolGroupShouldBeFound("majorId.equals=" + majorId);

        // Get all the schoolGroupList where major equals to majorId + 1
        defaultSchoolGroupShouldNotBeFound("majorId.equals=" + (majorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSchoolGroupShouldBeFound(String filter) throws Exception {
        restSchoolGroupMockMvc.perform(get("/api/school-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restSchoolGroupMockMvc.perform(get("/api/school-groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSchoolGroupShouldNotBeFound(String filter) throws Exception {
        restSchoolGroupMockMvc.perform(get("/api/school-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSchoolGroupMockMvc.perform(get("/api/school-groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSchoolGroup() throws Exception {
        // Get the schoolGroup
        restSchoolGroupMockMvc.perform(get("/api/school-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSchoolGroup() throws Exception {
        // Initialize the database
        schoolGroupService.save(schoolGroup);

        int databaseSizeBeforeUpdate = schoolGroupRepository.findAll().size();

        // Update the schoolGroup
        SchoolGroup updatedSchoolGroup = schoolGroupRepository.findById(schoolGroup.getId()).get();
        // Disconnect from session so that the updates on updatedSchoolGroup are not directly saved in db
        em.detach(updatedSchoolGroup);
        updatedSchoolGroup
            .name(UPDATED_NAME);

        restSchoolGroupMockMvc.perform(put("/api/school-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSchoolGroup)))
            .andExpect(status().isOk());

        // Validate the SchoolGroup in the database
        List<SchoolGroup> schoolGroupList = schoolGroupRepository.findAll();
        assertThat(schoolGroupList).hasSize(databaseSizeBeforeUpdate);
        SchoolGroup testSchoolGroup = schoolGroupList.get(schoolGroupList.size() - 1);
        assertThat(testSchoolGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingSchoolGroup() throws Exception {
        int databaseSizeBeforeUpdate = schoolGroupRepository.findAll().size();

        // Create the SchoolGroup

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolGroupMockMvc.perform(put("/api/school-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolGroup)))
            .andExpect(status().isBadRequest());

        // Validate the SchoolGroup in the database
        List<SchoolGroup> schoolGroupList = schoolGroupRepository.findAll();
        assertThat(schoolGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSchoolGroup() throws Exception {
        // Initialize the database
        schoolGroupService.save(schoolGroup);

        int databaseSizeBeforeDelete = schoolGroupRepository.findAll().size();

        // Delete the schoolGroup
        restSchoolGroupMockMvc.perform(delete("/api/school-groups/{id}", schoolGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SchoolGroup> schoolGroupList = schoolGroupRepository.findAll();
        assertThat(schoolGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolGroup.class);
        SchoolGroup schoolGroup1 = new SchoolGroup();
        schoolGroup1.setId(1L);
        SchoolGroup schoolGroup2 = new SchoolGroup();
        schoolGroup2.setId(schoolGroup1.getId());
        assertThat(schoolGroup1).isEqualTo(schoolGroup2);
        schoolGroup2.setId(2L);
        assertThat(schoolGroup1).isNotEqualTo(schoolGroup2);
        schoolGroup1.setId(null);
        assertThat(schoolGroup1).isNotEqualTo(schoolGroup2);
    }
}
