angular.module("app")
    .component("chat", {
        templateUrl: "/app/template/chat.html", controller($scope, ChatApi, $q, UserApi, UserService, SocketService) {

            // var stompClient = null;
            document.getElementById("myForm").addEventListener("submit", myFunction);
            var objDiv = document.getElementById("scroll");
            moment.locale("tr");
            const privateToggle = document.getElementById('close-private');
            const groupToggle = document.getElementById('close-group');


            $scope.typing = () => {


                SocketService.send("/app/status", {
                    senderName: $scope.account.username,
                    status: "typing",
                    userList: $scope.dumps[$scope.index].users,
                    chatId: $scope.dumps[$scope.index].id
                });
                /*
                stompClient.send("/app/status", {},
                    JSON.stringify({
                        senderName: $scope.account.username, status: "typing",
                        userList: $scope.dumps[$scope.index].users, chatId: $scope.dumps[$scope.index].id
                    }));
                 */
            }

            $scope.timeAgo = (time) => {
                return moment(time).fromNow();
            }

            const toBottom = () => {
                setTimeout(() => {
                    objDiv.scrollTop = objDiv.scrollHeight;
                }, 0);
            }


            function myFunction() {


                const message = {
                    message: $scope.message,
                    time: moment().add(3, 'hours'),
                    sender: UserService.getUser(),
                    chatID: $scope.dumps[$scope.index].id
                };

                SocketService.send("/app/chat", message);
                /*
                stompClient.send("/app/chat", {},
                    JSON.stringify(message));

                 */


                $scope.message = "";

            }


            $scope.changeIndex = function (index) {
                $scope.index = index;
                $scope.allMessages = $scope.dumps[$scope.index].messages;


                ChatApi.seen($scope.dumps[$scope.index], () => {
                    console.log("changeIndex");
                });

                toBottom();

            }


            function sendChat(chat) {


                SocketService.send("/app/chat", chat);
                /*
                stompClient.send("/app/chat", {},
                    JSON.stringify(chat));

                 */
            }

            /*
                        $scope.connect = () => {
                            var socket = new SockJS('/wa');
                            stompClient = Stomp.over(socket);
                            stompClient.debug = false;


                            stompClient.connect({}, function (frame) {
                                    console.log('Connected: ' + frame);


                                    stompClient.subscribe('/topic/seen/' + $scope.account.id, function (message) {
                                        let data = JSON.parse(message.body);
                                        console.log(data);
                                        $scope.dumps = $scope.dumps.map(chat => {
                                            if (chat.id == data.id) {
                                                return data;
                                            }
                                            return chat;
                                        })
                                        for (let i = 0; i < $scope.dumps.length; i++) {
                                            $scope.dumps[i].users = $scope.dumps[i].users.filter(user => user.username !== $scope.account.username);
                                        }
                                        $scope.allMessages = $scope.dumps[$scope.index].messages ?? [];

                                        toBottom();


                                        setTimeout(() => {
                                            $scope.$apply();
                                            toBottom();
                                        }, 0);


                                    });

                                    stompClient.subscribe('/topic/new/' + $scope.account.id, function (message) {
                                        $scope.dumps.push(JSON.parse(message.body));
                                        $scope.$apply();
                                    })

                                    stompClient.subscribe('/topic/status/' + $scope.account.id, function (message) {

                                        let data = JSON.parse(message.body);

                                        if (data.chatId == $scope.dumps[$scope.index].id) {
                                            $scope.placeHolder = data.senderName + " " + data.status;
                                            $scope.$apply();

                                            setTimeout(() => {
                                                $scope.placeHolder = "";
                                                $scope.$apply();
                                            }, 3000);
                                        }


                                    });


                                    stompClient.subscribe('/topic/newuser/', function (message) {
                                        console.log(message);
                                        $scope.allUsers = UserApi.getAll();
                                        $scope.$apply();
                                    });


                                    stompClient.subscribe('/topic/' + $scope.account.id, function (messageOutput) {

                                        $scope.dumps = $scope.dumps.map((dump) => {
                                            if (dump.id == JSON.parse(messageOutput.body).id) {
                                                return JSON.parse(messageOutput.body);
                                            }
                                            return dump;
                                        })

                                        for (let i = 0; i < $scope.dumps.length; i++) {
                                            $scope.dumps[i].users = $scope.dumps[i].users.filter(user => user.username !== $scope.account.username);
                                        }
                                        $scope.allMessages = $scope.dumps[$scope.index].messages ?? [];

                                        let messages = JSON.parse(messageOutput.body).messages;

                                        console.log($scope.account.username, messages[messages.length - 1].sender.username);

                                        if ($scope.dumps[$scope.index].id === JSON.parse(messageOutput.body).id &&
                                            $scope.account.username !== messages[messages.length - 1].sender.username) {
                                            setTimeout(() => {
                                                ChatApi.seen($scope.dumps[$scope.index], () => {
                                                    console.log("göründü");
                                                    toBottom();
                                                });
                                            }, Math.floor(Math.random() * 1000));
                                        }

                                        $scope.$apply();
                                        toBottom();

                                    });
                                },
                                error => {
                                    console.log(error);
                                });
                        }

            */

            const topic = (messageOutput) => {
                $scope.dumps = $scope.dumps.map((dump) => {
                    if (dump.id == JSON.parse(messageOutput.body).id) {
                        return JSON.parse(messageOutput.body);
                    }
                    return dump;
                })

                for (let i = 0; i < $scope.dumps.length; i++) {
                    $scope.dumps[i].users = $scope.dumps[i].users.filter(user => user.username !== $scope.account.username);
                }
                $scope.allMessages = $scope.dumps[$scope.index].messages ?? [];

                let messages = JSON.parse(messageOutput.body).messages;

                console.log($scope.account.username, messages[messages.length - 1].sender.username);

                if ($scope.dumps[$scope.index].id === JSON.parse(messageOutput.body).id && $scope.account.username !== messages[messages.length - 1].sender.username) {
                    setTimeout(() => {
                        ChatApi.seen($scope.dumps[$scope.index], () => {
                            console.log("göründü");
                            toBottom();
                        });
                    }, Math.floor(Math.random() * 1000));
                }

                $scope.$apply();
                toBottom();
            }


            const seen = (message) => {
                let data = JSON.parse(message.body);
                console.log(data);
                $scope.dumps = $scope.dumps.map(chat => {
                    if (chat.id == data.id) {
                        return data;
                    }
                    return chat;
                })
                for (let i = 0; i < $scope.dumps.length; i++) {
                    $scope.dumps[i].users = $scope.dumps[i].users.filter(user => user.username !== $scope.account.username);
                }
                $scope.allMessages = $scope.dumps[$scope.index].messages ?? [];

                toBottom();


                setTimeout(() => {
                    $scope.$apply();
                    toBottom();
                }, 0);

            }

            const news = (message) => {
                $scope.dumps.push(JSON.parse(message.body));
                $scope.$apply();
            }

            const status = (message) => {
                let data = JSON.parse(message.body);

                if (data.chatId == $scope.dumps[$scope.index].id) {
                    $scope.placeHolder = data.senderName + " " + data.status;
                    $scope.$apply();

                    setTimeout(() => {
                        $scope.placeHolder = "";
                        $scope.$apply();
                    }, 3000);
                }
            }

            const newuser = (message) => {
                console.log(message);
                $scope.allUsers = UserApi.getAll();
                $scope.$apply();
            }


            $scope.startGroupChat = (form) => {


                let data = {
                    name: form.name, users: form.users.map(user => {
                        return {
                            username: user
                        }
                    }),
                }
                data["@type"] = "GroupChat";

                ChatApi.create(data, (response) => {


                    if (response.users.length > 1){
                        groupToggle.click();
                        $scope.groupForm = {};
                    }

                });


            }


            $scope.startConversation = () => {

                const user = [];
                user.push({
                    username: $scope.user,
                })

                const data = {
                    users: user, "@type": "PrivateChat",
                }

                console.log(data);

                ChatApi.create(data, (response) => {

                    if (response.users.length > 1) {
                        privateToggle.click();
                        $scope.user = "";
                    }


                });

            }


            $scope.init = async () => {
                $scope.user = "";
                $scope.placeHolder = "";
                $scope.account = UserService.getUser();
                $scope.allUsers = UserApi.getAll();
                $scope.index = 0;
                $scope.message = "";
                UserApi.read((data) => {
                    $scope.account = data;
                    UserService.setUser($scope.account);
                    ChatApi.read((data) => {
                        $scope.dumps = data;
                        for (let i = 0; i < $scope.dumps.length; i++) {
                            $scope.dumps[i].users = $scope.dumps[i].users.filter(user => user.username !== $scope.account.username);
                        }
                        $scope.allMessages = $scope.dumps[$scope.index]?.messages ?? [];

                        // $scope.connect();
                        SocketService.connect();
                        SocketService.subscribe('/topic/seen/' + $scope.account.id, seen);
                        SocketService.subscribe('/topic/new/' + $scope.account.id, news);
                        SocketService.subscribe('/topic/status/' + $scope.account.id, status);
                        SocketService.subscribe('/topic/newuser/', newuser);
                        SocketService.subscribe('/topic/' + $scope.account.id, topic);

                        toBottom();
                        ChatApi.seen($scope.dumps[$scope.index], () => {
                            console.log("sasas");
                        });

                    })
                });


            }
            $scope.init();
        }
    })