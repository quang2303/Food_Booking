function validate() {
    const username = $("#username").val();
    const password = $("#password").val();
    let valid = true;
    if (username === "") {
        $("#username").next().text("Please input account");
        valid = false;
    }
    if (password === "") {
        $("#password").next().text("Please input password");
        valid = false;
    }

    return valid;
}

$("#login-form").submit(function (e) {
    e.preventDefault();

    if (!validate()) {
        return;
    }

    $(".note").empty();
	const username = $("#username").val();
	    const password = $("#password").val();
	
	$.ajax({
		url: "http://localhost:8080/api/auth/login",
		method: "POST",
		data: {
			username: username,
			password: password
		},
		
		success: function() {
			sessionStorage.setItem("login", "1");
			window.location.href = "/admin/home.html";
		},
		error: function(xhr) {
			const response = xhr.responseJSON;
			if(response.error) {
				showToast("error", "Login failed", response.error)
			}
			if(response.other) {
				showToast("other", "Server maintenance", response.error)
			}
		}
	})
});
