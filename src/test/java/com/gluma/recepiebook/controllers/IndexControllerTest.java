package com.gluma.recepiebook.controllers;

import com.gluma.recepiebook.domain.Recipe;
import com.gluma.recepiebook.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class IndexControllerTest {

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    IndexController indexController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        indexController=new IndexController(recipeService);
    }

//    ArgumentCaptor is used to capture arguments for mocked methods.
//    In this example, we are mocking the service and telling "when the mock recipeService.getRecipes()
//    is called then the mock should return a Set of recipe".
//    We then created a ArgumentCapture to capture the argument (Set<Recipe>) for the mocked method.
//    Next we verified this statement in the controller method.
//            model.addAttribute("recipes", recipeService.getRecipes());
//    We did it as:
//    verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
//    The precding code can be read as.
//    "Verify that addAttribute has been called on model once with the attribute name
//    recipes" and value Set<Recipe> - note argumentCaptor.capture()
//    in the verify statement above is returning Set<Recipe>"
//    Finally we asserted that the ArgumentCapture object holds a Set with two Recipe objects
//     |
//    \/

    @Test
    public void getIndexPage() {
        //given
        Set<Recipe> recipes = new HashSet<>();
        recipes.add(new Recipe());

        Recipe recipe = new Recipe();
        recipe.setId(123L);

        recipes.add(recipe);

        when(recipeService.getRecipes()).thenReturn(recipes);

        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = indexController.getIndexPage(model);

        //then
        assertEquals("index", viewName);
        verify(recipeService, times(1)).getRecipes();
        verify(model, times(1)).addAttribute(eq("recipes" ), argumentCaptor.capture());

        Set<Recipe> setInController = argumentCaptor.getValue();
        assertEquals(2,setInController.size());
    }

    @Test
    public void testMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
}