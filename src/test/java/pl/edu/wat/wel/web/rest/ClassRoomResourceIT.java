package pl.edu.wat.wel.web.rest;

import pl.edu.wat.wel.SrsApp;
import pl.edu.wat.wel.domain.ClassRoom;
import pl.edu.wat.wel.domain.Reservation;
import pl.edu.wat.wel.domain.ClassModel;
import pl.edu.wat.wel.domain.Building;
import pl.edu.wat.wel.repository.ClassRoomRepository;
import pl.edu.wat.wel.service.ClassRoomService;
import pl.edu.wat.wel.web.rest.errors.ExceptionTranslator;
import pl.edu.wat.wel.service.dto.ClassRoomCriteria;
import pl.edu.wat.wel.service.ClassRoomQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static pl.edu.wat.wel.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ClassRoomResource} REST controller.
 */
@SpringBootTest(classes = SrsApp.class)
public class ClassRoomResourceIT {

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_MAXIMUM_SIZE = 2;
    private static final Integer UPDATED_MAXIMUM_SIZE = 3;
    private static final Integer SMALLER_MAXIMUM_SIZE = 2 - 1;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Mock
    private ClassRoomRepository classRoomRepositoryMock;

    @Mock
    private ClassRoomService classRoomServiceMock;

    @Autowired
    private ClassRoomService classRoomService;

    @Autowired
    private ClassRoomQueryService classRoomQueryService;

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

    private MockMvc restClassRoomMockMvc;

    private ClassRoom classRoom;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClassRoomResource classRoomResource = new ClassRoomResource(classRoomService, classRoomQueryService);
        this.restClassRoomMockMvc = MockMvcBuilders.standaloneSetup(classRoomResource)
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
    public static ClassRoom createEntity(EntityManager em) {
        ClassRoom classRoom = new ClassRoom()
            .number(DEFAULT_NUMBER)
            .maximumSize(DEFAULT_MAXIMUM_SIZE);
        // Add required entity
        Building building;
        if (TestUtil.findAll(em, Building.class).isEmpty()) {
            building = BuildingResourceIT.createEntity(em);
            em.persist(building);
            em.flush();
        } else {
            building = TestUtil.findAll(em, Building.class).get(0);
        }
        classRoom.setBuilding(building);
        return classRoom;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassRoom createUpdatedEntity(EntityManager em) {
        ClassRoom classRoom = new ClassRoom()
            .number(UPDATED_NUMBER)
            .maximumSize(UPDATED_MAXIMUM_SIZE);
        // Add required entity
        Building building;
        if (TestUtil.findAll(em, Building.class).isEmpty()) {
            building = BuildingResourceIT.createUpdatedEntity(em);
            em.persist(building);
            em.flush();
        } else {
            building = TestUtil.findAll(em, Building.class).get(0);
        }
        classRoom.setBuilding(building);
        return classRoom;
    }

    @BeforeEach
    public void initTest() {
        classRoom = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassRoom() throws Exception {
        int databaseSizeBeforeCreate = classRoomRepository.findAll().size();

        // Create the ClassRoom
        restClassRoomMockMvc.perform(post("/api/class-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classRoom)))
            .andExpect(status().isCreated());

        // Validate the ClassRoom in the database
        List<ClassRoom> classRoomList = classRoomRepository.findAll();
        assertThat(classRoomList).hasSize(databaseSizeBeforeCreate + 1);
        ClassRoom testClassRoom = classRoomList.get(classRoomList.size() - 1);
        assertThat(testClassRoom.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testClassRoom.getMaximumSize()).isEqualTo(DEFAULT_MAXIMUM_SIZE);
    }

    @Test
    @Transactional
    public void createClassRoomWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classRoomRepository.findAll().size();

        // Create the ClassRoom with an existing ID
        classRoom.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassRoomMockMvc.perform(post("/api/class-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classRoom)))
            .andExpect(status().isBadRequest());

        // Validate the ClassRoom in the database
        List<ClassRoom> classRoomList = classRoomRepository.findAll();
        assertThat(classRoomList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = classRoomRepository.findAll().size();
        // set the field null
        classRoom.setNumber(null);

        // Create the ClassRoom, which fails.

        restClassRoomMockMvc.perform(post("/api/class-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classRoom)))
            .andExpect(status().isBadRequest());

        List<ClassRoom> classRoomList = classRoomRepository.findAll();
        assertThat(classRoomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClassRooms() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList
        restClassRoomMockMvc.perform(get("/api/class-rooms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classRoom.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].maximumSize").value(hasItem(DEFAULT_MAXIMUM_SIZE)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllClassRoomsWithEagerRelationshipsIsEnabled() throws Exception {
        ClassRoomResource classRoomResource = new ClassRoomResource(classRoomServiceMock, classRoomQueryService);
        when(classRoomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restClassRoomMockMvc = MockMvcBuilders.standaloneSetup(classRoomResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restClassRoomMockMvc.perform(get("/api/class-rooms?eagerload=true"))
        .andExpect(status().isOk());

        verify(classRoomServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllClassRoomsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ClassRoomResource classRoomResource = new ClassRoomResource(classRoomServiceMock, classRoomQueryService);
            when(classRoomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restClassRoomMockMvc = MockMvcBuilders.standaloneSetup(classRoomResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restClassRoomMockMvc.perform(get("/api/class-rooms?eagerload=true"))
        .andExpect(status().isOk());

            verify(classRoomServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getClassRoom() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get the classRoom
        restClassRoomMockMvc.perform(get("/api/class-rooms/{id}", classRoom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(classRoom.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.maximumSize").value(DEFAULT_MAXIMUM_SIZE));
    }

    @Test
    @Transactional
    public void getAllClassRoomsByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where number equals to DEFAULT_NUMBER
        defaultClassRoomShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the classRoomList where number equals to UPDATED_NUMBER
        defaultClassRoomShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllClassRoomsByNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where number not equals to DEFAULT_NUMBER
        defaultClassRoomShouldNotBeFound("number.notEquals=" + DEFAULT_NUMBER);

        // Get all the classRoomList where number not equals to UPDATED_NUMBER
        defaultClassRoomShouldBeFound("number.notEquals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllClassRoomsByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultClassRoomShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the classRoomList where number equals to UPDATED_NUMBER
        defaultClassRoomShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllClassRoomsByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where number is not null
        defaultClassRoomShouldBeFound("number.specified=true");

        // Get all the classRoomList where number is null
        defaultClassRoomShouldNotBeFound("number.specified=false");
    }
                @Test
    @Transactional
    public void getAllClassRoomsByNumberContainsSomething() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where number contains DEFAULT_NUMBER
        defaultClassRoomShouldBeFound("number.contains=" + DEFAULT_NUMBER);

        // Get all the classRoomList where number contains UPDATED_NUMBER
        defaultClassRoomShouldNotBeFound("number.contains=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllClassRoomsByNumberNotContainsSomething() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where number does not contain DEFAULT_NUMBER
        defaultClassRoomShouldNotBeFound("number.doesNotContain=" + DEFAULT_NUMBER);

        // Get all the classRoomList where number does not contain UPDATED_NUMBER
        defaultClassRoomShouldBeFound("number.doesNotContain=" + UPDATED_NUMBER);
    }


    @Test
    @Transactional
    public void getAllClassRoomsByMaximumSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where maximumSize equals to DEFAULT_MAXIMUM_SIZE
        defaultClassRoomShouldBeFound("maximumSize.equals=" + DEFAULT_MAXIMUM_SIZE);

        // Get all the classRoomList where maximumSize equals to UPDATED_MAXIMUM_SIZE
        defaultClassRoomShouldNotBeFound("maximumSize.equals=" + UPDATED_MAXIMUM_SIZE);
    }

    @Test
    @Transactional
    public void getAllClassRoomsByMaximumSizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where maximumSize not equals to DEFAULT_MAXIMUM_SIZE
        defaultClassRoomShouldNotBeFound("maximumSize.notEquals=" + DEFAULT_MAXIMUM_SIZE);

        // Get all the classRoomList where maximumSize not equals to UPDATED_MAXIMUM_SIZE
        defaultClassRoomShouldBeFound("maximumSize.notEquals=" + UPDATED_MAXIMUM_SIZE);
    }

    @Test
    @Transactional
    public void getAllClassRoomsByMaximumSizeIsInShouldWork() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where maximumSize in DEFAULT_MAXIMUM_SIZE or UPDATED_MAXIMUM_SIZE
        defaultClassRoomShouldBeFound("maximumSize.in=" + DEFAULT_MAXIMUM_SIZE + "," + UPDATED_MAXIMUM_SIZE);

        // Get all the classRoomList where maximumSize equals to UPDATED_MAXIMUM_SIZE
        defaultClassRoomShouldNotBeFound("maximumSize.in=" + UPDATED_MAXIMUM_SIZE);
    }

    @Test
    @Transactional
    public void getAllClassRoomsByMaximumSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where maximumSize is not null
        defaultClassRoomShouldBeFound("maximumSize.specified=true");

        // Get all the classRoomList where maximumSize is null
        defaultClassRoomShouldNotBeFound("maximumSize.specified=false");
    }

    @Test
    @Transactional
    public void getAllClassRoomsByMaximumSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where maximumSize is greater than or equal to DEFAULT_MAXIMUM_SIZE
        defaultClassRoomShouldBeFound("maximumSize.greaterThanOrEqual=" + DEFAULT_MAXIMUM_SIZE);

        // Get all the classRoomList where maximumSize is greater than or equal to (DEFAULT_MAXIMUM_SIZE + 1)
        defaultClassRoomShouldNotBeFound("maximumSize.greaterThanOrEqual=" + (DEFAULT_MAXIMUM_SIZE + 1));
    }

    @Test
    @Transactional
    public void getAllClassRoomsByMaximumSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where maximumSize is less than or equal to DEFAULT_MAXIMUM_SIZE
        defaultClassRoomShouldBeFound("maximumSize.lessThanOrEqual=" + DEFAULT_MAXIMUM_SIZE);

        // Get all the classRoomList where maximumSize is less than or equal to SMALLER_MAXIMUM_SIZE
        defaultClassRoomShouldNotBeFound("maximumSize.lessThanOrEqual=" + SMALLER_MAXIMUM_SIZE);
    }

    @Test
    @Transactional
    public void getAllClassRoomsByMaximumSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where maximumSize is less than DEFAULT_MAXIMUM_SIZE
        defaultClassRoomShouldNotBeFound("maximumSize.lessThan=" + DEFAULT_MAXIMUM_SIZE);

        // Get all the classRoomList where maximumSize is less than (DEFAULT_MAXIMUM_SIZE + 1)
        defaultClassRoomShouldBeFound("maximumSize.lessThan=" + (DEFAULT_MAXIMUM_SIZE + 1));
    }

    @Test
    @Transactional
    public void getAllClassRoomsByMaximumSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where maximumSize is greater than DEFAULT_MAXIMUM_SIZE
        defaultClassRoomShouldNotBeFound("maximumSize.greaterThan=" + DEFAULT_MAXIMUM_SIZE);

        // Get all the classRoomList where maximumSize is greater than SMALLER_MAXIMUM_SIZE
        defaultClassRoomShouldBeFound("maximumSize.greaterThan=" + SMALLER_MAXIMUM_SIZE);
    }


    @Test
    @Transactional
    public void getAllClassRoomsByReservationCIsEqualToSomething() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);
        Reservation reservationC = ReservationResourceIT.createEntity(em);
        em.persist(reservationC);
        em.flush();
        classRoom.addReservationC(reservationC);
        classRoomRepository.saveAndFlush(classRoom);
        Long reservationCId = reservationC.getId();

        // Get all the classRoomList where reservationC equals to reservationCId
        defaultClassRoomShouldBeFound("reservationCId.equals=" + reservationCId);

        // Get all the classRoomList where reservationC equals to reservationCId + 1
        defaultClassRoomShouldNotBeFound("reservationCId.equals=" + (reservationCId + 1));
    }


    @Test
    @Transactional
    public void getAllClassRoomsByClassModelIsEqualToSomething() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);
        ClassModel classModel = ClassModelResourceIT.createEntity(em);
        em.persist(classModel);
        em.flush();
        classRoom.addClassModel(classModel);
        classRoomRepository.saveAndFlush(classRoom);
        Long classModelId = classModel.getId();

        // Get all the classRoomList where classModel equals to classModelId
        defaultClassRoomShouldBeFound("classModelId.equals=" + classModelId);

        // Get all the classRoomList where classModel equals to classModelId + 1
        defaultClassRoomShouldNotBeFound("classModelId.equals=" + (classModelId + 1));
    }


    @Test
    @Transactional
    public void getAllClassRoomsByBuildingIsEqualToSomething() throws Exception {
        // Get already existing entity
        Building building = classRoom.getBuilding();
        classRoomRepository.saveAndFlush(classRoom);
        Long buildingId = building.getId();

        // Get all the classRoomList where building equals to buildingId
        defaultClassRoomShouldBeFound("buildingId.equals=" + buildingId);

        // Get all the classRoomList where building equals to buildingId + 1
        defaultClassRoomShouldNotBeFound("buildingId.equals=" + (buildingId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassRoomShouldBeFound(String filter) throws Exception {
        restClassRoomMockMvc.perform(get("/api/class-rooms?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classRoom.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].maximumSize").value(hasItem(DEFAULT_MAXIMUM_SIZE)));

        // Check, that the count call also returns 1
        restClassRoomMockMvc.perform(get("/api/class-rooms/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassRoomShouldNotBeFound(String filter) throws Exception {
        restClassRoomMockMvc.perform(get("/api/class-rooms?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassRoomMockMvc.perform(get("/api/class-rooms/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingClassRoom() throws Exception {
        // Get the classRoom
        restClassRoomMockMvc.perform(get("/api/class-rooms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassRoom() throws Exception {
        // Initialize the database
        classRoomService.save(classRoom);

        int databaseSizeBeforeUpdate = classRoomRepository.findAll().size();

        // Update the classRoom
        ClassRoom updatedClassRoom = classRoomRepository.findById(classRoom.getId()).get();
        // Disconnect from session so that the updates on updatedClassRoom are not directly saved in db
        em.detach(updatedClassRoom);
        updatedClassRoom
            .number(UPDATED_NUMBER)
            .maximumSize(UPDATED_MAXIMUM_SIZE);

        restClassRoomMockMvc.perform(put("/api/class-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedClassRoom)))
            .andExpect(status().isOk());

        // Validate the ClassRoom in the database
        List<ClassRoom> classRoomList = classRoomRepository.findAll();
        assertThat(classRoomList).hasSize(databaseSizeBeforeUpdate);
        ClassRoom testClassRoom = classRoomList.get(classRoomList.size() - 1);
        assertThat(testClassRoom.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testClassRoom.getMaximumSize()).isEqualTo(UPDATED_MAXIMUM_SIZE);
    }

    @Test
    @Transactional
    public void updateNonExistingClassRoom() throws Exception {
        int databaseSizeBeforeUpdate = classRoomRepository.findAll().size();

        // Create the ClassRoom

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassRoomMockMvc.perform(put("/api/class-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classRoom)))
            .andExpect(status().isBadRequest());

        // Validate the ClassRoom in the database
        List<ClassRoom> classRoomList = classRoomRepository.findAll();
        assertThat(classRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClassRoom() throws Exception {
        // Initialize the database
        classRoomService.save(classRoom);

        int databaseSizeBeforeDelete = classRoomRepository.findAll().size();

        // Delete the classRoom
        restClassRoomMockMvc.perform(delete("/api/class-rooms/{id}", classRoom.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassRoom> classRoomList = classRoomRepository.findAll();
        assertThat(classRoomList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassRoom.class);
        ClassRoom classRoom1 = new ClassRoom();
        classRoom1.setId(1L);
        ClassRoom classRoom2 = new ClassRoom();
        classRoom2.setId(classRoom1.getId());
        assertThat(classRoom1).isEqualTo(classRoom2);
        classRoom2.setId(2L);
        assertThat(classRoom1).isNotEqualTo(classRoom2);
        classRoom1.setId(null);
        assertThat(classRoom1).isNotEqualTo(classRoom2);
    }
}
