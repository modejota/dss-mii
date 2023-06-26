window.addEventListener('load', function() {

	let sessionId = getCookie("sessionId")
	let userRole = getCookie("userRole")
	let usuarioID = getCookie("userId")
	let link = document.getElementById("login_ref")
	let parent = link.parentNode;


	if (sessionId != undefined) {
		let newLink = document.createElement('a');
		if (userRole == "admin" || userRole == "ADMIN") {
			newLink.classList.add("flex-c-m", "trans-04", "p-lr-25");
			newLink.id = "admin_ref";
			newLink.href = "http://localhost:8080/GomezGarciaJoseAlberto-p1/admin.html"
			newLink.innerText = "Administrar"
			parent.prepend(newLink)
		}
		link.innerText = "Cerrar sesión"
		link.addEventListener('click', function(event) {
			event.preventDefault();

			doLogout().then((resp) => {
				swal({
					title: "¡Éxito!",
					text: "Has cerrado sesión con éxito.",
					icon: "success",
					button: "Aceptar",
				}).then(() => {
					window.location.href = 'http://localhost:8080/GomezGarciaJoseAlberto-p1/index.html';
				})
			}).catch((err) => {
				swal({
					title: "¡Error!",
					text: "No se ha podido cerrar sesión correctamente.",
					icon: "error",
					button: "Aceptar",
				});
			})
		});
	} else {
		link.innerText = "Iniciar sesión"
		let toBeRemoved = document.getElementById("admin_ref");
		if (toBeRemoved != undefined) {
			toBeRemoved.remove();
		}
	}

	var catalogo;
	var carr;
	
			if (usuarioID != undefined) {
			getCarrito(usuarioID).then((re) => {
				carr = re.productos;
				carr.forEach((item) => {
					let cartIcons = $('.js-show-cart');
					let cartNumber = parseInt($(cartIcons[0]).attr("data-notify"), 10);
					let cart = $('.header-cart-wrapitem');
					let total = $('.header-cart-total');
					cartNumber++;
					$(cartIcons[0]).attr("data-notify", cartNumber);
					$(cartIcons[1]).attr("data-notify", cartNumber);
					let li = document.createElement('li');
					li.classList.add("header-cart-item", "flex-w", "flex-t", "m-b-12");
					let header = document.createElement('div');
					header.classList.add("header-cart-item-img");
					let dupImg = document.createElement('img');
					dupImg.src = 'data:image/jpeg;base64,' + item.imagen;

					$(header).click(function() {
						let liElement = $(this).parent();

						let total = $(this).parent().parent().parent().parent().parent().parent().parent().find('.header-cart-total');
						let cartIcons = $(this).parent().parent().parent().parent().parent().parent().parent().find('.js-show-cart');
						let array = liElement[0].innerText.split("\n");
						let arrayTotal = total[0].innerHTML.split(" ");
						let totalNumber = parseFloat($.trim(arrayTotal[1].replace("$", "")));
						let priceNumber = parseFloat($.trim(array[1].replace("$", "")));
						let newTotalNumber = totalNumber - priceNumber;
						$(liElement).remove();
						total[0].innerHTML = $.trim(arrayTotal[0]) + " $" + newTotalNumber;
						let cartNumber = parseInt($(cartIcons[0]).attr("data-notify"), 10);
						cartNumber--;
						$(cartIcons[0]).attr("data-notify", cartNumber);
						$(cartIcons[1]).attr("data-notify", cartNumber);
						removeProductoToCarrito(usuarioID, item.id);
						// Para eliminar producto no le ponemos notificación via SWAL. Parece más natural.
					});

					header.append(dupImg);
					li.append(header);

					let body = document.createElement('div');
					body.classList.add("header-cart-item-txt", "p-t-8");
					let name = document.createElement('a');
					name.classList.add("header-cart-item-name", "m-b-18", "hov-cl1", "trans-04")
					name.innerText = item.nombre;
					let priceElement = document.createElement('span');
					priceElement.classList.add("header-cart-item-info");
					priceElement.innerText = "$" + parseFloat(item.precio);
					body.append(name);
					body.append(priceElement);
					li.append(body);
					cart.append(li);

					let array = total[0].innerHTML.split(" ")
					let totalNumber = parseFloat($.trim(array[1].replace("$", "")));
					let newTotalNumber = parseFloat(item.precio) + totalNumber;
					newTotalNumber = newTotalNumber.toFixed(2)
					total[0].innerHTML = $.trim(array[0]) + " $" + newTotalNumber;
				})
			})
		}

	getProductos().then((resp) => {
		console.log("Respuesta: " + resp);
		catalogo = resp;
		resp.forEach((v) => {
			let body = document.createElement('div');
			body.classList.add("col-sm-6", "col-md-4", "col-lg-3", "p-b-35", "isotope-item");
			let div2 = document.createElement('div');
			div2.classList.add("block2");
			let divImg = document.createElement('div');
			divImg.classList.add("block2-pic", "hov-img0");
			let img = document.createElement('img');
			img.src = 'data:image/jpeg;base64,' + v.imagen;
			let button = document.createElement('button');
			button.classList.add("block2-btn", "flex-c-m", "stext-103", "cl2", "size-102", "bg0", "bor2", "hov-btn1", "p-lr-15", "trans-04", "js-addcart-detail");
			button.innerText = "Añadir al carrito";
			button.addEventListener('click', function() {
				if (usuarioID != undefined) {
					const item = carr.find(item => item.id === 2);
					addProductoToCarrito(usuarioID, v.id, v.nombre, v.precio, v.imagen).then(() => {
						if (!item) {
							swal({
								title: "¡Éxito!",
								text: "Has añadido el producto al carrito.",
								icon: "success",
								button: "Aceptar",
							}).then(() => {
								window.location.reload()
							});
						} else {
							swal({
								title: "¡Error!",
								text: "El producto ya se encuentra en el carrito.",
								icon: "error",
								button: "Aceptar",
							});
						}
					})

				} else {
					swal({
						title: 'Error',
						text: 'No se ha podido agregar el producto al carrito. ¿Está usted logeado?',
						icon: 'warning',
						button: 'Aceptar',
					})
				}
			})

			let div3 = document.createElement('div');
			div3.classList.add("block2-txt", "flex-w", "flex-t", "p-t-14");

			let div4 = document.createElement('div');
			div4.classList.add("block2-txt-child1", "flex-col-l");

			let name = document.createElement('a');
			name.classList.add("stext-104", "cl4", "hov-cl1", "trans-04", "js-name-b2", "p-b-6");
			name.innerText = v.nombre;

			let price = document.createElement('span');
			price.classList.add("stext-105", "cl3");
			price.innerText = "$" + v.precio;

			let heart = document.createElement('div');
			heart.classList.add("block2-txt-child2", "flex-r", "p-t-3");

			let aElem = document.createElement('a');
			aElem.classList.add("btn-addwish-b2", "dis-block", "pos-relative", "js-addwish-b2");

			let imgHeart1 = document.createElement('img');
			imgHeart1.classList.add("icon-heart1", "dis-block", "trans-04");
			imgHeart1.src = "static/images/icons/icon-heart-01.png";
			let imgHeart2 = document.createElement('img');
			imgHeart2.classList.add("icon-heart2", "dis-block", "trans-04", "ab-t-l");
			imgHeart2.src = "static/images/icons/icon-heart-02.png";

			aElem.append(imgHeart1);
			aElem.append(imgHeart2);

			heart.append(aElem);

			div4.append(name);
			div4.append(price);
			div3.append(div4);
			div3.append(heart);
			body.append(div2);
			div2.append(divImg);
			div2.append(div3);
			divImg.append(img);
			divImg.append(button);

			$('.isotope-grid').append($(body));

		})


		/*==================================================================
   [ Isotope ]*/
		var $topeContainer = $('.isotope-grid');
		var $filter = $('.filter-tope-group');

		// filter items on button click
		$filter.each(function() {
			$filter.on('click', 'button', function() {
				var filterValue = $(this).attr('data-filter');
				$topeContainer.isotope({ filter: filterValue });
			});

		});

		// init Isotope
		$(window).on('load', function() {
			var $grid = $topeContainer.each(function() {
				$(this).isotope({
					itemSelector: '.isotope-item',
					layoutMode: 'fitRows',
					percentPosition: true,
					animationEngine: 'best-available',
					masonry: {
						columnWidth: '.isotope-item'
					}
				});
			});
		});

		var isotopeButton = $('.filter-tope-group button');

		$(isotopeButton).each(function() {
			$(this).on('click', function() {
				for (var i = 0; i < isotopeButton.length; i++) {
					$(isotopeButton[i]).removeClass('how-active1');
				}

				$(this).addClass('how-active1');
			});
		});

		$('.js-addwish-b2, .js-addwish-detail').on('click', function(e) {
			e.preventDefault();
		});

		$('.js-addwish-b2').each(function() {
			var nameProduct = $(this).parent().parent().find('.js-name-b2').html();
			$(this).on('click', function() {
				swal(nameProduct, "is added to wishlist !", "success");

				$(this).addClass('js-addedwish-b2');
				$(this).off('click');
			});
		});

		$('.js-addwish-detail').each(function() {
			var nameProduct = $(this).parent().parent().parent().find('.js-name-detail').html();

			$(this).on('click', function() {
				swal(nameProduct, "is added to wishlist !", "success");

				$(this).addClass('js-addedwish-detail');
				$(this).off('click');
			});
		});

	}).catch((err) => {
		console.log("[getProducts] catch");
		console.log(err);
	})
});

async function getProductos() {
	let response = await fetch('http://localhost:8080/GomezGarciaJoseAlberto-p1/rest/productos/', {
		method: 'GET',
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		}
	});
	return response.json();
};

async function addProductoToCarrito(idc, idp, name, price, image) {
	let item = {
		"id": idp,
		"nombre": name,
		"precio": price,
		"imagen": image
	}
	let uri = 'http://localhost:8080/GomezGarciaJoseAlberto-p1/rest/carritos/' + idc + '/productos';
	let res = encodeURI(uri);
	await fetch(res, {
		method: 'POST',
		credentials: 'include',
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(item)
	}).then((result) => {
		console.log("Resultado de la operacion: " + result);
	}).catch((err) => {
		console.log("Error: " + err);
	})
};

async function getCarrito(id) {
	let uri = 'http://localhost:8080/GomezGarciaJoseAlberto-p1/rest/carritos/' + id;
	let res = encodeURI(uri);
	let response = await fetch(res, {
		method: 'GET',
		credentials: 'include',
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		}
	});
	return response.json();
};

async function removeProductoToCarrito(idc, idp) {
	let uri = 'http://localhost:8080/GomezGarciaJoseAlberto-p1/rest/carritos/' + idc + '/productos/' + idp;
	let res = encodeURI(uri);

	await fetch(res, {
		method: 'DELETE',
		credentials: 'include',
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		}
	}).then((result) => {
		console.log("Resultado de la operacion: " + result);
	}).catch((err) => {
		console.log("Error: " + err);
	})
};

function getCookie(name) {
	const value = `; ${document.cookie}`;
	const parts = value.split(`; ${name}=`);
	if (parts.length === 2) return parts.pop().split(';').shift();
};

async function doLogout() {
	await fetch('http://localhost:8080/GomezGarciaJoseAlberto-p1/rest/auth/', {
		method: 'DELETE',
		credentials: "include",
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
	}).then((result) => {
		console.log("Resultado de la operacion: " + result.status);
	}).catch((err) => {
		console.log("Error: " + err);
	})
}
