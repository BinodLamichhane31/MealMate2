package com.example.mealmate.ui.home

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mealmate.R
import com.example.mealmate.model.Recipe
import com.example.mealmate.viewmodel.RecipeViewModel
import com.google.firebase.auth.FirebaseAuth

class UpdateRecipeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var etRecipeName: EditText
    private lateinit var etIngredients: EditText
    private lateinit var etInstructions: EditText
    private lateinit var etServing: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var btnUpdateRecipe: Button
    private lateinit var progressDialog: ProgressDialog
    private lateinit var back: ImageView



    private val recipeViewModel: RecipeViewModel by viewModels()
    private lateinit var recipe: Recipe // Store the received Recipe object
    private lateinit var categories: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_recipe)

        // Initialize UI elements
        etRecipeName = findViewById(R.id.etRecipeName)
        etIngredients = findViewById(R.id.etIngredients)
        etInstructions = findViewById(R.id.etInstructions)
        etServing = findViewById(R.id.etServing)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        btnUpdateRecipe = findViewById(R.id.btnUpdateRecipe)
        back = findViewById(R.id.urBack)
        back.setOnClickListener{finish()}


        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Updating Recipe...")

        // Define categories for the dropdown
        categories = listOf("Breakfast", "Lunch", "Dinner", "Dessert", "Snacks")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        // Get the Recipe object from Intent
        recipe = intent.getParcelableExtra("recipe")!!

        if (recipe != null) {
            loadRecipeDetails(recipe)
        } else {
            Toast.makeText(this, "Recipe not found!", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnUpdateRecipe.setOnClickListener {
            updateRecipe()
        }
    }

    private fun loadRecipeDetails(recipe: Recipe) {
        etRecipeName.setText(recipe.name)
        etIngredients.setText(recipe.ingredients.joinToString(", ")) // Convert list to comma-separated
        etInstructions.setText(recipe.instructions)
        etServing.setText(recipe.serving)

        // Set the spinner selection based on the current category
        val categoryPosition = categories.indexOf(recipe.category)
        if (categoryPosition >= 0) {
            spinnerCategory.setSelection(categoryPosition)
        }
    }

    private fun updateRecipe() {
        val name = etRecipeName.text.toString().trim()
        val ingredients = etIngredients.text.toString().trim().split(",").map { it.trim() } // Convert input to list
        val instructions = etInstructions.text.toString().trim()
        val serving = etServing.text.toString().trim()
        val category = spinnerCategory.selectedItem.toString()

        if (name.isEmpty() || ingredients.isEmpty() || instructions.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            return
        }

        progressDialog.setMessage("Updating Recipe...")
        progressDialog.show()
        auth = FirebaseAuth.getInstance() // Initialize FirebaseAuth
        val userID = auth.currentUser?.uid.toString()

        val updatedRecipe = Recipe(recipe.id, name, category, ingredients, instructions, serving,userID)

        // Use ViewModel to update the recipe
        recipeViewModel.updateRecipe(updatedRecipe)

        progressDialog.dismiss()
        Toast.makeText(this, "Recipe updated successfully!", Toast.LENGTH_SHORT).show()
        finish()
    }
}
