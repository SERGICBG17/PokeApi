package com.example.ejemploapi.data.model
class Pokemon {
    //podría poner el @SerializedName en todos, que sirve para por si no llamas exactamente igual la variable con el campo de la api
    //que coja ese nombre para que no de problemas ejemplo @SerializedName("name") y yo llamo a la variable nombre

    // Nombre del Pokémon
    lateinit var name: String

    // URL que devuelve la API, normalmente con el id del Pokémon
    lateinit var url: String

}