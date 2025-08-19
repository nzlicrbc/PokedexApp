package com.example.pokedexapp.util

object Constants {

    // API ayarları
    const val POKEMON_SPRITE_BASE_URL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"

    // HTTP ayarları
    const val AUTHORIZATION_HEADER = "Authorization"
    const val BEARER_PREFIX = "Bearer "
    const val CONNECT_TIMEOUT_SECONDS = 30L
    const val READ_TIMEOUT_SECONDS = 30L
    const val WRITE_TIMEOUT_SECONDS = 30L

    // Navigasyon route'ları
    const val ROUTE_POKEMON_LIST = "pokemon_list"
    const val ROUTE_POKEMON_DETAIL = "pokemon_detail"
    const val ROUTE_POKEMON_DETAIL_WITH_ARG = "pokemon_detail/{pokemonId}"
    const val POKEMON_ID_ARG = "pokemonId"

    // Sayfalama ayarları
    const val DEFAULT_POKEMON_LIST_LIMIT = 20
    const val DEFAULT_POKEMON_LIST_OFFSET = 0
    const val INITIAL_PAGE = 0
    const val PAGE_INCREMENT = 1
    const val DEFAULT_SIZE_WHEN_NULL = 0

    // Grid ayarları
    const val POKEMON_GRID_COLUMNS = 2
    const val DEFAULT_LAST_VISIBLE_INDEX = 0
    const val LOAD_MORE_THRESHOLD = 4

    // Pokemon item görünümü
    const val POKEMON_ITEM_ASPECT_RATIO = 1f
    const val POKEMON_ITEM_BACKGROUND_ALPHA = 0.6f
    const val POKEMON_IMAGE_WIDTH_RATIO = 0.75f
    const val POKEMON_IMAGE_ASPECT_RATIO = 1f

    // Pokemon detay ekranı
    const val POKEMON_ID_PADDING = 3
    const val GRADIENT_ALPHA_HIGH = 0.9f
    const val GRADIENT_ALPHA_LOW = 0.6f

    // Stat bar animasyonları
    const val DEFAULT_ANIMATION_TRIGGER = 0
    const val DEFAULT_ANIMATION_DELAY = 0
    const val STAT_ANIMATION_DURATION = 1000
    const val MIN_STAT_PERCENT = 0f
    const val MAX_STAT_PERCENT = 1f
    const val TEXT_VISIBILITY_THRESHOLD = 0.18f
    const val STAT_BAR_WEIGHT = 1f

    // Stat animasyon gecikmeleri
    const val STAT_SHEET_ANIMATION_DELAY = 50L
    const val STAT_HP_ANIMATION_DELAY = 100
    const val STAT_ATTACK_ANIMATION_DELAY = 200
    const val STAT_DEFENSE_ANIMATION_DELAY = 300
    const val STAT_SPEED_ANIMATION_DELAY = 400
    const val STAT_SPECIAL_DEFENSE_ANIMATION_DELAY = 500
    const val STAT_SPECIAL_ATTACK_ANIMATION_DELAY = 600

    // Pokemon stat isimleri
    const val STAT_HP = "hp"
    const val STAT_ATTACK = "attack"
    const val STAT_DEFENSE = "defense"
    const val STAT_SPEED = "speed"
    const val STAT_SPECIAL_ATTACK = "special-attack"
    const val STAT_SPECIAL_DEFENSE = "special-defense"

    // Varsayılan değerler
    const val DEFAULT_STAT_VALUE = 0
    const val DEFAULT_POKEMON_ID = 1
    const val MAX_STAT_VALUE = 300

    // Birim dönüşümleri
    const val WEIGHT_CONVERSION_FACTOR = 10.0
    const val HEIGHT_CONVERSION_FACTOR = 10.0

    // Görsel ayarları
    const val IMAGE_HARDWARE_ACCELERATION_ENABLED = false
}