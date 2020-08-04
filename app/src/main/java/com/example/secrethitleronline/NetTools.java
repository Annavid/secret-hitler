package com.example.secrethitleronline;

class NetTools {

    static String getLoginURL() {
        return "/api/v1/users/obtain-token/";
    }

    static String getRegisterURL() {
        return "/api/v1/users/create/";
    }

    static String getCreateGameURL() {
        return "/api/v1/game/create/";
    }

    static String getAvailableGamesList() { return "/api/v1/game/list/"; }

    static String getWebSocketFirstAddress() { return "/ws/game/"; }
}
