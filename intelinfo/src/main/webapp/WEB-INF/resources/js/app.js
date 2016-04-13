  var myApp = angular.module('myApp', []);


     myApp.directive('fileModel', ['$parse', function ($parse) {
            return {
               restrict: 'A',
               link: function(scope, element, attrs) {
                  var model = $parse(attrs.fileModel);
                  var modelSetter = model.assign;
                  
                  element.bind('change', function(){
                     scope.$apply(function(){
                        modelSetter(scope, element[0].files[0]);
                     });
                  });
               }
            };
         }]);

    myApp.service('multipartForm', ['$http', function ($http) {
            this.post = function(uploadUrl,data){
               for (var key in data)
               var fd = new FormData();
               fd.append(key, data[key]);
            
               $http.post(uploadUrl, fd, {
                  transformRequest: angular.identity,
                  headers: {'Content-Type': undefined}
               })
               .success(function(){

               console.log("Uploaded successfully!");
               })
               .error(function(){
               	console.log("error while uploading");
               });
            }  

         }]);


  
    myApp.controller('myCtrl', ['$scope', 'multipartForm', function($scope, multipartForm){
            
            $scope.Submit = function(){
            	var file=$scope.myFile;
            	console.log("file is "+file);
               var uploadUrl = "uploads/";
               multipartForm.post(uploadUrl,file);
            }


         }]);

  