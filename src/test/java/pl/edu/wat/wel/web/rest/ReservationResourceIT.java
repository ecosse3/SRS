package pl.edu.wat.wel.web.rest;

import pl.edu.wat.wel.SrsApp;
import pl.edu.wat.wel.domain.Reservation;
import pl.edu.wat.wel.domain.User;
import pl.edu.wat.wel.domain.SchoolGroup;
import pl.edu.wat.wel.domain.Building;
import pl.edu.wat.wel.domain.ClassRoom;
import pl.edu.wat.wel.domain.ClassHours;
import pl.edu.wat.wel.domain.ClassDuration;
import pl.edu.wat.wel.domain.Status;
import pl.edu.wat.wel.repository.ReservationRepository;
import pl.edu.wat.wel.service.ReservationService;
import pl.edu.wat.wel.web.rest.errors.ExceptionTranslator;
import pl.edu.wat.wel.service.dto.ReservationCriteria;
import pl.edu.wat.wel.service.ReservationQueryService;

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
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static pl.edu.wat.wel.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ReservationResource} REST controller.
 */
@SpringBootTest(classes = SrsApp.class)
public class ReservationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE_TO_TEACHER = "AAAAAAAAAA";
    private static final String UPDATED_NOTE_TO_TEACHER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ORIGINAL_CLASS_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ORIGINAL_CLASS_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ORIGINAL_CLASS_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_NEW_CLASS_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NEW_CLASS_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_NEW_CLASS_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_REQUESTED_BY = "AAAAAAAAAA";
    private static final String UPDATED_REQUESTED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationRepository reservationRepositoryMock;

    @Mock
    private ReservationService reservationServiceMock;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationQueryService reservationQueryService;

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

    private MockMvc restReservationMockMvc;

    private Reservation reservation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReservationResource reservationResource = new ReservationResource(reservationService, reservationQueryService);
        this.restReservationMockMvc = MockMvcBuilders.standaloneSetup(reservationResource)
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
    public static Reservation createEntity(EntityManager em) {
        Reservation reservation = new Reservation()
            .name(DEFAULT_NAME)
            .noteToTeacher(DEFAULT_NOTE_TO_TEACHER)
            .originalClassDate(DEFAULT_ORIGINAL_CLASS_DATE)
            .newClassDate(DEFAULT_NEW_CLASS_DATE)
            .requestedBy(DEFAULT_REQUESTED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
        // Add required entity
        SchoolGroup schoolGroup;
        if (TestUtil.findAll(em, SchoolGroup.class).isEmpty()) {
            schoolGroup = SchoolGroupResourceIT.createEntity(em);
            em.persist(schoolGroup);
            em.flush();
        } else {
            schoolGroup = TestUtil.findAll(em, SchoolGroup.class).get(0);
        }
        reservation.setSchoolGroup(schoolGroup);
        // Add required entity
        Building building;
        if (TestUtil.findAll(em, Building.class).isEmpty()) {
            building = BuildingResourceIT.createEntity(em);
            em.persist(building);
            em.flush();
        } else {
            building = TestUtil.findAll(em, Building.class).get(0);
        }
        reservation.setBuilding(building);
        // Add required entity
        ClassHours classHours;
        if (TestUtil.findAll(em, ClassHours.class).isEmpty()) {
            classHours = ClassHoursResourceIT.createEntity(em);
            em.persist(classHours);
            em.flush();
        } else {
            classHours = TestUtil.findAll(em, ClassHours.class).get(0);
        }
        reservation.setOriginalStartTime(classHours);
        // Add required entity
        ClassDuration classDuration;
        if (TestUtil.findAll(em, ClassDuration.class).isEmpty()) {
            classDuration = ClassDurationResourceIT.createEntity(em);
            em.persist(classDuration);
            em.flush();
        } else {
            classDuration = TestUtil.findAll(em, ClassDuration.class).get(0);
        }
        reservation.setClassDuration(classDuration);
        return reservation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reservation createUpdatedEntity(EntityManager em) {
        Reservation reservation = new Reservation()
            .name(UPDATED_NAME)
            .noteToTeacher(UPDATED_NOTE_TO_TEACHER)
            .originalClassDate(UPDATED_ORIGINAL_CLASS_DATE)
            .newClassDate(UPDATED_NEW_CLASS_DATE)
            .requestedBy(UPDATED_REQUESTED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        // Add required entity
        SchoolGroup schoolGroup;
        if (TestUtil.findAll(em, SchoolGroup.class).isEmpty()) {
            schoolGroup = SchoolGroupResourceIT.createUpdatedEntity(em);
            em.persist(schoolGroup);
            em.flush();
        } else {
            schoolGroup = TestUtil.findAll(em, SchoolGroup.class).get(0);
        }
        reservation.setSchoolGroup(schoolGroup);
        // Add required entity
        Building building;
        if (TestUtil.findAll(em, Building.class).isEmpty()) {
            building = BuildingResourceIT.createUpdatedEntity(em);
            em.persist(building);
            em.flush();
        } else {
            building = TestUtil.findAll(em, Building.class).get(0);
        }
        reservation.setBuilding(building);
        // Add required entity
        ClassHours classHours;
        if (TestUtil.findAll(em, ClassHours.class).isEmpty()) {
            classHours = ClassHoursResourceIT.createUpdatedEntity(em);
            em.persist(classHours);
            em.flush();
        } else {
            classHours = TestUtil.findAll(em, ClassHours.class).get(0);
        }
        reservation.setOriginalStartTime(classHours);
        // Add required entity
        ClassDuration classDuration;
        if (TestUtil.findAll(em, ClassDuration.class).isEmpty()) {
            classDuration = ClassDurationResourceIT.createUpdatedEntity(em);
            em.persist(classDuration);
            em.flush();
        } else {
            classDuration = TestUtil.findAll(em, ClassDuration.class).get(0);
        }
        reservation.setClassDuration(classDuration);
        return reservation;
    }

    @BeforeEach
    public void initTest() {
        reservation = createEntity(em);
    }

    @Test
    @Transactional
    public void createReservation() throws Exception {
        int databaseSizeBeforeCreate = reservationRepository.findAll().size();

        // Create the Reservation
        restReservationMockMvc.perform(post("/api/reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservation)))
            .andExpect(status().isCreated());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeCreate + 1);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testReservation.getNoteToTeacher()).isEqualTo(DEFAULT_NOTE_TO_TEACHER);
        assertThat(testReservation.getOriginalClassDate()).isEqualTo(DEFAULT_ORIGINAL_CLASS_DATE);
        assertThat(testReservation.getNewClassDate()).isEqualTo(DEFAULT_NEW_CLASS_DATE);
        assertThat(testReservation.getRequestedBy()).isEqualTo(DEFAULT_REQUESTED_BY);
        assertThat(testReservation.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void createReservationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reservationRepository.findAll().size();

        // Create the Reservation with an existing ID
        reservation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservationMockMvc.perform(post("/api/reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservation)))
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservationRepository.findAll().size();
        // set the field null
        reservation.setName(null);

        // Create the Reservation, which fails.

        restReservationMockMvc.perform(post("/api/reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservation)))
            .andExpect(status().isBadRequest());

        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOriginalClassDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservationRepository.findAll().size();
        // set the field null
        reservation.setOriginalClassDate(null);

        // Create the Reservation, which fails.

        restReservationMockMvc.perform(post("/api/reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservation)))
            .andExpect(status().isBadRequest());

        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNewClassDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservationRepository.findAll().size();
        // set the field null
        reservation.setNewClassDate(null);

        // Create the Reservation, which fails.

        restReservationMockMvc.perform(post("/api/reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservation)))
            .andExpect(status().isBadRequest());

        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReservations() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList
        restReservationMockMvc.perform(get("/api/reservations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].noteToTeacher").value(hasItem(DEFAULT_NOTE_TO_TEACHER)))
            .andExpect(jsonPath("$.[*].originalClassDate").value(hasItem(DEFAULT_ORIGINAL_CLASS_DATE.toString())))
            .andExpect(jsonPath("$.[*].newClassDate").value(hasItem(DEFAULT_NEW_CLASS_DATE.toString())))
            .andExpect(jsonPath("$.[*].requestedBy").value(hasItem(DEFAULT_REQUESTED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllReservationsWithEagerRelationshipsIsEnabled() throws Exception {
        ReservationResource reservationResource = new ReservationResource(reservationServiceMock, reservationQueryService);
        when(reservationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restReservationMockMvc = MockMvcBuilders.standaloneSetup(reservationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restReservationMockMvc.perform(get("/api/reservations?eagerload=true"))
        .andExpect(status().isOk());

        verify(reservationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllReservationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ReservationResource reservationResource = new ReservationResource(reservationServiceMock, reservationQueryService);
            when(reservationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restReservationMockMvc = MockMvcBuilders.standaloneSetup(reservationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restReservationMockMvc.perform(get("/api/reservations?eagerload=true"))
        .andExpect(status().isOk());

            verify(reservationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get the reservation
        restReservationMockMvc.perform(get("/api/reservations/{id}", reservation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reservation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.noteToTeacher").value(DEFAULT_NOTE_TO_TEACHER))
            .andExpect(jsonPath("$.originalClassDate").value(DEFAULT_ORIGINAL_CLASS_DATE.toString()))
            .andExpect(jsonPath("$.newClassDate").value(DEFAULT_NEW_CLASS_DATE.toString()))
            .andExpect(jsonPath("$.requestedBy").value(DEFAULT_REQUESTED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllReservationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where name equals to DEFAULT_NAME
        defaultReservationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the reservationList where name equals to UPDATED_NAME
        defaultReservationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllReservationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where name not equals to DEFAULT_NAME
        defaultReservationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the reservationList where name not equals to UPDATED_NAME
        defaultReservationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllReservationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultReservationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the reservationList where name equals to UPDATED_NAME
        defaultReservationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllReservationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where name is not null
        defaultReservationShouldBeFound("name.specified=true");

        // Get all the reservationList where name is null
        defaultReservationShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllReservationsByNameContainsSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where name contains DEFAULT_NAME
        defaultReservationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the reservationList where name contains UPDATED_NAME
        defaultReservationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllReservationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where name does not contain DEFAULT_NAME
        defaultReservationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the reservationList where name does not contain UPDATED_NAME
        defaultReservationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllReservationsByNoteToTeacherIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where noteToTeacher equals to DEFAULT_NOTE_TO_TEACHER
        defaultReservationShouldBeFound("noteToTeacher.equals=" + DEFAULT_NOTE_TO_TEACHER);

        // Get all the reservationList where noteToTeacher equals to UPDATED_NOTE_TO_TEACHER
        defaultReservationShouldNotBeFound("noteToTeacher.equals=" + UPDATED_NOTE_TO_TEACHER);
    }

    @Test
    @Transactional
    public void getAllReservationsByNoteToTeacherIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where noteToTeacher not equals to DEFAULT_NOTE_TO_TEACHER
        defaultReservationShouldNotBeFound("noteToTeacher.notEquals=" + DEFAULT_NOTE_TO_TEACHER);

        // Get all the reservationList where noteToTeacher not equals to UPDATED_NOTE_TO_TEACHER
        defaultReservationShouldBeFound("noteToTeacher.notEquals=" + UPDATED_NOTE_TO_TEACHER);
    }

    @Test
    @Transactional
    public void getAllReservationsByNoteToTeacherIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where noteToTeacher in DEFAULT_NOTE_TO_TEACHER or UPDATED_NOTE_TO_TEACHER
        defaultReservationShouldBeFound("noteToTeacher.in=" + DEFAULT_NOTE_TO_TEACHER + "," + UPDATED_NOTE_TO_TEACHER);

        // Get all the reservationList where noteToTeacher equals to UPDATED_NOTE_TO_TEACHER
        defaultReservationShouldNotBeFound("noteToTeacher.in=" + UPDATED_NOTE_TO_TEACHER);
    }

    @Test
    @Transactional
    public void getAllReservationsByNoteToTeacherIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where noteToTeacher is not null
        defaultReservationShouldBeFound("noteToTeacher.specified=true");

        // Get all the reservationList where noteToTeacher is null
        defaultReservationShouldNotBeFound("noteToTeacher.specified=false");
    }
                @Test
    @Transactional
    public void getAllReservationsByNoteToTeacherContainsSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where noteToTeacher contains DEFAULT_NOTE_TO_TEACHER
        defaultReservationShouldBeFound("noteToTeacher.contains=" + DEFAULT_NOTE_TO_TEACHER);

        // Get all the reservationList where noteToTeacher contains UPDATED_NOTE_TO_TEACHER
        defaultReservationShouldNotBeFound("noteToTeacher.contains=" + UPDATED_NOTE_TO_TEACHER);
    }

    @Test
    @Transactional
    public void getAllReservationsByNoteToTeacherNotContainsSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where noteToTeacher does not contain DEFAULT_NOTE_TO_TEACHER
        defaultReservationShouldNotBeFound("noteToTeacher.doesNotContain=" + DEFAULT_NOTE_TO_TEACHER);

        // Get all the reservationList where noteToTeacher does not contain UPDATED_NOTE_TO_TEACHER
        defaultReservationShouldBeFound("noteToTeacher.doesNotContain=" + UPDATED_NOTE_TO_TEACHER);
    }


    @Test
    @Transactional
    public void getAllReservationsByOriginalClassDateIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where originalClassDate equals to DEFAULT_ORIGINAL_CLASS_DATE
        defaultReservationShouldBeFound("originalClassDate.equals=" + DEFAULT_ORIGINAL_CLASS_DATE);

        // Get all the reservationList where originalClassDate equals to UPDATED_ORIGINAL_CLASS_DATE
        defaultReservationShouldNotBeFound("originalClassDate.equals=" + UPDATED_ORIGINAL_CLASS_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByOriginalClassDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where originalClassDate not equals to DEFAULT_ORIGINAL_CLASS_DATE
        defaultReservationShouldNotBeFound("originalClassDate.notEquals=" + DEFAULT_ORIGINAL_CLASS_DATE);

        // Get all the reservationList where originalClassDate not equals to UPDATED_ORIGINAL_CLASS_DATE
        defaultReservationShouldBeFound("originalClassDate.notEquals=" + UPDATED_ORIGINAL_CLASS_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByOriginalClassDateIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where originalClassDate in DEFAULT_ORIGINAL_CLASS_DATE or UPDATED_ORIGINAL_CLASS_DATE
        defaultReservationShouldBeFound("originalClassDate.in=" + DEFAULT_ORIGINAL_CLASS_DATE + "," + UPDATED_ORIGINAL_CLASS_DATE);

        // Get all the reservationList where originalClassDate equals to UPDATED_ORIGINAL_CLASS_DATE
        defaultReservationShouldNotBeFound("originalClassDate.in=" + UPDATED_ORIGINAL_CLASS_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByOriginalClassDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where originalClassDate is not null
        defaultReservationShouldBeFound("originalClassDate.specified=true");

        // Get all the reservationList where originalClassDate is null
        defaultReservationShouldNotBeFound("originalClassDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllReservationsByOriginalClassDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where originalClassDate is greater than or equal to DEFAULT_ORIGINAL_CLASS_DATE
        defaultReservationShouldBeFound("originalClassDate.greaterThanOrEqual=" + DEFAULT_ORIGINAL_CLASS_DATE);

        // Get all the reservationList where originalClassDate is greater than or equal to UPDATED_ORIGINAL_CLASS_DATE
        defaultReservationShouldNotBeFound("originalClassDate.greaterThanOrEqual=" + UPDATED_ORIGINAL_CLASS_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByOriginalClassDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where originalClassDate is less than or equal to DEFAULT_ORIGINAL_CLASS_DATE
        defaultReservationShouldBeFound("originalClassDate.lessThanOrEqual=" + DEFAULT_ORIGINAL_CLASS_DATE);

        // Get all the reservationList where originalClassDate is less than or equal to SMALLER_ORIGINAL_CLASS_DATE
        defaultReservationShouldNotBeFound("originalClassDate.lessThanOrEqual=" + SMALLER_ORIGINAL_CLASS_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByOriginalClassDateIsLessThanSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where originalClassDate is less than DEFAULT_ORIGINAL_CLASS_DATE
        defaultReservationShouldNotBeFound("originalClassDate.lessThan=" + DEFAULT_ORIGINAL_CLASS_DATE);

        // Get all the reservationList where originalClassDate is less than UPDATED_ORIGINAL_CLASS_DATE
        defaultReservationShouldBeFound("originalClassDate.lessThan=" + UPDATED_ORIGINAL_CLASS_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByOriginalClassDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where originalClassDate is greater than DEFAULT_ORIGINAL_CLASS_DATE
        defaultReservationShouldNotBeFound("originalClassDate.greaterThan=" + DEFAULT_ORIGINAL_CLASS_DATE);

        // Get all the reservationList where originalClassDate is greater than SMALLER_ORIGINAL_CLASS_DATE
        defaultReservationShouldBeFound("originalClassDate.greaterThan=" + SMALLER_ORIGINAL_CLASS_DATE);
    }


    @Test
    @Transactional
    public void getAllReservationsByNewClassDateIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where newClassDate equals to DEFAULT_NEW_CLASS_DATE
        defaultReservationShouldBeFound("newClassDate.equals=" + DEFAULT_NEW_CLASS_DATE);

        // Get all the reservationList where newClassDate equals to UPDATED_NEW_CLASS_DATE
        defaultReservationShouldNotBeFound("newClassDate.equals=" + UPDATED_NEW_CLASS_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByNewClassDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where newClassDate not equals to DEFAULT_NEW_CLASS_DATE
        defaultReservationShouldNotBeFound("newClassDate.notEquals=" + DEFAULT_NEW_CLASS_DATE);

        // Get all the reservationList where newClassDate not equals to UPDATED_NEW_CLASS_DATE
        defaultReservationShouldBeFound("newClassDate.notEquals=" + UPDATED_NEW_CLASS_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByNewClassDateIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where newClassDate in DEFAULT_NEW_CLASS_DATE or UPDATED_NEW_CLASS_DATE
        defaultReservationShouldBeFound("newClassDate.in=" + DEFAULT_NEW_CLASS_DATE + "," + UPDATED_NEW_CLASS_DATE);

        // Get all the reservationList where newClassDate equals to UPDATED_NEW_CLASS_DATE
        defaultReservationShouldNotBeFound("newClassDate.in=" + UPDATED_NEW_CLASS_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByNewClassDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where newClassDate is not null
        defaultReservationShouldBeFound("newClassDate.specified=true");

        // Get all the reservationList where newClassDate is null
        defaultReservationShouldNotBeFound("newClassDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllReservationsByNewClassDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where newClassDate is greater than or equal to DEFAULT_NEW_CLASS_DATE
        defaultReservationShouldBeFound("newClassDate.greaterThanOrEqual=" + DEFAULT_NEW_CLASS_DATE);

        // Get all the reservationList where newClassDate is greater than or equal to UPDATED_NEW_CLASS_DATE
        defaultReservationShouldNotBeFound("newClassDate.greaterThanOrEqual=" + UPDATED_NEW_CLASS_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByNewClassDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where newClassDate is less than or equal to DEFAULT_NEW_CLASS_DATE
        defaultReservationShouldBeFound("newClassDate.lessThanOrEqual=" + DEFAULT_NEW_CLASS_DATE);

        // Get all the reservationList where newClassDate is less than or equal to SMALLER_NEW_CLASS_DATE
        defaultReservationShouldNotBeFound("newClassDate.lessThanOrEqual=" + SMALLER_NEW_CLASS_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByNewClassDateIsLessThanSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where newClassDate is less than DEFAULT_NEW_CLASS_DATE
        defaultReservationShouldNotBeFound("newClassDate.lessThan=" + DEFAULT_NEW_CLASS_DATE);

        // Get all the reservationList where newClassDate is less than UPDATED_NEW_CLASS_DATE
        defaultReservationShouldBeFound("newClassDate.lessThan=" + UPDATED_NEW_CLASS_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByNewClassDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where newClassDate is greater than DEFAULT_NEW_CLASS_DATE
        defaultReservationShouldNotBeFound("newClassDate.greaterThan=" + DEFAULT_NEW_CLASS_DATE);

        // Get all the reservationList where newClassDate is greater than SMALLER_NEW_CLASS_DATE
        defaultReservationShouldBeFound("newClassDate.greaterThan=" + SMALLER_NEW_CLASS_DATE);
    }


    @Test
    @Transactional
    public void getAllReservationsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where requestedBy equals to DEFAULT_REQUESTED_BY
        defaultReservationShouldBeFound("requestedBy.equals=" + DEFAULT_REQUESTED_BY);

        // Get all the reservationList where requestedBy equals to UPDATED_REQUESTED_BY
        defaultReservationShouldNotBeFound("requestedBy.equals=" + UPDATED_REQUESTED_BY);
    }

    @Test
    @Transactional
    public void getAllReservationsByRequestedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where requestedBy not equals to DEFAULT_REQUESTED_BY
        defaultReservationShouldNotBeFound("requestedBy.notEquals=" + DEFAULT_REQUESTED_BY);

        // Get all the reservationList where requestedBy not equals to UPDATED_REQUESTED_BY
        defaultReservationShouldBeFound("requestedBy.notEquals=" + UPDATED_REQUESTED_BY);
    }

    @Test
    @Transactional
    public void getAllReservationsByRequestedByIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where requestedBy in DEFAULT_REQUESTED_BY or UPDATED_REQUESTED_BY
        defaultReservationShouldBeFound("requestedBy.in=" + DEFAULT_REQUESTED_BY + "," + UPDATED_REQUESTED_BY);

        // Get all the reservationList where requestedBy equals to UPDATED_REQUESTED_BY
        defaultReservationShouldNotBeFound("requestedBy.in=" + UPDATED_REQUESTED_BY);
    }

    @Test
    @Transactional
    public void getAllReservationsByRequestedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where requestedBy is not null
        defaultReservationShouldBeFound("requestedBy.specified=true");

        // Get all the reservationList where requestedBy is null
        defaultReservationShouldNotBeFound("requestedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllReservationsByRequestedByContainsSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where requestedBy contains DEFAULT_REQUESTED_BY
        defaultReservationShouldBeFound("requestedBy.contains=" + DEFAULT_REQUESTED_BY);

        // Get all the reservationList where requestedBy contains UPDATED_REQUESTED_BY
        defaultReservationShouldNotBeFound("requestedBy.contains=" + UPDATED_REQUESTED_BY);
    }

    @Test
    @Transactional
    public void getAllReservationsByRequestedByNotContainsSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where requestedBy does not contain DEFAULT_REQUESTED_BY
        defaultReservationShouldNotBeFound("requestedBy.doesNotContain=" + DEFAULT_REQUESTED_BY);

        // Get all the reservationList where requestedBy does not contain UPDATED_REQUESTED_BY
        defaultReservationShouldBeFound("requestedBy.doesNotContain=" + UPDATED_REQUESTED_BY);
    }


    @Test
    @Transactional
    public void getAllReservationsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where createdDate equals to DEFAULT_CREATED_DATE
        defaultReservationShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the reservationList where createdDate equals to UPDATED_CREATED_DATE
        defaultReservationShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultReservationShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the reservationList where createdDate not equals to UPDATED_CREATED_DATE
        defaultReservationShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultReservationShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the reservationList where createdDate equals to UPDATED_CREATED_DATE
        defaultReservationShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where createdDate is not null
        defaultReservationShouldBeFound("createdDate.specified=true");

        // Get all the reservationList where createdDate is null
        defaultReservationShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllReservationsByParticipantsIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);
        User participants = UserResourceIT.createEntity(em);
        em.persist(participants);
        em.flush();
        reservation.addParticipants(participants);
        reservationRepository.saveAndFlush(reservation);
        Long participantsId = participants.getId();

        // Get all the reservationList where participants equals to participantsId
        defaultReservationShouldBeFound("participantsId.equals=" + participantsId);

        // Get all the reservationList where participants equals to participantsId + 1
        defaultReservationShouldNotBeFound("participantsId.equals=" + (participantsId + 1));
    }


    @Test
    @Transactional
    public void getAllReservationsBySchoolGroupIsEqualToSomething() throws Exception {
        // Get already existing entity
        SchoolGroup schoolGroup = reservation.getSchoolGroup();
        reservationRepository.saveAndFlush(reservation);
        Long schoolGroupId = schoolGroup.getId();

        // Get all the reservationList where schoolGroup equals to schoolGroupId
        defaultReservationShouldBeFound("schoolGroupId.equals=" + schoolGroupId);

        // Get all the reservationList where schoolGroup equals to schoolGroupId + 1
        defaultReservationShouldNotBeFound("schoolGroupId.equals=" + (schoolGroupId + 1));
    }


    @Test
    @Transactional
    public void getAllReservationsByBuildingIsEqualToSomething() throws Exception {
        // Get already existing entity
        Building building = reservation.getBuilding();
        reservationRepository.saveAndFlush(reservation);
        Long buildingId = building.getId();

        // Get all the reservationList where building equals to buildingId
        defaultReservationShouldBeFound("buildingId.equals=" + buildingId);

        // Get all the reservationList where building equals to buildingId + 1
        defaultReservationShouldNotBeFound("buildingId.equals=" + (buildingId + 1));
    }


    @Test
    @Transactional
    public void getAllReservationsByClassRoomIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);
        ClassRoom classRoom = ClassRoomResourceIT.createEntity(em);
        em.persist(classRoom);
        em.flush();
        reservation.setClassRoom(classRoom);
        reservationRepository.saveAndFlush(reservation);
        Long classRoomId = classRoom.getId();

        // Get all the reservationList where classRoom equals to classRoomId
        defaultReservationShouldBeFound("classRoomId.equals=" + classRoomId);

        // Get all the reservationList where classRoom equals to classRoomId + 1
        defaultReservationShouldNotBeFound("classRoomId.equals=" + (classRoomId + 1));
    }


    @Test
    @Transactional
    public void getAllReservationsByOriginalStartTimeIsEqualToSomething() throws Exception {
        // Get already existing entity
        ClassHours originalStartTime = reservation.getOriginalStartTime();
        reservationRepository.saveAndFlush(reservation);
        Long originalStartTimeId = originalStartTime.getId();

        // Get all the reservationList where originalStartTime equals to originalStartTimeId
        defaultReservationShouldBeFound("originalStartTimeId.equals=" + originalStartTimeId);

        // Get all the reservationList where originalStartTime equals to originalStartTimeId + 1
        defaultReservationShouldNotBeFound("originalStartTimeId.equals=" + (originalStartTimeId + 1));
    }


    @Test
    @Transactional
    public void getAllReservationsByNewStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);
        ClassHours newStartTime = ClassHoursResourceIT.createEntity(em);
        em.persist(newStartTime);
        em.flush();
        reservation.setNewStartTime(newStartTime);
        reservationRepository.saveAndFlush(reservation);
        Long newStartTimeId = newStartTime.getId();

        // Get all the reservationList where newStartTime equals to newStartTimeId
        defaultReservationShouldBeFound("newStartTimeId.equals=" + newStartTimeId);

        // Get all the reservationList where newStartTime equals to newStartTimeId + 1
        defaultReservationShouldNotBeFound("newStartTimeId.equals=" + (newStartTimeId + 1));
    }


    @Test
    @Transactional
    public void getAllReservationsByClassDurationIsEqualToSomething() throws Exception {
        // Get already existing entity
        ClassDuration classDuration = reservation.getClassDuration();
        reservationRepository.saveAndFlush(reservation);
        Long classDurationId = classDuration.getId();

        // Get all the reservationList where classDuration equals to classDurationId
        defaultReservationShouldBeFound("classDurationId.equals=" + classDurationId);

        // Get all the reservationList where classDuration equals to classDurationId + 1
        defaultReservationShouldNotBeFound("classDurationId.equals=" + (classDurationId + 1));
    }


    @Test
    @Transactional
    public void getAllReservationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);
        Status status = StatusResourceIT.createEntity(em);
        em.persist(status);
        em.flush();
        reservation.setStatus(status);
        reservationRepository.saveAndFlush(reservation);
        Long statusId = status.getId();

        // Get all the reservationList where status equals to statusId
        defaultReservationShouldBeFound("statusId.equals=" + statusId);

        // Get all the reservationList where status equals to statusId + 1
        defaultReservationShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReservationShouldBeFound(String filter) throws Exception {
        restReservationMockMvc.perform(get("/api/reservations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].noteToTeacher").value(hasItem(DEFAULT_NOTE_TO_TEACHER)))
            .andExpect(jsonPath("$.[*].originalClassDate").value(hasItem(DEFAULT_ORIGINAL_CLASS_DATE.toString())))
            .andExpect(jsonPath("$.[*].newClassDate").value(hasItem(DEFAULT_NEW_CLASS_DATE.toString())))
            .andExpect(jsonPath("$.[*].requestedBy").value(hasItem(DEFAULT_REQUESTED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restReservationMockMvc.perform(get("/api/reservations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReservationShouldNotBeFound(String filter) throws Exception {
        restReservationMockMvc.perform(get("/api/reservations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReservationMockMvc.perform(get("/api/reservations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingReservation() throws Exception {
        // Get the reservation
        restReservationMockMvc.perform(get("/api/reservations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReservation() throws Exception {
        // Initialize the database
        reservationService.save(reservation);

        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Update the reservation
        Reservation updatedReservation = reservationRepository.findById(reservation.getId()).get();
        // Disconnect from session so that the updates on updatedReservation are not directly saved in db
        em.detach(updatedReservation);
        updatedReservation
            .name(UPDATED_NAME)
            .noteToTeacher(UPDATED_NOTE_TO_TEACHER)
            .originalClassDate(UPDATED_ORIGINAL_CLASS_DATE)
            .newClassDate(UPDATED_NEW_CLASS_DATE)
            .requestedBy(UPDATED_REQUESTED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restReservationMockMvc.perform(put("/api/reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReservation)))
            .andExpect(status().isOk());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReservation.getNoteToTeacher()).isEqualTo(UPDATED_NOTE_TO_TEACHER);
        assertThat(testReservation.getOriginalClassDate()).isEqualTo(UPDATED_ORIGINAL_CLASS_DATE);
        assertThat(testReservation.getNewClassDate()).isEqualTo(UPDATED_NEW_CLASS_DATE);
        assertThat(testReservation.getRequestedBy()).isEqualTo(UPDATED_REQUESTED_BY);
        assertThat(testReservation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Create the Reservation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservationMockMvc.perform(put("/api/reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservation)))
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReservation() throws Exception {
        // Initialize the database
        reservationService.save(reservation);

        int databaseSizeBeforeDelete = reservationRepository.findAll().size();

        // Delete the reservation
        restReservationMockMvc.perform(delete("/api/reservations/{id}", reservation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reservation.class);
        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        Reservation reservation2 = new Reservation();
        reservation2.setId(reservation1.getId());
        assertThat(reservation1).isEqualTo(reservation2);
        reservation2.setId(2L);
        assertThat(reservation1).isNotEqualTo(reservation2);
        reservation1.setId(null);
        assertThat(reservation1).isNotEqualTo(reservation2);
    }
}
