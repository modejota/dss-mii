

window.addEventListener('load', function() {

	getProducto().then((resp) => {
		resp.forEach((v) => {

			let tr = document.createElement('tr');
			tr.classList.add('table_row');
			let td = document.createElement('td');
			td.classList.add('column-1');
			let div = document.createElement('div');
			div.classList.add('how-itemcart1');
			let img = document.createElement('img');
			img.src = 'data:image/jpeg;base64,' + v.imagen;

			let td2 = document.createElement('td');
			td2.classList.add('column-2');
			td2.innerText = v.nombre;

			let td3 = document.createElement('td');
			td3.classList.add('column-3');
			td3.innerText = '$' + v.precio;


			$(div).click(function() {
				swal({
					title: '¿Estás seguro de eliminar este producto?',
					text: 'Esta acción no se puede deshacer',
					icon: 'warning',
					buttons: ['Cancelar', 'Aceptar'],
					dangerMode: true
				}).then((willDelete) => {
					if (willDelete) {
						swal('Acción realizada', 'El producto se ha eliminado con éxito', 'success');
						let trElement = $(this).parent().parent();
						$(trElement).remove();
						removeProducto(v.id);
					} else {
						swal('Acción cancelada', 'No se ha eliminado el producto', 'error');
					}
				});

			});

			div.append(img);
			td.append(div);
			tr.append(td);
			tr.append(td2);
			tr.append(td3);

			$('#eliminate_table').append($(tr));
		})
	});
	let form = document.getElementById('form_enviar');
	form.addEventListener('submit', function(e) {
		e.preventDefault();
		let id = parseInt(form.elements[0].value);
		let nombre = form.elements[1].value;
		let precio = parseFloat(form.elements[2].value);
		let input = document.querySelector('input[type="file"]');
		let imagen = input.files[0]
		if (imagen == undefined)
			imagen = "/9j/4AAQSkZJRgABAQEBLAEsAAD/4QByRXhpZgAASUkqAAgAAAABAA4BAgBQAAAAGgAAAAAAAABBdmFpbGFibGUgaW4gaGlnaC1yZXNvbHV0aW9uIGFuZCBzZXZlcmFsIHNpemVzIHRvIGZpdCB0aGUgbmVlZHMgb2YgeW91ciBwcm9qZWN0Lv/hBXBodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+Cjx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iPgoJPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4KCQk8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczpwaG90b3Nob3A9Imh0dHA6Ly9ucy5hZG9iZS5jb20vcGhvdG9zaG9wLzEuMC8iIHhtbG5zOklwdGM0eG1wQ29yZT0iaHR0cDovL2lwdGMub3JnL3N0ZC9JcHRjNHhtcENvcmUvMS4wL3htbG5zLyIgICB4bWxuczpHZXR0eUltYWdlc0dJRlQ9Imh0dHA6Ly94bXAuZ2V0dHlpbWFnZXMuY29tL2dpZnQvMS4wLyIgeG1sbnM6ZGM9Imh0dHA6Ly9wdXJsLm9yZy9kYy9lbGVtZW50cy8xLjEvIiB4bWxuczpwbHVzPSJodHRwOi8vbnMudXNlcGx1cy5vcmcvbGRmL3htcC8xLjAvIiAgeG1sbnM6aXB0Y0V4dD0iaHR0cDovL2lwdGMub3JnL3N0ZC9JcHRjNHhtcEV4dC8yMDA4LTAyLTI5LyIgeG1sbnM6eG1wUmlnaHRzPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvcmlnaHRzLyIgcGhvdG9zaG9wOkNyZWRpdD0iR2V0dHkgSW1hZ2VzL2lTdG9ja3Bob3RvIiBHZXR0eUltYWdlc0dJRlQ6QXNzZXRJRD0iMTEzODE3OTE4MyIgeG1wUmlnaHRzOldlYlN0YXRlbWVudD0iaHR0cHM6Ly93d3cuaXN0b2NrcGhvdG8uY29tL2xlZ2FsL2xpY2Vuc2UtYWdyZWVtZW50P3V0bV9tZWRpdW09b3JnYW5pYyZhbXA7dXRtX3NvdXJjZT1nb29nbGUmYW1wO3V0bV9jYW1wYWlnbj1pcHRjdXJsIiA+CjxkYzpjcmVhdG9yPjxyZGY6U2VxPjxyZGY6bGk+QmFyYnVsYXQ8L3JkZjpsaT48L3JkZjpTZXE+PC9kYzpjcmVhdG9yPjxkYzpkZXNjcmlwdGlvbj48cmRmOkFsdD48cmRmOmxpIHhtbDpsYW5nPSJ4LWRlZmF1bHQiPkF2YWlsYWJsZSBpbiBoaWdoLXJlc29sdXRpb24gYW5kIHNldmVyYWwgc2l6ZXMgdG8gZml0IHRoZSBuZWVkcyBvZiB5b3VyIHByb2plY3QuPC9yZGY6bGk+PC9yZGY6QWx0PjwvZGM6ZGVzY3JpcHRpb24+CjxwbHVzOkxpY2Vuc29yPjxyZGY6U2VxPjxyZGY6bGkgcmRmOnBhcnNlVHlwZT0nUmVzb3VyY2UnPjxwbHVzOkxpY2Vuc29yVVJMPmh0dHBzOi8vd3d3LmlzdG9ja3Bob3RvLmNvbS9waG90by9saWNlbnNlLWdtMTEzODE3OTE4My0/dXRtX21lZGl1bT1vcmdhbmljJmFtcDt1dG1fc291cmNlPWdvb2dsZSZhbXA7dXRtX2NhbXBhaWduPWlwdGN1cmw8L3BsdXM6TGljZW5zb3JVUkw+PC9yZGY6bGk+PC9yZGY6U2VxPjwvcGx1czpMaWNlbnNvcj4KCQk8L3JkZjpEZXNjcmlwdGlvbj4KCTwvcmRmOlJERj4KPC94OnhtcG1ldGE+Cjw/eHBhY2tldCBlbmQ9InciPz4K/+0AnFBob3Rvc2hvcCAzLjAAOEJJTQQEAAAAAAB/HAJQAAhCYXJidWxhdBwCeABQQXZhaWxhYmxlIGluIGhpZ2gtcmVzb2x1dGlvbiBhbmQgc2V2ZXJhbCBzaXplcyB0byBmaXQgdGhlIG5lZWRzIG9mIHlvdXIgcHJvamVjdC4cAm4AGEdldHR5IEltYWdlcy9pU3RvY2twaG90bwD/2wBDAAoHBwgHBgoICAgLCgoLDhgQDg0NDh0VFhEYIx8lJCIfIiEmKzcvJik0KSEiMEExNDk7Pj4+JS5ESUM8SDc9Pjv/wgALCAJkAmQBAREA/8QAGwABAAIDAQEAAAAAAAAAAAAAAAYHAwQFAQL/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"
		let reader = new FileReader();
		reader.readAsDataURL(imagen);
		reader.onload = function() {
			let base64data = reader.result;
			base64data = base64data.replace(/^data:image\/(png|jpg|jpeg);base64,/, "");
			addProducto(id, nombre, precio, base64data).then((resp) => {
				addElement(id, nombre, precio, base64data);
				form.elements[0].value = "";
				form.elements[1].value = "";
				form.elements[2].value = "";
				input.value = "";
			});
		}

	});

});

function addElement(id, nombre, precio, imagen) {
	let tr = document.createElement("tr");
	tr.classList.add("table_row");
	let column1 = document.createElement("td");
	column1.classList.add("column-1");
	let column2 = document.createElement("td");
	column2.classList.add("column-2");
	let column3 = document.createElement("td");
	column3.classList.add("column-3");
	let div = document.createElement("div");
	div.classList.add("how-itemcart1");
	let img = document.createElement("img");
	img.src = 'data:image/jpeg;base64,' + imagen;
	div.append(img);
	column1.append(div);
	tr.append(column1);
	column2.innerHTML = nombre;
	column3.innerHTML = "$" + precio;
	tr.append(column2);
	tr.append(column3);
	$('#eliminate_table').append($(tr));
}


async function getProducto() {
	let response = await fetch('http://localhost:8080/GomezGarciaJoseAlberto-p1/rest/productos/', {
		method: 'GET',
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		}
	});
	return response.json();
}


async function addProducto(ID, name, price, image) {
	let data = {
		"id": ID,
		"nombre": name,
		"precio": price,
		"imagen": image
	};
	await fetch('http://localhost:8080/GomezGarciaJoseAlberto-p1/rest/productos/', {
		method: 'POST',
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(data)
	}).then((result) => {
		console.log("Resultado de la operacion: " + result);
	}).catch((err) => {
		console.log("Error: " + err);
	})
}


async function removeProducto(itemID) {
	let uri = 'http://localhost:8080/GomezGarciaJoseAlberto-p1/rest/productos/' + itemID;
	let res = encodeURI(uri);

	await fetch(res, {
		method: 'DELETE',
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		}
	}).then((result) => {
		console.log("Resultado de la operacion: " + result);
	}).catch((err) => {
		console.log("Error: " + err);
	})
}


