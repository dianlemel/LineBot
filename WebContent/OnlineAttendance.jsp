<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>線上課程狀況頁面</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="Expires" content="-1">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-control" content="no-cache">
    <meta http-equiv="Cache" content="no-cache">
    <meta charset="UTF-8">
    <script type='text/javascript' src="./js/angular.js"></script>
    <style>
        html {
            font-size: 11px;
        }

        table {
            width: 100%;
            table-layout: fixed;
            word-break: break-all;
        }

        th {
            text-align: center;
        }

        table, th, td {
            border: 1px solid black;
        }

    </style>
    <script type='text/javascript'>
        const app = angular.module('myApp', []);
        app.factory('$socket', function ($rootScope) {
            let socket = new WebSocket("wss://<%= line.Main.SERVER_IP %>/OnlineAttendance");
            socket.onopen = function () {
                socket.send('<%= request.getAttribute("Key") %>');
            };
            return {
                onClose: function (callback) {
                    socket.onclose = function () {
                        var args = arguments;
                        $rootScope.$apply(function () {
                            callback.apply(socket, args);
                        });
                    };
                },
                onMessage: function (callback) {
                    socket.onmessage = function () {
                        var args = arguments;
                        $rootScope.$apply(function () {
                            callback.apply(socket, args);
                        });
                    };
                }
            };
        });
        app.controller('myCtrl', function ($scope, $http, $socket) {
            class Member {

                #ID;
                #NAME;
                #ACCOUNT;

                constructor(id, name, account, check) {
                    this.#ID = id;
                    this.#NAME = name;
                    this.#ACCOUNT = account;
                    this.Check = check;
                }

                get Id() {
                    return this.#ID;
                }

                get Name() {
                    return this.#NAME;
                }

                get Account() {
                    return this.#ACCOUNT;
                }

            }

            $scope.getChechLength = function () {
                return $scope.members.filter(function (m) {
                    return m.Check;
                }).length;
            }
            $socket.onMessage(function (event) {
                const data = JSON.parse(event.data);
                console.log(data);
                switch (data.TYPE) {
                    case "STATUS":
                        $scope.status = data.DATA;
                        break;
                    case "CHECK":
                        const id = data.DATA;
                        const check = data.DATA2;
                        $scope.members.find(member => member.Id == id).Check = check;
                        break;
                    case "ADD":
                        const member = data.DATA;
                        $scope.members.push(new Member(member.ID, member.NAME, member.ACCOUNT, member.CHECK));
                        break;
                }
            });
            $socket.onClose(function () {
                alert('與伺服器連線中斷');
            });
            $scope.sortSwitch = function () {
                let sortF;
                switch ($scope.sortType) {
                    case "1":
                        sortF = function (m1, m2) {
                            return m1.Id - m2.Id;
                        };
                        break;
                    case "2":
                        sortF = function (m1, m2) {
                            return m1.Account - m2.Account;
                        };
                        break;
                    case "3":
                        sortF = function (m1, m2) {
                            const m1c = m1.Check ? 1 : 0;
                            const m2c = m2.Check ? 1 : 0;
                            return m1c - m2c;
                        };
                        break;
                    case "4":
                        sortF = function (m1, m2) {
                            const m1c = m1.Check ? 1 : 0;
                            const m2c = m2.Check ? 1 : 0;
                            return m2c - m1c;
                        };
                        break;
                }
                if (sortF) {
                    $scope.members.sort(sortF);
                }
            };
            $scope.members = [];
            $scope.sortType = "1";
            $scope.status = 0;
            $scope.classroom = "未知";
            $scope.name = "未知";
            $scope.date = "未知";
            var json = '<%= request.getAttribute("Data")%>';
            if(json){
                var data = JSON.parse(json);
                data.MEMBER.map(member => new Member(member.ID, member.NAME, member.ACCOUNT, member.CHECK)).forEach(function (member) {
                    $scope.members.push(member);
                });
                $scope.status = data.STATUS;
                $scope.classroom = data.CLASSROOM;
                $scope.name = data.NAME;
                $scope.date = data.DATE;
            }
            $scope.getStatusName = function(){
                switch($scope.status){
                    case 0:
                        return "課程尚未開始";
                    case 1:
                        return "課程進行中";
                    case 2:
                        return "課程已結束";
                }
            }
        });
    </script>
</head>
<body>
<div ng-app="myApp" ng-controller="myCtrl" style="max-width: 100%; text-align: center;">
    <table>
        <tr>
            <th colspan="4" style="background: #cccccc;">課程名稱</th>
        </tr>
        <tr>
            <th colspan="4">{{name}}</th>
        </tr>
        <tr>
            <th colspan="2" style="background: #cccccc;">課程教室</th>
            <th colspan="2">{{classroom}}</th>
        </tr>
        <tr>
            <th colspan="2" style="background: #cccccc;">狀態</th>
            <th colspan="2">{{getStatusName()}}</th>
        </tr>
        <tr>
            <th colspan="4" style="background: #cccccc;">時間</th>
        </tr>
        <tr>
            <th colspan="4">{{date}}</th>
        </tr>
        <tr>
            <th style="background: #cccccc;">總人數</th>
            <th>{{members.length}}</th>
            <th style="background: #cccccc;">到課人數</th>
            <th>{{getChechLength()}}</th>
        </tr>
        <tr>
            <th colspan="4" style="background: #cccccc;">學生清單</th>
        </tr>
        <tr>
            <th colspan="4">
                <div style="width: 100%; height: 500px; overflow: auto;">
                    <table ng-repeat="member in members"
                           style="background: {{member.Check ? '#96f226' : '#cccccc'}}; margin-bottom: 10px;">
                        <tr>
                            <th colspan="1">姓名</th>
                            <th colspan="5">{{member.Name}}</th>
                        </tr>
                        <tr>
                            <th colspan="1">ID</th>
                            <th colspan="5">{{member.Id}}</th>
                        </tr>
                        <tr>
                            <th colspan="1">帳號</th>
                            <th colspan="5">{{member.Account}}</th>
                        </tr>
                    </table>
                </div>
            </th>
        </tr>
        <tr>
            <th colspan="2" style="background: #cccccc;">順序調整</th>
            <th colspan="2">
                <select ng-model="sortType" ng-switch="sortSwitch()" style="width: 100%; border: 0">
                    <option value="1">按照ID排序</option>
                    <option value="2">按照帳號排序</option>
                    <option value="3">尚未到課排到最前面</option>
                    <option value="4">已到課排到最前面</option>
                </select>
            </th>
        </tr>
    </table>
</div>
</body>
</html>