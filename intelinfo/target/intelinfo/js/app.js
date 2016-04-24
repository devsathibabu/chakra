var myApp= angular.module('App',[]);

//this controller is for file upload

    myApp.directive('fileModel', [ '$parse', function($parse) {
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


    myApp.service('fileUpload', ['$http', function($http) {
	    this.uploadFileToUrl = function(uploadUrl, file) {
		    var fd = new FormData();
		    fd.append('file', file);
		    $http.post(uploadUrl, fd, {
			    transformRequest : angular.identity,
			    headers : {
				'Content-Type' : undefined
			    }
		    }).success(function() {
			    console.log("success");
		    }).error(function() {
		    	console.log("error");
		    });
	    }
    }]);


    myApp.controller('uploadCtrl', [ '$scope', 'fileUpload',function($scope, fileUpload) {
		
		$scope.uploadFile = function() {
             
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
                    console.log("inside submit");
				    var file = $scope.myFile;
				    console.log('file is ' + JSON.stringify(file));
				    var uploadUrl = "http://192.168.1.161:8080/intelinfo/uploads.htm";
				    fileUpload.uploadFileToUrl(uploadUrl, file);
			    }
			}
		};
	}]);

   
//this controller is for the file download

    myApp.service('fileDownload',['$http',function($http) {

    	this.downloadFilefromUrl=function(downloadUrl,category) {
		      var fd = new FormData();
		      fd.append('category',category);
		      $http.post(downloadUrl,fd,{
			    transformRequest : angular.identity,
			    headers : {
				'Content-Type' : undefined
			    }
		    }).success(function() {
			   console.log("success balle balle");
		    }).error(function() {
               console.log("error");
		   });

        }
   }]);


    myApp.controller('myCtrl',['$scope','$http','fileDownload',function($scope,$http,fileDownload) {
         
        $http.get('json/categories.json').success(function(data) {
            $scope.categories = data;
        });
    		
    	$scope.download=function(){
	            var category=$scope.inputlist;
    		    console.log(category);
    		    var downloadUrl="http://192.168.1.161:8080/intelinfo/download.htm";
    		    fileDownload.downloadFilefromUrl(downloadUrl,category);
    	};    		
    }]);

//this controller is for template download

    myApp.service('templateDownload',['$http',function($http){

    	this.downloadtemplatefromUrl=function(templateUrl,category){
		    var fd = new FormData();
		    fd.append('category', category);
		    $http.post(templateUrl, fd, {
				    transformRequest : angular.identity,
				    headers : {
					'Content-Type' : undefined
				    }
			}).success(function() {
			    console.log("success balle balle");
		    }).error(function() {
		    	console.log("error");
	    	});
    	}
    }]);


    myApp.controller('templateCtrl',['$scope','$http','templateDownload',function($scope,$http,templateDownload){
         
        $http.get('json/categories.json').success(function(data) {
            $scope.categories = data;
        });
    	
    	$scope.tempDownload=function(){
    		
    	        var category=$scope.inputlist;
    	    	console.log(category);
    		    var templateUrl="http://192.168.1.161:8080/intelinfo/template.htm";
    		    templateDownload.downloadtemplatefromUrl(templateUrl,category);
    	};

    }]);

      
      

    