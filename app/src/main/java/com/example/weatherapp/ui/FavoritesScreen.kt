import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.viewmodel.WeatherViewModel

@Composable
fun FavoritesScreen(
    onPlaceSelected: (Float, Float) -> Unit,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val favorites by viewModel.favorites.observeAsState(emptyList())

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Your Favorites",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(favorites) { place ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${place.place}, ${place.county}, ${place.country}",
                        modifier = Modifier
                            .weight(1f) // Tar upp utrymmet till vänster om ikonen
                            .clickable {
                                onPlaceSelected(place.lat.toFloat(), place.lon.toFloat())
                            }
                    )

                    // Stjärnikonen för att ta bort favoriten
                    Icon(
                        imageVector = Icons.Default.Star, // Fylld stjärna för favoriter
                        contentDescription = "Remove from favorites",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                viewModel.removeFavorite(place.geonameid)
                            }
                    )
                }
            }
        }
    }
}
