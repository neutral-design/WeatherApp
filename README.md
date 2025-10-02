# WeatherApp ☀️🌧️

En Android-app byggd med **Jetpack Compose** som hämtar väderdata från **SMHI:s API**.  
Appen låter användaren söka efter platser eller använda telefonens GPS för att visa aktuell väderinformation.

## Funktioner
- 🔍 Sök efter platsnamn och hämta väderdata
- 📍 Hämta koordinater via telefonens plats (med runtime permissions)
- ⭐ Lägg till favoritplatser
- 🗺️ Visa väder för valda koordinater

## Tekniker och bibliotek
- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Android Navigation](https://developer.android.com/guide/navigation)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) för Dependency Injection
- [SMHI Open Data API](https://opendata.smhi.se/apidocs/metfcst/index.html) för väderprognoser

## Behörigheter
Appen använder följande Android-permission:
- `ACCESS_FINE_LOCATION` – för att hämta användarens aktuella plats

## Installation
1. Klona repot  
   ```bash
   git clone https://github.com/<ditt-användarnamn>/weatherapp.git
