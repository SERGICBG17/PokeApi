package com.example.ejemploapi

class Pokemon {
    private lateinit var name: String
    private lateinit var url: String

    fun getNombre() : String{
        return this.name
    }
    fun getUrl() : String {
        return this.url
    }
    fun setNombre(name : String){
        this.name = name
    }
    fun setUrl(url : String){
        this.url = url
    }

}