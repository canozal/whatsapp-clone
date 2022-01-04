angular.module('common', [])
    .factory("UserService", function(){
        let user = {
            id: null,
            username: "",
            imageUrl: "",
            lastSeen: "",
        }

        return {
            getUser,
            setUser
        }


        function getUser() {
            return user;
        }

        function setUser(newUser) {
            user = newUser;
        }
});

angular.module('mySocket', [])
    .factory("SocketService", function($q) {


        let q = $q.defer();

        return {
            connect,
            disconnect,
            subscribe,
            send
        }


        function send(topic, message) {
            q.promise.then(function(stompClient) {
                stompClient.send(topic, {}, JSON.stringify(message));
            });
        }

        function subscribe(topic, callback) {

            q.promise.then(function(stompClient) {
                stompClient.subscribe(topic, callback);
            });

        }


        function connect(){
            let stompClient = null;
            const socket = new SockJS('/wa');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function(frame) {
                console.log('Connected: ' + frame);
                q.resolve(stompClient);
            });
        }


        function disconnect() {

            q.promise.then(function(stompClient) {
                if (stompClient != null) {
                    stompClient.disconnect();
                }
                console.log("Disconnected");
            });

        }

    });