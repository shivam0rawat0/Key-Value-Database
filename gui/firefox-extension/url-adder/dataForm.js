document.addEventListener("DOMContentLoaded", function() {
  var myButton = document.getElementById("saveBtn");
  myButton.addEventListener("click", function() {
		const params = new URLSearchParams(window.location.search);
		var key = document.getElementById("key").value;
		var value = localStorage.getItem("myData");
		if(key!="") {
			console.log("["+key+":"+value+"]");
			var postData = JSON.stringify({"key":key,"value":value});
			var xhr = new XMLHttpRequest();
			xhr.open("POST","http://localhost:3040/active/setKey", true);
			xhr.setRequestHeader('Content-Type', 'application/json');
			xhr.onreadystatechange = () => {
				if (xhr.readyState === 4 && xhr.status === 200) {
					alert("Added");
					window.close();
				}
			};
			xhr.send(postData);
		}
	});
});