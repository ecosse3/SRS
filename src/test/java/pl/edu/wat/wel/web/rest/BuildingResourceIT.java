package pl.edu.wat.wel.web.rest;

import pl.edu.wat.wel.SrsApp;
import pl.edu.wat.wel.domain.Building;
import pl.edu.wat.wel.domain.ClassRoom;
import pl.edu.wat.wel.domain.Reservation;
import pl.edu.wat.wel.repository.BuildingRepository;
import pl.edu.wat.wel.service.BuildingService;
import pl.edu.wat.wel.web.rest.errors.ExceptionTranslator;
import pl.edu.wat.wel.service.dto.BuildingCriteria;
import pl.edu.wat.wel.service.BuildingQueryService;

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
 * Integration tests for the {@link BuildingResource} REST controller.
 */
@SpringBootTest(classes = SrsApp.class)
public class BuildingResourceIT {

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private BuildingQueryService buildingQueryService;

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

    private MockMvc restBuildingMockMvc;

    private Building building;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BuildingResource buildingResource = new BuildingResource(buildingService, buildingQueryService);
        this.restBuildingMockMvc = MockMvcBuilders.standaloneSetup(buildingResource)
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
    public static Building createEntity(EntityManager em) {
        Building building = new Building()
            .number(DEFAULT_NUMBER);
        return building;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Building createUpdatedEntity(EntityManager em) {
        Building building = new Building()
            .number(UPDATED_NUMBER);
        return building;
    }

    @BeforeEach
    public void initTest() {
        building = createEntity(em);
    }

    @Test
    @Transactional
    public void createBuilding() throws Exception {
        int databaseSizeBeforeCreate = buildingRepository.findAll().size();

        // Create the Building
        restBuildingMockMvc.perform(post("/api/buildings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(building)))
            .andExpect(status().isCreated());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeCreate + 1);
        Building testBuilding = buildingList.get(buildingList.size() - 1);
        assertThat(testBuilding.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    @Transactional
    public void createBuildingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = buildingRepository.findAll().size();

        // Create the Building with an existing ID
        building.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBuildingMockMvc.perform(post("/api/buildings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(building)))
            .andExpect(status().isBadRequest());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = buildingRepository.findAll().size();
        // set the field null
        building.setNumber(null);

        // Create the Building, which fails.

        restBuildingMockMvc.perform(post("/api/buildings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(building)))
            .andExpect(status().isBadRequest());

        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBuildings() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList
        restBuildingMockMvc.perform(get("/api/buildings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(building.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getBuilding() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get the building
        restBuildingMockMvc.perform(get("/api/buildings/{id}", building.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(building.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER));
    }

    @Test
    @Transactional
    public void getAllBuildingsByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where number equals to DEFAULT_NUMBER
        defaultBuildingShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the buildingList where number equals to UPDATED_NUMBER
        defaultBuildingShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllBuildingsByNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where number not equals to DEFAULT_NUMBER
        defaultBuildingShouldNotBeFound("number.notEquals=" + DEFAULT_NUMBER);

        // Get all the buildingList where number not equals to UPDATED_NUMBER
        defaultBuildingShouldBeFound("number.notEquals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllBuildingsByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultBuildingShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the buildingList where number equals to UPDATED_NUMBER
        defaultBuildingShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllBuildingsByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where number is not null
        defaultBuildingShouldBeFound("number.specified=true");

        // Get all the buildingList where number is null
        defaultBuildingShouldNotBeFound("number.specified=false");
    }
                @Test
    @Transactional
    public void getAllBuildingsByNumberContainsSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where number contains DEFAULT_NUMBER
        defaultBuildingShouldBeFound("number.contains=" + DEFAULT_NUMBER);

        // Get all the buildingList where number contains UPDATED_NUMBER
        defaultBuildingShouldNotBeFound("number.contains=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllBuildingsByNumberNotContainsSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where number does not contain DEFAULT_NUMBER
        defaultBuildingShouldNotBeFound("number.doesNotContain=" + DEFAULT_NUMBER);

        // Get all the buildingList where number does not contain UPDATED_NUMBER
        defaultBuildingShouldBeFound("number.doesNotContain=" + UPDATED_NUMBER);
    }


    @Test
    @Transactional
    public void getAllBuildingsByClassRoomBIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);
        ClassRoom classRoomB = ClassRoomResourceIT.createEntity(em);
        em.persist(classRoomB);
        em.flush();
        building.addClassRoomB(classRoomB);
        buildingRepository.saveAndFlush(building);
        Long classRoomBId = classRoomB.getId();

        // Get all the buildingList where classRoomB equals to classRoomBId
        defaultBuildingShouldBeFound("classRoomBId.equals=" + classRoomBId);

        // Get all the buildingList where classRoomB equals to classRoomBId + 1
        defaultBuildingShouldNotBeFound("classRoomBId.equals=" + (classRoomBId + 1));
    }


    @Test
    @Transactional
    public void getAllBuildingsByReservationBIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);
        Reservation reservationB = ReservationResourceIT.createEntity(em);
        em.persist(reservationB);
        em.flush();
        building.addReservationB(reservationB);
        buildingRepository.saveAndFlush(building);
        Long reservationBId = reservationB.getId();

        // Get all the buildingList where reservationB equals to reservationBId
        defaultBuildingShouldBeFound("reservationBId.equals=" + reservationBId);

        // Get all the buildingList where reservationB equals to reservationBId + 1
        defaultBuildingShouldNotBeFound("reservationBId.equals=" + (reservationBId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBuildingShouldBeFound(String filter) throws Exception {
        restBuildingMockMvc.perform(get("/api/buildings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(building.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)));

        // Check, that the count call also returns 1
        restBuildingMockMvc.perform(get("/api/buildings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBuildingShouldNotBeFound(String filter) throws Exception {
        restBuildingMockMvc.perform(get("/api/buildings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBuildingMockMvc.perform(get("/api/buildings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingBuilding() throws Exception {
        // Get the building
        restBuildingMockMvc.perform(get("/api/buildings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBuilding() throws Exception {
        // Initialize the database
        buildingService.save(building);

        int databaseSizeBeforeUpdate = buildingRepository.findAll().size();

        // Update the building
        Building updatedBuilding = buildingRepository.findById(building.getId()).get();
        // Disconnect from session so that the updates on updatedBuilding are not directly saved in db
        em.detach(updatedBuilding);
        updatedBuilding
            .number(UPDATED_NUMBER);

        restBuildingMockMvc.perform(put("/api/buildings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBuilding)))
            .andExpect(status().isOk());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeUpdate);
        Building testBuilding = buildingList.get(buildingList.size() - 1);
        assertThat(testBuilding.getNumber()).isEqualTo(UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingBuilding() throws Exception {
        int databaseSizeBeforeUpdate = buildingRepository.findAll().size();

        // Create the Building

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBuildingMockMvc.perform(put("/api/buildings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(building)))
            .andExpect(status().isBadRequest());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBuilding() throws Exception {
        // Initialize the database
        buildingService.save(building);

        int databaseSizeBeforeDelete = buildingRepository.findAll().size();

        // Delete the building
        restBuildingMockMvc.perform(delete("/api/buildings/{id}", building.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Building.class);
        Building building1 = new Building();
        building1.setId(1L);
        Building building2 = new Building();
        building2.setId(building1.getId());
        assertThat(building1).isEqualTo(building2);
        building2.setId(2L);
        assertThat(building1).isNotEqualTo(building2);
        building1.setId(null);
        assertThat(building1).isNotEqualTo(building2);
    }
}
