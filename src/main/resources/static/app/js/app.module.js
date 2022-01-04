var app = angular.module('app', ['ngRoute', 'ngResource', 'common', 'mySocket']);


app.config(function($routeProvider, $locationProvider) {

    $locationProvider.html5Mode(true);

    $routeProvider
        .when("/", {
            template: "<chat></chat>"
        })
        .otherwise({
            redirectTo: "/"
        });
});

app.factory('ChatApi', ($resource) => {
    return $resource("/", {}, {
        create: {
            method: 'POST',
            url: '/chat'
        },
        read: {
            method: 'GET',
            url: '/chat',
            isArray: true
        },
        seen: {
            method: 'POST',
            url: '/chat/seen'
        }
    });
})

app.factory('UserApi', ($resource) => {
    return $resource("/", {}, {
        read: {
            method: 'GET',
            url: '/user',
        },
        getAll: {
            method: 'GET',
            url: '/user/all',
            isArray: true
        },
    });
})