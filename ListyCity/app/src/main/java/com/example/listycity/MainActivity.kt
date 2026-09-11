package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val cityRepository = CityRepository()

        setContent {
            ListyCityTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = {
                            cityRepository.addCity(it)
                        },
                        onDeleteCity = {
                            cityRepository.deleteCity(it)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
class CityRepository {
    private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver", "Moscow", "Sydney", "Berlin",
        "Vienna", "Tokyo", "Beijing", "Osaka", "New Delhi"
    )
    val cities: List<String>
        get() = _cities
    fun addCity(city: String) {
        _cities.add(city)
    }
    fun deleteCity(city: String) {
        _cities.remove(city)
    }
}
@Composable
fun CityListScreen(
    cities: List<String>,
    onAddCity: (String) -> Unit,
    onDeleteCity: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var newCityName by remember {
        mutableStateOf("")
    }
    var selectedCity by remember {
        mutableStateOf<String?>(null)
    }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            OutlinedTextField(
                value = newCityName,
                onValueChange = {
                    newCityName = it
                },
                label = {
                    Text("City name")
                },
                modifier = Modifier.weight(1f)
            )
            Spacer(
                modifier = Modifier.width(8.dp)
            )
            Button(
                onClick = {
                    if (newCityName.isNotBlank()) {
                        onAddCity(newCityName)
                        newCityName = ""
                    }
                }
            ) {
                Text("Add City")
            }
        }
        Button(
            onClick = {
                selectedCity?.let {
                    onDeleteCity(it)
                    selectedCity = null
                }
            },
            enabled = selectedCity != null,
            modifier = Modifier.padding(
                horizontal = 16.dp
            )
        ) {
            Text("Delete City")
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(cities) { city ->
                CityRow(
                    city = city,
                    selected = city == selectedCity,
                    onClick = {
                        selectedCity = city
                    }
                )
            }
        }
    }
}
@Composable
fun CityRow(
    city: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = if (selected) {
            "✓ $city"
        } else {
            city
        },
        fontSize = 28.sp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 18.dp,
                vertical = 14.dp
            )
    )
}