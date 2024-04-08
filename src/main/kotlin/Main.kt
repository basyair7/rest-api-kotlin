package com.basyair7

import com.basyair7.APIHandler

fun main() {
    val restAPI = APIHandler(8080);
    restAPI.testAPI();
    restAPI.put("id");
}