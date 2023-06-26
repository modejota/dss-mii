// Get the element with id="login_form"
let form = document.getElementById('login_form');
// Add a "submit" event listener to the form
form.addEventListener('submit', function(e) {
    e.preventDefault();
    let username = form.elements[0].value;
    let password = form.elements[1].value;
    doLogin(username, password).then((resp) => {
		let cook = (getCookie('sessionId'))
		if (cook != "") {
            swal({
              title: "¡Éxito!",
              text: "Has iniciado sesión con éxito.",
              icon: "success",
              button: "Aceptar",
            }).then((value) => {
                window.location.href = 'http://localhost:8080/GomezGarciaJoseAlberto-p1/index.html';
            })
        } else {
        	swal({
          	title: "¡Error!",
          	text: "El usuario o la contraseña son incorrectos.",
          	icon: "error",
          	button: "Aceptar",
        	});
    	}
    })
});

async function doLogin(username, password) {
    let data = {
        "username": username,
        "password": password
    }
	await fetch('http://localhost:8080/GomezGarciaJoseAlberto-p1/rest/auth/', {
		method: 'POST',
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(data)
	}).then((result) => {
		console.log("Resultado de la operacion: " + result.status);
	}).catch((err) => {
		console.log("Error: " + err);
	})
}

function getCookie(name) {
	const value = `; ${document.cookie}`;
	const parts = value.split(`; ${name}=`);
	if (parts.length === 2) return parts.pop().split(';').shift();
	else return ""
};