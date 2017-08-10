var app = angular.module('timelineApp', ['ngVis']);

        app.controller('timelineCtrl', function($scope, $http, VisDataSet, $location) {

            //primeiro busca os scheduled para fazer os background, depois busca os dias.
            var scheduledMeals = [];
            $http.get("/scheduled-meals").then(function(data){
                scheduledMeals = data.data;
                console.log(scheduledMeals);
                loadAll();
            },  function(error){
                AlertService.error(error.data.message);
                console.log(error)
            });


            function loadAll () {
                $http.get("/meal-log")
                    .then(
                        function(data){
                            console.log(data);
                            buildTimelineInfo(data);
                        },
                        function(error){
                            console.log(error);
                        }
                    );
            }

            function buildTimelineInfo(data) {
                $scope.timelineDatas = [];
                $scope.timelineOptions = {
                      "align": "center",
                       "autoResize": true,
                       "editable": true,
                       "selectable": true,
                       "orientation": "bottom",
                       "showCurrentTime": true,
                       "showMajorLabels": true,
                       "showMinorLabels": true,
                       "height":"800px"
                };

                $scope.timelineData = {};

                var items = new VisDataSet();

                $scope.timelineData.items = extractItens(data.data, items);

                //define os scheduled do dia
                $scope.timelineData.items = extractBackgroundItens(scheduledMeals, new Date(data.data[0].mealDateTime.epochSecond * 1000), $scope.timelineData.items);





                function extractBackgroundItens(scheduledMeals, mealLogDayDate, items){
                    scheduledMeals.forEach(function(item,i){

                        var data = mealLogDayDate;
                        data.setHours(item.targetTime.split(":")[0]);
                        data.setMinutes(item.targetTime.split(":")[1]);



                        var start = new Date(data.getTime() - (20*60*1000) );
                        var end = new Date(data.getTime() + (20*60*1000) );
                                                items.add({
                                                    id: i+items.length,
                                                    content:
                                                    ' <span style="color:#97B0F8;">' +
                                                    item.name +
                                                    '</span>',
                                                    start: start,
                                                    end: end,
                                                    type: 'background'
                                                    });
                    });
                    return items;
                }

                function extractItens(data) {
                    var items = new vis.DataSet();
                    data.forEach(function(item, i){
                        var start = new Date(item.mealDateTime.epochSecond * 1000);
                        items.add({
                            id: i,
                            content:
                            ' <span style="color:#97B0F8;">' +
                            '<a  onClick="window.open(\'data:'+ item.photoContentType + ';base64,' + item.photo + '\')\">' +
                                "<img src=\"data:" + item.photoContentType + ";base64," + item.photo + "\" style=\"max-height: 30px;\" alt=\"mealLog image\"/>" +
                            '</a>' +
                             '</span>',
                            start: start,

                            type: 'box'
                            });
                    })
                    return items;
                }
            }
        });