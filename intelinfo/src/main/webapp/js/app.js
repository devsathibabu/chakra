        'use strict';
        var myApp = angular.module('App', ['angular-svg-round-progressbar']);

        //file-model directive

        myApp.directive('fileModel', [ '$parse', function ($parse) {
            return {
                restrict : 'A',
                link : function(scope, element, attrs) {
                var model = $parse(attrs.fileModel);
                var modelSetter = model.assign;

           element.bind('change', function() {
            scope.$apply(function() {
             modelSetter(scope, element[0].files[0]);
           });
          });
         }
        };
        }]);


        myApp.controller('myCtrl',['$scope','$http','$timeout', '$window',
         'roundProgressService',function($scope,$http,$timeout,$window,roundProgressService) {

           $scope.current1       =         0;
           $scope.current2       =         0;
           $scope.current3       =         0;
           $scope.max            =       100;
           $scope.stroke         =         5;
           $scope.radius         =        50;
           $scope.clockwise      =      true;
           $scope.offset         =        20;
           $scope.currentColor   = '#45ccce';
           $scope.bgColor        = '#eaeaea';
           $scope.animationDelay =         0;


          $scope.getStyle3 = function(){
            var transform = ($scope.isSemi ? '' : 'translateY(-50%) ') + 'translateX(-50%)';
            return {
              'top':'45%',
              'bottom': 'auto',
              'left': '50%',
              'transform': transform,
              '-moz-transform': transform,
              '-webkit-transform': transform,
              'font-size': $scope.radius/3.5 + 'px'
            };
          };


          $scope.showPreciseCurrent3 = function(amount){
            $timeout(function(){
              if(amount <= 0){
                $scope.preciseCurrent3 = $scope.current3;
              }else{
                var math = $window.Math;
                $scope.preciseCurrent3 = math.min(math.round(amount), $scope.max);
              }
            })
          }


          $http.get('json/categories.json').success(function(data) {
            $scope.categories = data;
          });

          $scope.download=function(){

              var category=$scope.inputlist;      
             if(category==null||category==""){
              document.getElementById("down").style="display:block;color:red";
              document.getElementById("down").innerHTML="*Please choose a category";
            }
            else{
              document.getElementById("down").innerHTML="";
              $scope.current1 =50;          
          }
          }

            //upload file

            $scope.uploadFile = function() {
              $scope.current3=0;

               document.getElementById('forup').style="display:inline-block;color:black;font-size:12px";

              console.log(document.getElementById('file-4').value.replace(/^.*\\/, '').split('.'));
              var fileExt=document.getElementById('file-4').value.replace(/^.*\\/, '').split('.');

              if($scope.myFile==null) {
               document.getElementById("myfile").innerHTML="*Please choose a file to proceed";
             }
             else{
               document.getElementById("myfile").innerHTML="";

               if(fileExt[1]!="xls") {
                document.getElementById("myfile").innerHTML="*Please select xls file only";
              }

              else {
                 $scope.current3=50;
               
                console.log("inside submit");
                var file = $scope.myFile;
                console.log('file is ' + JSON.stringify(file));
                var uploadUrl = "http://127.0.0.1:8080/intelinfo/uploads.htm";
                    var fd = new FormData();
                    fd.append('file', file);
                    $http.post(uploadUrl, fd, {
                      transformRequest : angular.identity,
                      headers : {
                        'Content-Type' : undefined
                      }
                    }).success(function() {
                      console.log("success");
                      $scope.current3=100;
                      setTimeout(function() {
                document.getElementById('forup').style="display:none";
                $scope.current3=0;
               }, 2000);
                    }).error(function(e) {
                      console.log(e);
                         console.log(e);
                document.getElementById('forup').style="color:red;font-size:12px";
                document.getElementById('forup').innerHTML="Something went wrong with file upload.Please try again!";
                setTimeout(function() {
                  document.getElementById('forup').style="display:none";
                $scope.current3=0;
               }, 3000);     
             $scope.current3=0;
                        
                    });
                  }
                }
              };


              //download template

           $scope.templateDownload=function(){
            
             var category=$scope.inputlist;
             if(category==null||category==""){
              document.getElementById("temp").style="display:block;color:red";
              document.getElementById("temp").innerHTML="*Please choose a category";
            }
            else{
              document.getElementById("temp").innerHTML="";
             console.log(category);

           }
           };         

         }]);







