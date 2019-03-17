$(document).ready(function () {

	$('#users-tab').on('click', function (e) {
		e.preventDefault()
		$(this).tab('show')
	});
	$('#connections-tab').on('click', function (e) {
		e.preventDefault()
		$(this).tab('show')
	});
	$('#requests-tab').on('click', function (e) {
		e.preventDefault()
		$(this).tab('show')
	});

	$('.accept-button').on('click', function (e) {
		var id = e.target.id;
		id = id.split("-")[1];
		$.ajax({
			type: 'POST',
			url: 'Connections',
			data: {
				action: 'accept',
				userId: id
			},
			success: function (resText) {
				if (resText === 'success') {
					var divid = '#request-' + id;
					$(divid).hide();
					var count = $("#requestCount").text();
					count = count - 1;
					$("#requestCount").text(count === 0 ? "" : count);
					$("#dropdownRequestCount").text(count === 0 ? "" : count);

					//ako je prihvacen treba se dinamciki user ucitati u tabu konekcija
					$.ajax({
						type: 'POST',
						url: 'User',
						data: {
							action: "getUser",
							userId: id
						},
						success: function (resText) {
							var userObject = resText;
							//dio generisanja templeta
							console.log(userObject);

							var documentFragemnt = document.createDocumentFragment();
							var node = document.querySelector(".conn-template").cloneNode(true);
							console.log(node);
							node.id = 'connection-' + id;
							node.style.display = "flex";
							node.querySelector(".img-conn").src = !userObject.picture || userObject.picture === "" ? "img/user.svg" : userObject.picture;
							node.querySelector(".user-conn").textContent = userObject.name + " " + userObject.surname;
							node.querySelector(".user-conn").href = "Account?action=view&userId=" + id;
							node.querySelector(".remove-button").id = "dec-" + id;
							node.querySelector(".remove-button").onclick = removeUser;
							documentFragemnt.appendChild(node);
							$("#connections").append(documentFragemnt);
						}
					})
				}
			}
		})
	})

	$('.decline-button').on('click', function (e) {
		var id = e.target.id;
		id = id.split("-")[1];
		$.ajax({
			type: 'POST',
			url: 'Connections',
			data: {
				action: 'decline',
				userId: id
			},
			success: function (resText) {
				if (resText === 'success') {
					var divid = '#request-' + id;
					$(divid).hide();
					var count = $("#requestCount").text();
					count = count - 1;
					$("#requestCount").text(count === 0 ? "" : count);
					$("#dropdownRequestCount").text(count === 0 ? "" : count);
				}
			}
		})
	})

	$('.remove-button').on('click', function (e) {
		removeUser(e);
	})

	$(".add-button").on('click', function(e){
		sendRequest(e);
	})
	
	$(".cancel-button").on('click', function(e){
		cancelRequest(e);
	})

	$("#faculty-select").change(function(){
		var facultyId = this.value;
		$.ajax({
			type:'POST',
			url:'User',
			data:{
				action: 'getUsers',
				facultyId: facultyId
			},
			success: function(userList){
				console.log(userList);
				//iterirati i dinamicki popuniti listu usera
				$(".other-user-list").empty();
				var documentFragemnt = document.createDocumentFragment();
				for(var i=0;i<userList.length;i++){
					var user = userList[i];
					var node = document.querySelector(".other-template").cloneNode(true);
					node.id = 'other-' + user.id;
					node.style.display = "flex";
					node.querySelector("#other-img").src = !user.picture || user.picture === ""?"img/user.svg":user.picture;
					node.querySelector("#other-user-link").textContent = user.name + " " + user.surname;
					node.querySelector("#other-user-link").href = "Account?action=view&userId=" + user.id;
					if(user.connectionType == 0){
						node.querySelector(".other-user-btn").id = "request-" + user.id;
						node.querySelector(".other-user-btn").onclick = sendRequest;
						node.querySelector(".other-user-btn").className += " add-button btn btn-secondary";
						node.querySelector(".other-user-btn").innerHTML = "<i class='fas fa-plus'></i>" + " Pošalji zahtjev";
					}else{
						node.querySelector(".other-user-btn").id = "request-" + user.id;
						node.querySelector(".other-user-btn").onclick = cancelRequest;
						node.querySelector(".other-user-btn").className += " cancel-button btn btn-danger";
						node.querySelector(".other-user-btn").innerHTML = "<i class='fas fa-times'></i>" + " Poništi zahtjev";
					}
					documentFragemnt.appendChild(node);
				}
				$(".other-user-list").append(documentFragemnt);
			}
		})
	})
});

function removeUser(e){
	var id = e.target.id;
	id = id.split("-")[1];
	$.ajax({
		type: 'POST',
		url: 'Connections',
		data: {
			action: 'remove',
			userId: id
		},
		success: function (resText) {
			if (resText === 'success') {
				var divid = '#connection-' + id;
				$(divid).hide();
			}
		}
	})
}

function sendRequest(e){
	var id = e.target.id;
	id = id.split("-")[1];
	$.ajax({
		type: 'POST',
		url: 'Connections',
		data: {
			action: 'add',
			userId: id
		},
		success: function (resText) {
			if (resText === 'success') {
				var elementId = "#" + e.target.id;
				//izmijeniti dugme koje salje zahtjev
				$(elementId).removeClass('add-button');
				$(elementId).addClass('cancel-button');
				$(elementId).removeClass('btn-secondary');
				$(elementId).addClass('btn-danger');
				$(elementId).html("<i class='fas fa-times'></i>" + " Poništi zahtjev");
				$(elementId).off('click');
				$(elementId).on('click', function(e){
					cancelRequest(e);
				})
			}
		}
	})
}

function cancelRequest(e){
	var id = e.target.id;
	id = id.split("-")[1];
	$.ajax({
		type: 'POST',
		url: 'Connections',
		data: {
			action: 'cancel',
			userId: id
		},
		success: function (resText) {
			if (resText === 'success') {
				var elementId = "#" + e.target.id;
				//izmijeni dugme koje ponistava zahtjev
				$(elementId).removeClass('cancel-button');
				$(elementId).addClass('add-button');
				$(elementId).removeClass('btn-danger');
				$(elementId).addClass('btn-secondary');
				$(elementId).html("<i class='fas fa-plus'></i>" + " Pošalji zahtjev");
				$(elementId).off('click');
				$(elementId).on('click', function(e){
					sendRequest(e);
				})
			}
		}
	})
}