package pl.edu.wat.wel.web.rest;

import pl.edu.wat.wel.SrsApp;
import pl.edu.wat.wel.domain.ClassModel;
import pl.edu.wat.wel.domain.ClassRoom;
import pl.edu.wat.wel.repository.ClassModelRepository;
import pl.edu.wat.wel.service.ClassModelService;
import pl.edu.wat.wel.web.rest.errors.ExceptionTranslator;
import pl.edu.wat.wel.service.dto.ClassModelCriteria;
import pl.edu.wat.wel.service.ClassModelQueryService;

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
 * Integration tests for the {@link ClassModelResource} REST controller.
 */
@SpringBootTest(classes = SrsApp.class)
public class ClassModelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ClassModelRepository classModelRepository;

    @Autowired
    private ClassModelService classModelService;

    @Autowired
    private ClassModelQueryService classModelQueryService;

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

    private MockMvc restClassModelMockMvc;

    private ClassModel classModel;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClassModelResource classModelResource = new ClassModelResource(classModelService, classModelQueryService);
        this.restClassModelMockMvc = MockMvcBuilders.standaloneSetup(classModelResource)
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
    public static ClassModel createEntity(EntityManager em) {
        ClassModel classModel = new ClassModel()
            .name(DEFAULT_NAME);
        return classModel;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassModel createUpdatedEntity(EntityManager em) {
        ClassModel classModel = new ClassModel()
            .name(UPDATED_NAME);
        return classModel;
    }

    @BeforeEach
    public void initTest() {
        classModel = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassModel() throws Exception {
        int databaseSizeBeforeCreate = classModelRepository.findAll().size();

        // Create the ClassModel
        restClassModelMockMvc.perform(post("/api/class-models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classModel)))
            .andExpect(status().isCreated());

        // Validate the ClassModel in the database
        List<ClassModel> classModelList = classModelRepository.findAll();
        assertThat(classModelList).hasSize(databaseSizeBeforeCreate + 1);
        ClassModel testClassModel = classModelList.get(classModelList.size() - 1);
        assertThat(testClassModel.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createClassModelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classModelRepository.findAll().size();

        // Create the ClassModel with an existing ID
        classModel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassModelMockMvc.perform(post("/api/class-models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classModel)))
            .andExpect(status().isBadRequest());

        // Validate the ClassModel in the database
        List<ClassModel> classModelList = classModelRepository.findAll();
        assertThat(classModelList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = classModelRepository.findAll().size();
        // set the field null
        classModel.setName(null);

        // Create the ClassModel, which fails.

        restClassModelMockMvc.perform(post("/api/class-models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classModel)))
            .andExpect(status().isBadRequest());

        List<ClassModel> classModelList = classModelRepository.findAll();
        assertThat(classModelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClassModels() throws Exception {
        // Initialize the database
        classModelRepository.saveAndFlush(classModel);

        // Get all the classModelList
        restClassModelMockMvc.perform(get("/api/class-models?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getClassModel() throws Exception {
        // Initialize the database
        classModelRepository.saveAndFlush(classModel);

        // Get the classModel
        restClassModelMockMvc.perform(get("/api/class-models/{id}", classModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(classModel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getAllClassModelsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        classModelRepository.saveAndFlush(classModel);

        // Get all the classModelList where name equals to DEFAULT_NAME
        defaultClassModelShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the classModelList where name equals to UPDATED_NAME
        defaultClassModelShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllClassModelsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classModelRepository.saveAndFlush(classModel);

        // Get all the classModelList where name not equals to DEFAULT_NAME
        defaultClassModelShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the classModelList where name not equals to UPDATED_NAME
        defaultClassModelShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllClassModelsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        classModelRepository.saveAndFlush(classModel);

        // Get all the classModelList where name in DEFAULT_NAME or UPDATED_NAME
        defaultClassModelShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the classModelList where name equals to UPDATED_NAME
        defaultClassModelShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllClassModelsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        classModelRepository.saveAndFlush(classModel);

        // Get all the classModelList where name is not null
        defaultClassModelShouldBeFound("name.specified=true");

        // Get all the classModelList where name is null
        defaultClassModelShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllClassModelsByNameContainsSomething() throws Exception {
        // Initialize the database
        classModelRepository.saveAndFlush(classModel);

        // Get all the classModelList where name contains DEFAULT_NAME
        defaultClassModelShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the classModelList where name contains UPDATED_NAME
        defaultClassModelShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllClassModelsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        classModelRepository.saveAndFlush(classModel);

        // Get all the classModelList where name does not contain DEFAULT_NAME
        defaultClassModelShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the classModelList where name does not contain UPDATED_NAME
        defaultClassModelShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllClassModelsByClassRoomIsEqualToSomething() throws Exception {
        // Initialize the database
        classModelRepository.saveAndFlush(classModel);
        ClassRoom classRoom = ClassRoomResourceIT.createEntity(em);
        em.persist(classRoom);
        em.flush();
        classModel.addClassRoom(classRoom);
        classModelRepository.saveAndFlush(classModel);
        Long classRoomId = classRoom.getId();

        // Get all the classModelList where classRoom equals to classRoomId
        defaultClassModelShouldBeFound("classRoomId.equals=" + classRoomId);

        // Get all the classModelList where classRoom equals to classRoomId + 1
        defaultClassModelShouldNotBeFound("classRoomId.equals=" + (classRoomId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassModelShouldBeFound(String filter) throws Exception {
        restClassModelMockMvc.perform(get("/api/class-models?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restClassModelMockMvc.perform(get("/api/class-models/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassModelShouldNotBeFound(String filter) throws Exception {
        restClassModelMockMvc.perform(get("/api/class-models?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassModelMockMvc.perform(get("/api/class-models/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingClassModel() throws Exception {
        // Get the classModel
        restClassModelMockMvc.perform(get("/api/class-models/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassModel() throws Exception {
        // Initialize the database
        classModelService.save(classModel);

        int databaseSizeBeforeUpdate = classModelRepository.findAll().size();

        // Update the classModel
        ClassModel updatedClassModel = classModelRepository.findById(classModel.getId()).get();
        // Disconnect from session so that the updates on updatedClassModel are not directly saved in db
        em.detach(updatedClassModel);
        updatedClassModel
            .name(UPDATED_NAME);

        restClassModelMockMvc.perform(put("/api/class-models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedClassModel)))
            .andExpect(status().isOk());

        // Validate the ClassModel in the database
        List<ClassModel> classModelList = classModelRepository.findAll();
        assertThat(classModelList).hasSize(databaseSizeBeforeUpdate);
        ClassModel testClassModel = classModelList.get(classModelList.size() - 1);
        assertThat(testClassModel.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingClassModel() throws Exception {
        int databaseSizeBeforeUpdate = classModelRepository.findAll().size();

        // Create the ClassModel

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassModelMockMvc.perform(put("/api/class-models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classModel)))
            .andExpect(status().isBadRequest());

        // Validate the ClassModel in the database
        List<ClassModel> classModelList = classModelRepository.findAll();
        assertThat(classModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClassModel() throws Exception {
        // Initialize the database
        classModelService.save(classModel);

        int databaseSizeBeforeDelete = classModelRepository.findAll().size();

        // Delete the classModel
        restClassModelMockMvc.perform(delete("/api/class-models/{id}", classModel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassModel> classModelList = classModelRepository.findAll();
        assertThat(classModelList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassModel.class);
        ClassModel classModel1 = new ClassModel();
        classModel1.setId(1L);
        ClassModel classModel2 = new ClassModel();
        classModel2.setId(classModel1.getId());
        assertThat(classModel1).isEqualTo(classModel2);
        classModel2.setId(2L);
        assertThat(classModel1).isNotEqualTo(classModel2);
        classModel1.setId(null);
        assertThat(classModel1).isNotEqualTo(classModel2);
    }
}
