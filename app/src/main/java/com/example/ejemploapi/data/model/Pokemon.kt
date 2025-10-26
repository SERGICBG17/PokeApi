package com.example.ejemploapi.data.model
class Pokemon {
    //podría poner el @SerializedName en todos, que sirve para por si no llamas exactamente igual la variable con el campo de la api
    //que coja ese nombre para que no de problemas ejemplo @SerializedName("name") y yo llamo a la variable nombre

    // Nombre del Pokémon
    private lateinit var name: String

    // URL que devuelve la API, normalmente con el id del Pokémon
    private lateinit var url: String

    // Peso del Pokémon
    var weight: Int = 0

    // Altura del Pokémon
    var height: Int = 0

    // Aquí guardamos las imágenes (sprites) que devuelve la API
    var sprites: Sprites? = null

    // Métodos para acceder y modificar el nombre y la URL
    fun getNombre(): String {
        return this.name
    }

    fun getUrl(): String {
        return this.url
    }

    fun setNombre(name: String) {
        this.name = name
    }

    fun setUrl(url: String) {
        this.url = url
    }
}