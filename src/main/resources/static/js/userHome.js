function setupModal(triggerId, modalId) {
	const $trigger = $(triggerId);
	const $modal = $("#" + modalId);
	const $closeBtn = $modal.find(".close");

	$trigger.on("click", function() {
		$modal.show();
		$("body").addClass("modal-open");
	});

	$closeBtn.on("click", function() {
		$modal.hide();
		$("body").removeClass("modal-open");
	});

	$(window).on("click", function(e) {
		if ($(e.target).is($modal)) {
			$modal.hide();
			$("body").removeClass("modal-open");
		}
	});
}

setupModal(".your-order-button", "modal-your-order");
setupModal(".cart-button", "modal-show-basket");

function addToBasket(itemAdd, numItem) {
	let basket = JSON.parse(sessionStorage.getItem('basket')) || [];
	let totalItem = parseInt(sessionStorage.getItem('totalItem')) || 0;
	let totalPrice = parseInt(sessionStorage.getItem('totalPrice')) || 0;
	numItem = parseInt(numItem);

	let item = { item: itemAdd, num: numItem }

	basket.push(item);
	totalItem += numItem;
	totalPrice += numItem * itemAdd.price

	sessionStorage.setItem('basket', JSON.stringify(basket));
	sessionStorage.setItem('totalItem', totalItem.toString())
	sessionStorage.setItem('totalPrice', totalPrice.toString())

	checkBasket();
	showToast("success", "Add item to basket", "Item added to your basket")
}

function deleteFromBasket(itemId) {
	let basket = JSON.parse(sessionStorage.getItem('basket'));
	let totalItem = parseInt(sessionStorage.getItem('totalItem'));
	let totalPrice = parseInt(sessionStorage.getItem('totalPrice'));

	let item = basket.filter(item => item.item.id == itemId);
	item = item[0];
	totalItem -= item.num;
	totalPrice -= item.num * item.item.price;
	basket = basket.filter(thisItem => thisItem.item.id != itemId);

	sessionStorage.setItem('basket', JSON.stringify(basket));
	sessionStorage.setItem('totalItem', totalItem.toString())
	sessionStorage.setItem('totalPrice', totalPrice.toString())

	checkBasket();
	showToast("success", "Remove item from basket", "Item removed from your basket")
}

function updateItemBasket(itemId, numItem) {
	let basket = JSON.parse(sessionStorage.getItem('basket'));
	let totalItem = parseInt(sessionStorage.getItem('totalItem'));
	let totalPrice = parseInt(sessionStorage.getItem('totalPrice'));
	numItem = parseInt(numItem);

	let isChange = false;
	basket.forEach(function(item) {
		if (item.item.id == itemId) {
			if (item.num == numItem) {
				return true;
			}
			isChange = true;
			totalItem += (numItem - item.num);
			totalPrice = totalPrice + ((numItem - item.num) * item.item.price);
			item.num = numItem;

			return true;
		}
	})

	if (isChange) {
		sessionStorage.setItem('basket', JSON.stringify(basket));
		sessionStorage.setItem('totalItem', totalItem.toString())
		sessionStorage.setItem('totalPrice', totalPrice.toString())

		checkBasket();
		showToast("success", "Update item quantity", "Item quantity updated")
	}
}

function getNumItemBasket(itemId) {
	let basket = JSON.parse(sessionStorage.getItem('basket')) || [];

	let item = basket.filter(item => item.item.id == itemId);

	if (item.length > 0) {
		item = item[0];
		return item.num;
	}

	return 0;
}

function checkBasket() {
	let totalItem = parseInt(sessionStorage.getItem('totalItem')) || 0;
	let totalPrice = parseInt(sessionStorage.getItem('totalPrice')) || 0;
	if (totalItem > 0) {

		const html = `
            <p class="total-price-basket active-basket">
                            ${formater.format(totalPrice)} đ
                        </p>
                        <div class="total-item-basket active-basket">
                            <p class="num-item-basket">${totalItem}</p>
                        </div>
        `;

		$(".active-basket").remove();
		$(".cart-button").append(html);
		$(".cart-button").addClass("has-item");
	} else {
		$(".active-basket").remove();
		$(".cart-button").removeClass("has-item");
	}
}

function setNumItem(id, num) {
	$(`#${id}`).addClass("item-active")
	$(`#${id} .button-plus`).css("display", "none");
	$(`#${id} .button-after-add`).css("display", "flex");
	$(`#${id} .button-after-add .num-item`).val(num);
}

function checkItemIsAdd() {
	let basket = JSON.parse(sessionStorage.getItem('basket')) || [];

	basket.forEach(function(thisItem) {
		setNumItem(thisItem.item.id, thisItem.num)
	})
}

$("body").on("click", ".button-plus", function(e) {
	e.preventDefault();
	let id = $(this).parent().parent().attr("id")
	let item = currentItems.filter(item => item.id == id);
	addToBasket(item[0], 1);

	$(this).parent().parent().addClass("item-active");
	$(this).next().css("display", "flex");
	$(this).next().children(2).children(2).val("1");
	$(this).css("display", "none");
});

$(".list-item").on("click", ".button-trash", function(e) {
	e.preventDefault();
	let id = $(this).parent().parent().parent().attr("id")
	deleteFromBasket(id)

	changeButtonAfterDelete(id)
});

function changeButtonAfterDelete(id) {
	$(`#${id}`).removeClass("item-active")
	$(`#${id} .button-plus`).css("display", "flex");
	$(`#${id} .button-after-add`).css("display", "none");
	$(`#${id} .button-after-add .num-item`).val("1");
}

$(".list-item").on("click", ".minus", function() {
	let currentP = parseInt($(this).next().val());

	if (currentP == 1) {
		showToast("error", "Add item to basket", "Min quantity of this item is 1")
		return;
	}

	currentP -= 1;

	let id = $(this).parent().parent().parent().parent().attr("id");
	updateItemBasket(id, currentP);

	$(this).next().val(currentP);
});

$(".list-item").on("click", ".plus", function() {
	let currentP = parseInt($(this).prev().val());

	if (currentP == 3) {
		showToast("error", "Add item to basket", "Max quantity of this item is 3")
		return;
	}

	currentP += 1;

	let id = $(this).parent().parent().parent().parent().attr("id");
	updateItemBasket(id, currentP);

	$(this).prev().val(currentP);
});

$(".list-item").on("blur", ".num-item", function(event) {
	const value = $(this).val();
	let id = $(this).parent().parent().parent().parent().attr("id");

	if (value == "") {
		deleteFromBasket(id);
		changeButtonAfterDelete(id)
	} else if (value < 1) {
		$(this).val("1");
		updateItemBasket(id, "1")
	} else if (value > 3) {
		$(this).val("3");
		updateItemBasket(id, "3")
	} else if (value == "1" || value == "2" || value == "3") {
		updateItemBasket(id, value)
	}
});

$(".button-filter").click(function() {
	if ($(this).hasClass("type-active")) {
		return;
	}

	if ($(this).attr("id") === "food") {
		$(this).addClass("type-active");
		$(this).next().removeClass("type-active");
	} else if ($(this).attr("id") === "drink") {
		$(this).addClass("type-active");
		$(this).prev().removeClass("type-active");
	}
	currentPage = 1;
	renderAllItems();
});

// Handle render item and event
let size = 10;
let currentPage = 1;
let maxPage = 1;

function renderPagesItem(numPage, totalItem) {
	$(".total-items").empty();
	$(".total-items").text(`Total ${totalItem} items`);
	$(".list-page").empty();
	for (let i = 1; i <= numPage; i++) {
		$(".list-page").append(`<a id="page-${i}">${i}</a>`);
	}
}

function renderItemsPerPage(data) {
	const body = $(".list-item");
	body.empty();

	if (data.length == 0) {
		body.append('<p class="empty-modal">No items to display.</p')
	}

	data.forEach(function(item) {
		const row = `
		<div id = ${item.id} class="item">
		                    <div class="item-info">
		                        <div class="item-image">
		                            <img
		                                src="/uploads/${item.image}"
		                                alt=""
		                            />
		                        </div>
		                        <div class="info">
		                            <p class="name">${item.name}</p>
		                            <p class="description">
		                                ${item.description}
		                            </p>
		                            <p class="price">${formater.format(item.price)} đ</p>
		                        </div>
		                    </div>
		                    <div class="list-button">
		                        <div class="button-plus">
		                            <img src="../images/icon/add.svg" alt="" />
		                        </div>
		                        <div class="button-after-add">
		                            <div class="button-trash">
		                                <img
		                                    src="../images/icon/trash-can.svg"
		                                    alt=""
		                                />
		                            </div>
		                            <div class="num-block">
		                                <img
		                                    class="minus"
		                                    src="../images/icon/minus.svg"
		                                    alt=""
		                                />
		                                <input
		                                    class="num-item"
		                                    type="number"
		                                    min="1"
		                                    max="3"
		                                    value="1"
											maxlength="1"
		                                />
		                                <img
		                                    class="plus"
		                                    src="../images/icon/plus.svg"
		                                    alt=""
		                                />
		                            </div>
		                        </div>
		                    </div>
		                </div>
		      `;
		body.append(row);
	});

	let activePage = `#page-${currentPage}`;

	$(".list-page a").removeClass("active");
	$(activePage).addClass("active");
}

$(".pagination .left").on("click", function() {
	if (currentPage == 1) {
		return;
	}
	currentPage = currentPage - 1;
	renderAllItems();
});

$(".pagination .right").on("click", function() {
	if (currentPage == maxPage) {
		return;
	}
	currentPage = currentPage + 1;
	renderAllItems();
});

$(".pagination .list-page").on("click", "a", function() {
	let pageClick = parseInt($(this).text());
	if (currentPage == pageClick) {
		return;
	}
	currentPage = pageClick;
	renderAllItems();
});

let currentItems;
let sortType = "ASC";
function renderAllItems() {
	const nameSearch = $("#search").val();
	let type = "FOOD";
	if ($("#food").hasClass("type-active")) {
		type = "FOOD";
	} else {
		type = "DRINK";
	}

	$.ajax({
		url: `http://localhost:8080/api/user/items?name=${encodeURIComponent(
			nameSearch
		)}&type=${encodeURIComponent(type)}&page=${encodeURIComponent(
			currentPage
		)}&size=${encodeURIComponent(size)}&sortByPrice=${encodeURIComponent(
			sortType
		)}`,
		method: "GET",

		beforeSend: function() {
			$("main .list-item").hide();
			$("main .list-item-s").fadeIn(200);
		},

		success: function(data) {
			renderPagesItem(data.data.totalPages, data.data.totalElements);
			renderItemsPerPage(data.data.data);
			maxPage = data.data.totalPages;
			currentItems = data.data.data;

			setupModal(".item-info", "modal-add-item");
			checkItemIsAdd();
		},

		error: function(xhr) {
			handleShowToastError(xhr, "Load item")
		},

		complete: function() {
			$("main .list-item-s").hide();
			$("main .list-item").fadeIn(200);
		},
	});
}

$("#num-per-page").change(function() {
	size = parseInt($(this).val());
	currentPage = 1;
	renderAllItems();
});

$("#filter-price").change(function() {
	sortType = $(this).val();
	renderAllItems();
});

$("#search").on("keypress", function(e) {
	if (e.which == 13) {
		$(".search-box").hide();
		renderAllItems();
	}
});

// Render add to basket modal
$("#modal-add-item").on("click", ".minus", function() {
	let currentP = parseInt($(this).next().val());

	if (currentP == 1) {
		showToast("error", "Add item to basket", "Min quantity of this item is 1")
		return;
	}

	currentP -= 1;

	$(this).next().val(currentP);

	let price = parseInt($("#modal-add-item .price").text().replace(".", ""));
	$("#modal-add-item #add-to-basket").text("Add to Basket - " + formater.format(price * currentP) + " đ");
});

$("#modal-add-item").on("click", ".plus", function() {
	let currentP = parseInt($(this).prev().val());

	if (currentP == 3) {
		showToast("error", "Add item to basket", "Max quantity of this item is 3")
		return;
	}

	currentP += 1;
	$(this).prev().val(currentP);

	let price = parseInt($("#modal-add-item .price").text().replace(".", ""));
	$("#modal-add-item #add-to-basket").text("Add to Basket - " + formater.format(price * currentP) + " đ");
});

$("#modal-add-item").on("blur", ".num-item", function(event) {
	const value = $(this).val();

	if (value == "" || value < 1) {
		$(this).val("1");
		let price = parseInt($("#modal-add-item .price").text().replace(".", ""));
		$("#modal-add-item #add-to-basket").text("Add to Basket - " + formater.format(price) + " đ");
	} else if (value > 3) {
		$(this).val("3");

		let price = parseInt($("#modal-add-item .price").text().replace(".", ""));
		$("#modal-add-item #add-to-basket").text("Add to Basket - " + formater.format(price * 3) + " đ");
	} else if (value == "1" || value == "2" || value == "3") {
		let price = parseInt($("#modal-add-item .price").text().replace(".", ""));
		$("#modal-add-item #add-to-basket").text("Add to Basket - " + formater.format(price * parseInt(value)) + " đ");
	}
});

$(".list-item").on("click", ".item-info", function() {
	const thisId = parseInt($(this).parent().attr("id"));

	let thisItem = currentItems.filter((item) => item.id == thisId);
	thisItem = thisItem[0];
	$("#modal-add-item .item-image img").attr(
		"src",
		`/uploads/${thisItem.image}`
	);
	$("#modal-add-item .name").text(thisItem.name);
	$("#modal-add-item .description").text(thisItem.description);
	$("#modal-add-item .price").text(formater.format(thisItem.price) + " đ");
	$("#modal-add-item .footer-add-item").attr("id", thisId);

	let numItem = getNumItemBasket(thisId);

	if (numItem == 0) {
		$("#modal-add-item .num-item").val("1");
		$("#modal-add-item #add-to-basket").text("Add to Basket - " + formater.format(thisItem.price) + " đ");
	} else {
		$("#modal-add-item .num-item").val(numItem);
		$("#modal-add-item #add-to-basket").text("Add to Basket - " + formater.format(thisItem.price * numItem) + " đ");
	}
});

$("#add-to-basket").click(function() {
	let itemId = $(this).parent().attr("id");
	let numItem = $("#modal-add-item .num-item").val();

	if (getNumItemBasket(itemId) > 0) {
		updateItemBasket(itemId, numItem)
	} else {
		let itemAdd = currentItems.filter(item => item.id == itemId);
		itemAdd = itemAdd[0];
		addToBasket(itemAdd, numItem)
	}

	$("#modal-add-item").hide();
	$("body").removeClass("modal-open");
	setNumItem(itemId, numItem)
})

//Handle show basket item
function renderBasket() {
	let basket = JSON.parse(sessionStorage.getItem('basket')) || [];
	if (basket.length == 0) {
		$("#modal-show-basket .list-basket").empty();
		$("#modal-show-basket .price-total").text("0 đ")
		$("#modal-show-basket .list-basket").append('<p class="empty-modal">Dont have item in basket</p>')
		return;
	}
	let totalPrice = 0;
	$("#modal-show-basket .list-basket").empty();
	basket.forEach(function(thisItem) {
		let item = thisItem.item;
		totalPrice += item.price * thisItem.num;
		let html = `
							<div id = ${item.id} class="item-basket">
									<div class="item-image">
										<img
											src="/uploads/${item.image}"
											alt="" />
									</div>
									<div class="basket-right">
										<div class="item-basket-info">
											<p class="name">${item.name}</p>
											<p class="description">${item.description}</p>
										</div>
										<div class="total-price-item">
											<p class="price">${formater.format(item.price)} đ</p>
											<div class="add-num-block">
												<img class="minus" src="../images/icon/minus.svg" alt="" />
												<input class="num-item" type="number" min="1" max="3" value=${thisItem.num} />
												<img class="plus" src="../images/icon/plus.svg" alt="" />
											</div>
											<p class="total-item">${formater.format(thisItem.num * item.price)} đ</p>
										</div>
									</div>
									<div class="delete-item-basket">
										<svg width="10px" height="10px" viewBox="0 0 22 22" version="1.1"
											xmlns="http://www.w3.org/2000/svg"
											xmlns:xlink="http://www.w3.org/1999/xlink" fill="#fff">
			                                    <g id="SVGRepo_bgCarrier"
												stroke-width="0"></g>
			                                    <g id="SVGRepo_tracerCarrier"
												stroke-linecap="round" stroke-linejoin="round"></g>
			                                    <g id="SVGRepo_iconCarrier">
			                                        <title>cancel</title>
			                                        <desc>Created with Sketch.</desc>
			                                        <g id="icons" stroke="none"
												stroke-width="1" fill="none" fill-rule="evenodd">
			                                            <g
												id="ui-gambling-website-lined-icnos-casinoshunter"
												transform="translate(-869.000000, -159.000000)" fill="#fff"
												fill-rule="nonzero">
			                                                <g id="square-filled"
												transform="translate(50.000000, 120.000000)">
			                                                    <path
												d="M820.716328,39.2890737 L830,48.573 L839.283672,39.2890737 C839.644156,38.9285898 840.211387,38.9008602 840.603678,39.2058851 L840.710926,39.3021143 C841.101451,39.6926386 841.101451,40.3258036 840.710926,40.7163279 L831.427,50 L840.710926,59.2836721 C841.07141,59.6441561 841.09914,60.2113872 840.794115,60.6036784 L840.697886,60.7109263 C840.307361,61.1014506 839.674196,61.1014506 839.283672,60.7109263 L830,51.427 L820.716328,60.7109263 C820.355844,61.0714102 819.788613,61.0991398 819.396322,60.7941149 L819.289074,60.6978857 C818.898549,60.3073614 818.898549,59.6741964 819.289074,59.2836721 L828.573,50 L819.289074,40.7163279 C818.92859,40.3558439 818.90086,39.7886128 819.205885,39.3963216 L819.302114,39.2890737 C819.692639,38.8985494 820.325804,38.8985494 820.716328,39.2890737 Z M819.996181,40.0092211 L829.987233,50 L819.996181,59.9907789 L820.009221,60.0038195 L830,50.0127671 L839.990779,60.0038195 L840.003819,59.9907789 L830.012767,50 L840.003819,40.0092211 L839.990779,39.9961805 L830,49.9872329 L820.009221,39.9961805 L819.996181,40.0092211 Z"
												id="cancel"></path>
			                                                </g>
			                                            </g>
			                                        </g>
			                                    </g>
			                                </svg>
									</div>
								</div>
			
			`;
		$("#modal-show-basket .list-basket").append(html);
	});
	$("#modal-show-basket .price-total").text(totalPrice + " đ")
}
$("#modal-show-basket").on("click", ".minus", function() {
	let currentP = parseInt($(this).next().val());

	if (currentP == 1) {
		showToast("error", "Add item to basket", "Min quantity of this item is 1")
		return;
	}

	currentP -= 1;

	$(this).next().val(currentP);

	let id = $(this).parent().parent().parent().parent().attr("id")
	updateItemBasket(id, currentP);

	let price = $(`#${id} .price`).text().replace(".", "");
	price = parseInt(price.substr(0, price.length - 2))
	$(this).parent().next().text(formater.format(price * currentP) + " đ");
	$("#modal-show-basket .price-total").text(sessionStorage.getItem('totalPrice') + " đ")

	checkItemIsAdd();
});

$("#modal-show-basket").on("click", ".plus", function() {
	let currentP = parseInt($(this).prev().val());

	if (currentP == 3) {
		showToast("error", "Add item to basket", "Max quantity of this item is 3")
		return;
	}

	currentP += 1;

	$(this).prev().val(currentP);

	let id = $(this).parent().parent().parent().parent().attr("id")
	updateItemBasket(id, currentP);

	let price = $(`#${id} .price`).text().replace(".", "");
	price = parseInt(price.substr(0, price.length - 2))
	$(this).parent().next().text(formater.format(price * currentP) + " đ");
	$("#modal-show-basket .price-total").text(sessionStorage.getItem('totalPrice') + " đ")

	checkItemIsAdd();
});

$("#modal-show-basket").on("blur", ".num-item", function(event) {
	const value = $(this).val();
	let id = $(this).parent().parent().parent().parent().attr("id");

	if (value == "") {
		deleteFromBasket(id);
		changeButtonAfterDelete(id);
		renderBasket();
		return;
	}
	else if (value < 1) {
		$(this).val("1");
		updateItemBasket(id, "1");

	} else if (value > 3) {
		$(this).val("3");
		updateItemBasket(id, "3")
	} else if (value == "1" || value == "2" || value == "3") {
		updateItemBasket(id, value)
	}


	let price = parseInt($(`#modal-show-basket #${id} .price`).text().replace(".", ""));
	let val = parseInt($(this).val());

	$(`#modal-show-basket #${id} .total-item`).text(formater.format(price * val));

	checkItemIsAdd();
});

$(".cart-button").click(function() {
	let basket = JSON.parse(sessionStorage.getItem('basket')) || [];

	if (basket.length == 0) {
		$("#modal-show-basket .list-basket").empty();
		$("#modal-show-basket .price-total").text("0 đ")
		$("#modal-show-basket .list-basket").append('<p class="empty-modal">Dont have item in basket</p>')
		return;
	}

	renderBasket();
})

$("#modal-show-basket").on("click", ".delete-item-basket", function() {
	let id = $(this).parent().attr("id");
	deleteFromBasket(id);
	renderBasket();
	changeButtonAfterDelete(id);
})

$("#modal-show-basket").on("click", "#place-order", function() {
	let totalItem = parseInt(sessionStorage.getItem('totalItem')) || 0;
	if (totalItem > 0) {
		window.location.href = "/user/placeOrder.html";
		return;
	}

	showToast("error", "Place order failed", "Dont't have item in the basket.");
})

let currentOrder;

function renderStatusOrder(status) {
	const listStatus = ["New", "Shipping", "Completed"];

	if (status == "Canceled") {
		$(".stepper-wrapper").append(`
										<div class="stepper-item step-1 completed">
											<div class="step-counter">
												<img src="../images/icon/status-part.svg" alt="" />
											</div>
											<div class="step-name">Order placed successfully</div>
										</div>
										<div class="stepper-item step-2 cancel">
											<div class="step-counter">
												<img src="../images/icon/status-part.svg" alt="" />
											</div>
											<div class="step-name">Preparing your order</div>
										</div>
										<div class="stepper-item step-2 active">
											<div class="step-counter">
												<img src="../images/icon/cancel-pink.svg" alt="" />
											</div>
											<div style="color: red;" class="step-name">Order is canceled</div>
										</div>
										
										<div class="stepper-item step-3">
											<div class="step-counter">
												<img src="../images/icon/status-feature.svg" alt="" />
											</div>
											<div class="step-name">Shipping</div>
										</div>
										<div class="stepper-item step-4">
											<div class="step-counter">
												<img src="../images/icon/status-feature.svg" alt="" />
											</div>
											<div class="step-name">Completed</div>
										</div>
					`);
		return;
	}

	$(".stepper-wrapper").append(`
						<div class="stepper-item step-1 completed">
								<div class="step-counter">
									<img src="../images/icon/status-part.svg" alt="" />
								</div>
								<div class="step-name">Order placed successfully</div>
							</div>
		`)

	let isStatus = false;
	for (let i = 0; i < listStatus.length; i++) {
		$(".stepper-wrapper").append(`
				<div class="stepper-item step-${i + 2}">
				</div>`);
		if (listStatus[i] == status) {
			isStatus = true;
			$(`.stepper-wrapper .step-${i + 2}`).addClass("active")
			$(`.stepper-wrapper .step-${i + 2}`).append(`
				<div class="step-counter">
					<img src="../images/icon/status-current.svg" alt="" />
				</div>
				<div class="step-name">${listStatus[i] == "New" ? "Preparing your order" : listStatus[i]}</div>
				`)
			continue;
		}
		if (!isStatus) {
			$(`.stepper-wrapper .step-${i + 2}`).addClass("completed")
			$(`.stepper-wrapper .step-${i + 2}`).append(`
							<div class="step-counter">
								<img src="../images/icon/status-part.svg" alt="" />
							</div>
							<div class="step-name">${listStatus[i] == "New" ? "Preparing your order" : listStatus[i]}</div>
							`)
		}
		if (isStatus) {
			$(`.stepper-wrapper .step-${i + 2}`).append(`
							<div class="step-counter">
								<img src="../images/icon/status-feature.svg" alt="" />
							</div>
							<div class="step-name">${listStatus[i] == "New" ? "Preparing your order" : listStatus[i]}</div>
							`)
		}
	}
}

function renderItemOrder() {
	$("#modal-your-order .modal-content").append(`
						<div class="order-code">
							<p class="title">Order code:</p>
							<p class="code">#${currentOrder.id}</p>
						</div>
		`);
	$("#modal-your-order .modal-content").append('<div class="item-order-block"><div class="list-item-order"></div></div>');
	$("#modal-your-order .modal-content").append(`
						<div class="detail-fee">
							<div class="total-price">
								<p class="title">Total</p>
								<p class="price-total">${formater.format(currentOrder.totalPrice - currentOrder.feeShip)} đ</p>
							</div>
							<div class="ship-fee">
								<p class="title">Shipping fee</p>
								<p class="price-fee">${formater.format(currentOrder.feeShip)} đ</p>
							</div>
						</div>
		`);

	if (currentOrder.items.length > 1) {
		$("#modal-your-order .item-order-block").append('<div class="see-more"><img src="../images/icon/more.svg" alt="" /></div>');
	}

	$("#modal-your-order .total-bill-price").text(formater.format(currentOrder.totalPrice) + " đ");

	const firstItem = currentOrder.items[0];

	$("#modal-your-order .list-item-order").append(`
						<div class="item-order">
									<div class="item-image">
										<img
											src="/uploads/${firstItem.image}"
											alt="" />
									</div>
									<div class="basket-right">
										<div class="item-basket-info">
											<p class="name">${firstItem.name}</p>
											<p class="description">${firstItem.description}</p>
										</div>
										<div class="total-price-item">
											<p class="price">${formater.format(firstItem.price)} đ</p>

											<div class="quantity">
												<p class="title">Quantity:</p>
												<p class="num-quantity">${firstItem.quantity}</p>
											</div>
										</div>
									</div>
								</div>
		
		`);

	renderStatusOrder(currentOrder.status);
}

function getOrder(id) {
	$.ajax({
		url: `http://localhost:8080/api/orders/${encodeURIComponent(id)}`,
		method: "GET",

		success: function(data) {
			const order = data.data;
			currentOrder = order
			renderItemOrder()
		},

		error: function(xhr) {
			handleShowToastError(xhr, "Get order")
		}
	})
}

function renderOrder(id) {
	if (id == 0) {
		$("#modal-your-order .total-bill-price").text(0 + " đ");
		$(".stepper-wrapper").append(`
								<div class="stepper-item step-1">
									<div class="step-counter">
										<img src="../images/icon/status-feature.svg" alt="" />
									</div>
									<div class="step-name">Order placed successfully</div>
								</div>
								<div class="stepper-item step-2">
									<div class="step-counter">
										<img src="../images/icon/status-feature.svg" alt="" />
									</div>
									<div class="step-name">Preparing your order</div>
								</div>
								
								<div class="stepper-item step-3">
									<div class="step-counter">
										<img src="../images/icon/status-feature.svg" alt="" />
									</div>
									<div class="step-name">Shipping</div>
								</div>
								<div class="stepper-item step-4">
									<div class="step-counter">
										<img src="../images/icon/status-feature.svg" alt="" />
									</div>
									<div class="step-name">Completed</div>
								</div>
			`);
		$("#modal-your-order .modal-content").append("<p style='margin: 120px 0px; text-align: center;'>You don't have order</p>");
		return;
	}

	getOrder(id)
}
let isOrderDropdown = false;
$("#modal-your-order").on("click", ".see-more", function() {
	$("#modal-your-order .list-item-order").empty();
	if (!isOrderDropdown) {
		isOrderDropdown = true;
		$(".see-more img").css("rotate", "180deg");
		currentOrder.items.forEach(function(item) {
			let html = `
											<div class="item-order">
														<div class="item-image">
															<img
																src="/uploads/${item.image}"
																alt="" />
														</div>
														<div class="basket-right">
															<div class="item-basket-info">
																<p class="name">${item.name}</p>
																<p class="description">${item.description}</p>
															</div>
															<div class="total-price-item">
																<p class="price">${formater.format(item.price)} đ</p>

																<div class="quantity">
																	<p class="title">Quantity:</p>
																	<p class="num-quantity">${item.quantity}</p>
																</div>
															</div>
														</div>
													</div>
							
							`;
			$("#modal-your-order .list-item-order").append(html);
		})

	} else {
		isOrderDropdown = false;
		$(".see-more img").css("rotate", "0deg");
		const firstItem = currentOrder.items[0];

		$("#modal-your-order .list-item-order").append(`
								<div class="item-order">
											<div class="item-image">
												<img
													src="/uploads/${firstItem.image}"
													alt="" />
											</div>
											<div class="basket-right">
												<div class="item-basket-info">
													<p class="name">${firstItem.name}</p>
													<p class="description">${firstItem.description}</p>
												</div>
												<div class="total-price-item">
													<p class="price">${formater.format(firstItem.price)} đ</p>

													<div class="quantity">
														<p class="title">Quantity:</p>
														<p class="num-quantity">${firstItem.quantity}</p>
													</div>
												</div>
											</div>
										</div>
				
				`);
	}
})

$(".your-order-button").click(function() {
	let orderId = parseInt(localStorage.getItem("order")) || 0;
	$("#modal-your-order .modal-content").empty();
	$("#modal-your-order .modal-content").append('	<div class="stepper-wrapper"></div>');
	renderOrder(orderId);
})

function firstLoad() {
	$(".skeleton-block").show();

	setTimeout(() => {
		$(".body-content").show();
		$(".skeleton-block").hide();
	}, 2000);
}

$("#search").on("input", function() {
	const keyword = $(this).val().trim();
	const $searchBox = $(".search-box");
	const $suggestBox = $("#suggest-box");
	
	$searchBox.show();

	if (keyword.length === 0) {
		$suggestBox.empty();
		return;
	}

	$.ajax({
		url: "http://localhost:8080/api/user/items/suggest",
		method: "GET",
		data: { name: keyword },
		success: function(res) {
			if (res.data && res.data.length > 0) {
				const html = res.data.map(item => `<div class="suggest-item">${item}</div>`).join("");
				$suggestBox.html(html);

				$(".suggest-item").on("click", function() {
					$("#search").val($(this).text());
					$suggestBox.empty();
					$searchBox.hide();
					renderAllItems();
				});
			} else {
				$suggestBox.empty();
			}
		},
		error: function() {
			console.error("Can not get suggest");
		}
	});
});

$("#search").on("focus", function() {

	const $searchBox = $(".search-box");
	const $suggestBox = $("#suggest-box");
	$searchBox.show();

	const $topSearchBox = $(".list-top-search");

	$.ajax({
		url: "http://localhost:8080/api/search/top_search",
		method: "GET",
		success: function(res) {
			if (res.data && res.data.length > 0) {
				$topSearchBox.empty();
				const html = res.data.map(item => `<div class="top-search-item">${item}</div>`).join("");
				$topSearchBox.html(html);

				$(".top-search-item").on("click", function() {
					$("#search").val($(this).text());
					$suggestBox.empty();
					$searchBox.hide();
					renderAllItems();
				});
			} else {
				$topSearchBox.empty();
			}
		},
		error: function() {
			console.error("Can not get top search");
		}
	});
})

$(document).on("click", function(e) {
	if (!$(e.target).closest(".search-block-cover").length) {
		$(".search-box").hide();
	}
});

function renderTopSold() {
	const $block = $(".list-top-sold");

	$.ajax({
		url: "http://localhost:8080/api/search/top_sold",
		method: "GET",
		success: function(res) {
			$block.empty();
			const html = res.data.map(item => `				
					<div class="item">
				            <div class="item-info">
				                <div class="item-image">
				                    <img src="/uploads/${item.image}" alt="" />
				                </div>
				                <div class="info">
				                    <p class="name">${item.name}</p>
				                    <p class="price">${formater.format(item.price)} đ</p>
									<p class="total-sold">Đã bán: ${item.totalSold}</p>
				                </div>
				            </div>
				        </div>`).join("");
			$block.html(html);
		},
		error: function() {
			handleShowToastError(xhr, "Load item top sold")
		}
	});
}

$(document).ready(function() {
	firstLoad();
	renderAllItems();
	checkBasket();
	renderTopSold()
});

$(window).scroll(function() {
	var sticky = $('.search-block-cover'),
		scroll = $(window).scrollTop();

	if (scroll >= 100) {
		sticky.addClass('fixed');
		$(".head").css("background", "#fff");
		$("header").css("background-image", "none");
		$("header").css("background-color", "#fff");
	} else {
		sticky.removeClass('fixed');
		$(".head").css("background", "none");
		$("header").css("background-image", 'url("../images/head-background.png")');
		$("header").css("background-color", "#edededff");
	}

	if (scroll >= 200) {
		$(".block-filter").addClass('fixed');
	} else {
		$(".block-filter").removeClass('fixed');
	}
});
