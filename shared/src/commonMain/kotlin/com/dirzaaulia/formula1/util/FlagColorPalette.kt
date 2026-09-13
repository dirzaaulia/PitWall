package com.dirzaaulia.formula1.util

import androidx.compose.ui.graphics.Color

object FlagColorPalette {

    /**
     * Resolves the primary iconic national flag color for an F1 host nation.
     * Used for atmospheric card gradients, glowing borders, and badges.
     */
    fun getCountryFlagColor(country: String): Color {
        val lower = country.lowercase().trim()
        return when {
            lower.contains("italy") || lower.contains("italia") -> Color(0xFF009246) // Italian Tricolore Emerald
            lower.contains("great britain") || lower.contains("united kingdom") || lower.contains("uk") -> Color(0xFF012169) // Royal Union Blue
            lower.contains("monaco") -> Color(0xFFCE1126) // Monaco Crimson
            lower.contains("netherlands") || lower.contains("dutch") -> Color(0xFFFF5900) // Oranje
            lower.contains("australia") -> Color(0xFF00843D) // Aussie Gold & Green
            lower.contains("spain") -> Color(0xFFAA151B) // Spanish Carmine Red
            lower.contains("belgium") -> Color(0xFFFDDA24) // Belgian Gold
            lower.contains("canada") -> Color(0xFFD80027) // Maple Leaf Red
            lower.contains("united states") || lower.contains("usa") || lower.contains("america") -> Color(0xFF0A3161) // American Deep Navy
            lower.contains("mexico") -> Color(0xFF006847) // Mexican Pine Green
            lower.contains("brazil") -> Color(0xFF009739) // Brazilian Verde
            lower.contains("japan") -> Color(0xFFBC002D) // Hinomaru Crimson Sun
            lower.contains("austria") -> Color(0xFFED2939) // Alpine Red
            lower.contains("hungary") -> Color(0xFF477050) // Magyar Forest Green
            lower.contains("singapore") -> Color(0xFFEE2536) // Marina Bay Crimson
            lower.contains("saudi") -> Color(0xFF006C35) // Saudi Royal Green
            lower.contains("bahrain") -> Color(0xFFCE1126) // Gulf Red
            lower.contains("qatar") -> Color(0xFF8A1538) // Lusail Maroon
            lower.contains("uae") || lower.contains("emirates") || lower.contains("abu dhabi") -> Color(0xFF00732F) // Emirates Green
            lower.contains("azerbaijan") -> Color(0xFF00B5E2) // Caspian Azure
            lower.contains("china") -> Color(0xFFDE2910) // Dragon Red
            lower.contains("france") -> Color(0xFF002395) // French Bleu
            lower.contains("germany") -> Color(0xFFFFCC00) // German Gold
            lower.contains("portugal") -> Color(0xFF006600) // Portuguese Green
            lower.contains("turkey") -> Color(0xFFE30A17) // Turkish Red
            lower.contains("malaysia") -> Color(0xFF010066) // Sepang Blue
            else -> Color(0xFFE10600) // Default F1 Racing Red
        }
    }

    /**
     * Resolves an accent/secondary complementary flag color for dual-tone gradients.
     */
    fun getCountryAccentColor(country: String): Color {
        val lower = country.lowercase().trim()
        return when {
            lower.contains("italy") -> Color(0xFFCE2B37)
            lower.contains("great britain") || lower.contains("united kingdom") -> Color(0xFFC8102E)
            lower.contains("monaco") -> Color(0xFFFFFFFF)
            lower.contains("netherlands") -> Color(0xFF21468B)
            lower.contains("australia") -> Color(0xFFFFCD00)
            lower.contains("spain") -> Color(0xFFF1BF00)
            lower.contains("belgium") -> Color(0xFFEF3340)
            lower.contains("united states") || lower.contains("usa") -> Color(0xFFB31942)
            lower.contains("brazil") -> Color(0xFFFEDD00)
            lower.contains("china") -> Color(0xFFFFDE00)
            lower.contains("azerbaijan") -> Color(0xFF509E2F)
            else -> Color(0x33FFFFFF)
        }
    }
}
