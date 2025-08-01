const address = [
	{
		nameCity: "Hồ Chí Minh",
		ward: [
			{
				nameWard: "Sài Gòn",
				feeShip: 45000
			},
			{
				nameWard: "Tân Định",
				feeShip: 35000
			},
			{
				nameWard: "Bến Thành",
				feeShip: 15000
			},
			{
				nameWard: "Ông Lãnh",
				feeShip: 55000
			},
			{
				nameWard: "Tăng Nhơn Phú",
				feeShip: 35000
			},
		]
	},
	{
		nameCity: "Đồng Nai",
		ward: [
			{
				nameWard: "Trảng Bom",
				feeShip: 65000
			},
			{
				nameWard: "Bàu Hàm",
				feeShip: 55000
			},
			{
				nameWard: "Hưng Thịnh",
				feeShip: 45000
			},
			{
				nameWard: "Thống Nhất",
				feeShip: 35000
			},
			{
				nameWard: "Định Quán",
				feeShip: 15000
			},
		]
	},
	{
		nameCity: "Hà Nội",
		ward: [
			{
				nameWard: "Hàng Mã",
				feeShip: 20000
			},
			{
				nameWard: "Hàng Bồ",
				feeShip: 15000
			},
			{
				nameWard: "Hàng Đào",
				feeShip: 25000
			},
			{
				nameWard: "Lý Thái Tổ",
				feeShip: 35000
			},
			{
				nameWard: "Trần Hưng Đạo",
				feeShip: 55000
			},
		]
	},
	{
		nameCity: "Lâm Đồng",
		ward: [
			{
				nameWard: "Lạc Dương",
				feeShip: 35000
			},
			{
				nameWard: "Đơn Dương",
				feeShip: 45000
			},
			{
				nameWard: "Đinh Văn Lâm Hà",
				feeShip: 55000
			},
			{
				nameWard: "Nam Ban Lâm Hà",
				feeShip: 15000
			},
			{
				nameWard: "Tân Hà Lâm Hà",
				feeShip: 20000
			},
		]
	},
	{
		nameCity: "Đà Nẵng",
		ward: [
			{
				nameWard: "Cẩm Lệ",
				feeShip: 35000
			},
			{
				nameWard: "Hòa Xuân",
				feeShip: 45000
			},
			{
				nameWard: "Tam Kỳ",
				feeShip: 55000
			},
			{
				nameWard: "Hương Trà",
				feeShip: 15000
			},
			{
				nameWard: "Phú Xuân",
				feeShip: 20000
			},
		]
	},
]

$(document).on("click", function(event) {
	if (!$(event.target).closest(".city").length) {
		$(".dropdown-city").hide();
	}
});

$(".city").on("click", function(event) {
	event.stopPropagation();
	$(".dropdown-city").toggle();
});

$(document).on("click", function(event) {
	if (!$(event.target).closest(".ward").length) {
		$(".dropdown-ward").hide();
	}
});

$(".ward").on("click", function(event) {
	event.stopPropagation();
	$(".dropdown-ward").toggle();
});

$(".dropdown-ward").on("click", ".item", function() {
	const val = $(this).text();

	$(".current-ward").text(val);

	let cityName = $(".current-city").text();

	let wardCity = address.filter(city => city.nameCity == cityName);
	wardCity = wardCity[0].ward;
	let totalPrice = parseInt(sessionStorage.getItem('totalPrice'));
	wardCity.forEach(function(ward) {
		console.log(ward);
		if (ward.nameWard == val) {
			$(".price-fee").text(formater.format(ward.feeShip) + " đ");
			$(".total-bill-price").text(formater.format((ward.feeShip + totalPrice)) + " đ");
		}
	})
});

$(".dropdown-city").on("click", ".item", function() {
	const val = $(this).text();

	$(".current-city").text(val);

	$(".dropdown-ward").empty();

	let wardCity = address.filter(city => city.nameCity == val);
	wardCity = wardCity[0].ward;
	wardCity.forEach(function(ward) {
		let html = `<div class="item">${ward.nameWard}</div>`;
		$(".dropdown-ward").append(html);
	})
});

function renderCity() {
	$(".dropdown-city").empty();
	address.forEach(function(city) {
		let html = `<div class="item">${city.nameCity}</div>`;
		$(".dropdown-city").append(html);
	})
}

function renderItemOrder() {
	let basket = JSON.parse(sessionStorage.getItem('basket')) || [];

	$(".list-item-order").empty();
	basket.forEach(function(thisItem) {
		let item = thisItem.item
		let html = `
						<div id = ${item.id} class="item-order">
		                                <div class="item-image">
		                                    <img
		                                        src="/uploads/${item.image}"
		                                        alt=""
		                                    />
		                                </div>
		                                <div class="basket-right">
		                                    <div class="item-basket-info">
		                                        <p class="name">
		                                            ${item.name}
		                                        </p>
		                                        <p class="description">
		                                            ${item.description}
		                                        </p>
		                                    </div>
		                                    <div class="total-price-item">
		                                        <p class="price">${formater.format(item.price)} đ</p>

		                                        <div class="quantity">
		                                            <p class="title-quantity">
		                                                Quantity:
		                                            </p>
		                                            <p class="num-quantity">${thisItem.num}</p>
		                                        </div>
		                                    </div>
		                                </div>
		                            </div>
		
		`;
		$(".list-item-order").append(html);
	});
	let totalPrice = parseInt(sessionStorage.getItem('totalPrice'));
	$(".price-total").text(formater.format(totalPrice) + " đ")
}

$(".cancel").click(function() {
	window.location.href = "/user/home.html";
})

function validateInfo(name, phone, message, city, ward, detailLocation) {
	let valid = true;

	if (name.length === 0) {
		valid = false;
		$(`#name`).next().text("Please input your name");
	} else if (name.length > 100) {
		valid = false;
		$(`#name`)
			.next()
			.text("Name length must be less than or equal 100");
	} else {
		$(`#name`).next().text("");
	}

	if (detailLocation.length === 0) {
		valid = false;
		$(`#detail-location`).next().text("Please input your detail address");
	} else if (detailLocation.length > 150) {
		valid = false;
		$(`#detail-location`)
			.next()
			.text("Detail address length must be less than or equal 150");
	} else {
		$(`#detail-location`).next().text("");
	}

	if (message.length > 500) {
		valid = false;
		$(`#message`)
			.next()
			.text("Message length must be less than or equal 500");
	} else {
		$(`#message`).next().text("");
	}

	if (city.length === 0 || city == "Choose City") {
		valid = false;
		$(`.city .note`)
			.text("Please choose city");
	} else {
		$(`.city .note`).text("");
	}

	if (ward.length === 0 || ward == "Choose Ward") {
		valid = false;
		$(`.ward .note`)
			.text("Please choose ward");
	} else {
		$(`.ward .note`).text("");
	}

	if (phone === "") {
		valid = false;
		$(`#phone`).next().text("Please input your phone");
	} else if (phone.length > 11) {
		valid = false;
		$(`#phone`)
			.next()
			.text("Phone length must be less than or equal 11");
	} else {
		$(`#phone`).next().text("");
	}

	return valid;
}

function createOrder(order) {
	$.ajax({
		url: "http://localhost:8080/api/orders",
		method: "POST",
		contentType: "application/json",
		data: JSON.stringify(order),

		success: function(data) {
			console.log(data)
			sessionStorage.clear();
			localStorage.setItem("order", JSON.stringify(data.data));
			window.location.href = "/user/orderSuccessfully.html";
		},

		error: function(xhr) {
			handleShowToastError(xhr, "Order placement failed")
		}
	});
}

$(".place-order").click(function() {
	const name = $("#name").val().trim();
	const phone = $("#phone").val().trim();
	const message = $("#message").val().trim();
	const city = $(".current-city").text().trim();
	const ward = $(".current-ward").text().trim();
	const detailLocation = $("#detail-location").val().trim();
	let priceText = $(".total-bill-price").text().replace(".", "");
	const totalPrice = parseInt(priceText.substr(0, priceText.length - 2));
	let feeText = $(".price-fee").text().replace(".", "");
	const feeShip = parseInt(feeText.substr(0, feeText.length - 2));

	if (!validateInfo(name, phone, message, city, ward, detailLocation)) {
		$("html, body").animate({
			scrollTop: $(".title").offset().top
		}, 1000);
		return;
	}

	const address = detailLocation + ", Ward " + ward + ", " + city + " City, " + "VietNam";
	let basket = JSON.parse(sessionStorage.getItem('basket')) || [];
	if (basket.length == 0) {
		showToast("error", "Order placement failed", "Don't have item in the basket")
		return;
	}
	const items = [];
	basket.forEach(function(thisItem) {
		let item = {
			itemId: thisItem.item.id,
			numItem: thisItem.num
		}
		items.push(item);
	})

	const order = {
		name: name,
		phone: phone,
		message: message,
		address: address,
		totalPrice: totalPrice,
		items: items,
		feeShip: feeShip
	}
	console.log(order);

	createOrder(order);
})

$(document).ready(function() {
	renderCity();
	renderItemOrder()
})
