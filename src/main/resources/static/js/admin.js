let currentItemId;

function clearModal(modalId) {
	$(`#${modalId} .note`).text("");
	$(`#${modalId} input`).val("");
	hasImage = false;
	$(`#${modalId} .image-show`).empty();
	$(`#${modalId} .image-show`).append('<img src="../images/img-ab.png" alt="" />');
}


//Set up show modal
function setupModal(triggerId, modalId) {
	const $trigger = $(triggerId);
	const $modal = $("#" + modalId);
	const $closeBtn = $modal.find(".close");
	const $cancelBtn = $modal.find(".cancel-modal");

	$trigger.on("click", function() {
		if (triggerId === ".delete" || triggerId === ".edit") {
			currentItemId = parseInt($(this).parent().attr("id"));
		}
		$modal.show();
		$("body").addClass("modal-open");
	});

	$closeBtn.on("click", function() {
		clearModal(modalId)
		$modal.hide();
		$("body").removeClass("modal-open");
	});
	$cancelBtn.on("click", function() {
		clearModal(modalId)
		$modal.hide();
		$("body").removeClass("modal-open");
	});

	$(window).on("click", function(e) {
		if ($(e.target).is($modal)) {
			clearModal(modalId)
			$modal.hide();
			$("body").removeClass("modal-open");
		}
	});
}

setupModal(".button-add", "modal-add-item");

//Set up show dropdown
function setupDropdown(dropdownTrigger, dropdownShow, pName) {
	$(document).on("click", function(event) {
		if (!$(event.target).closest(dropdownTrigger).length) {
			$(dropdownShow).hide();
		}
	});

	$(dropdownTrigger).on("click", function(event) {
		event.stopPropagation();
		$(dropdownShow).toggle();
	});

	$(dropdownShow).on("click", ".item", function() {
		if (pName == ".curr-status") {
			return;
		}
		const val = $(this).text();

		$(pName).text(val);
	});
}

setupDropdown(".status-dropdown-order", ".list-status-order", ".curr-status");
setupDropdown(".filter-time-dropdown", ".list-filter-time", ".current-time");

$(document).on("click", function(event) {
	if (!$(event.target).closest(".filter-status-dropdown").length) {
		$(".list-filter-status").hide();
	}
});

$(".filter-status-dropdown").on("click", function(event) {
	event.stopPropagation();
	$(".list-filter-status").toggle();
});

const arrayFilterStatus = ["New", "Shipping", "Canceled", "Completed"];

$(".list-filter-status").on("click", ".item", function() {
	const val = $(this).text();
	const tagFilter = `
        <div class="item-status">
                                    <p>${val}</p>
                                    <img class="cancel-tag"
                                        src="../images/icon/cancel.svg"
                                        alt=""
                                    />
                                </div>
    `;
	$(".list-status-choose").append(tagFilter);
	$(this).remove();

	arrayFilterStatus.push(val);
	renderAllOrders();
});

$(".list-status-choose").on("click", ".cancel-tag", function() {
	const val = $(this).prev().text();
	const tagFilter = `
        <p class="item">${val}</p>
    `;
	$(".list-filter-status").append(tagFilter);
	$(this).parent().remove();

	const index = arrayFilterStatus.indexOf(val);
	arrayFilterStatus.splice(index, 1);
	renderAllOrders();
});

let hasImage = false;

function validImage(image) {
	const validImage = ["png", "jpeg", "jpg"];

	let valid = true;

	if (image.type.split("/")[0] != "image") {
		valid = false;
		$(".info-button .note").text("File must be image");
	} else if (!validImage.includes(image.type.split("/")[1])) {
		valid = false;
		$(".info-button .note").text("Image type must be png, jpeg or jpg");
	} else if (image.size > 5000000) {
		valid = false;
		$(".info-button .note").text("size image must be less than 5mb");
	} else {
		$(".info-button .note").text("");
	}

	return valid;
}

function setupHandleImage(modalId) {
	$(`${modalId} .choose-file`).on("change", function(event) {
		if (hasImage) {
			showToast("error", "Add image", "You can only add one image")
			return;
		}
		if (
			event.target.files[0] &&
			!hasImage &&
			validImage(event.target.files[0])
		) {
			hasImage = true;
			const url = URL.createObjectURL(event.target.files[0]);
			const html = `
	        <div class="image-add">
	                                    <img
	                                        class="curr-image"
	                                        src=${url}
	                                        alt=""
	                                    />
	                                    <div class="cancel-image">
	                                        <img
	                                            src="../images/icon/cancel.svg"
	                                            alt=""
	                                        />
	                                    </div>
	                                </div>
	        `;
			$(`${modalId} .image-show`).empty();
			$(`${modalId} .image-show`).append(html);
		} else {
			$(`${modalId} .choose-file`).val("");
		}
	});

	$(`${modalId} .image-show`).on("click", ".cancel-image", function() {
		hasImage = false;
		$(`${modalId} .choose-file`).val("");
		$(`${modalId} .image-show`).empty();
		$(`${modalId} .image-show`).append('<img src="../images/img-ab.png" alt="" />');
	});
}

setupHandleImage("#modal-add-item");
setupHandleImage("#modal-edit-item");

$("#management-item .button-filter").click(function() {
	if ($(this).hasClass("active")) {
		return;
	}

	if (!$(this).hasClass("type-food")) {
		$(this).addClass("active");
		$(this).prev().removeClass("active");
	} else if (!$(this).hasClass("type-drink")) {
		$(this).addClass("active");
		$(this).next().removeClass("active");
	}

	if ($(this).parent().attr("id") === "filter-type") {
		currentPage = 1;
		renderAllItems()
	}
});

$("#modal-add-item .button-filter").click(function() {
	if ($(this).hasClass("active")) {
		return;
	}

	if (!$(this).hasClass("type-food")) {
		$(this).addClass("active");
		$(this).prev().removeClass("active");
	} else if (!$(this).hasClass("type-drink")) {
		$(this).addClass("active");
		$(this).next().removeClass("active");
	}
});

$("#modal-edit-item .button-filter").click(function() {
	if ($(this).hasClass("active")) {
		return;
	}

	if (!$(this).hasClass("type-food")) {
		$(this).addClass("active");
		$(this).prev().removeClass("active");
	} else if (!$(this).hasClass("type-drink")) {
		$(this).addClass("active");
		$(this).next().removeClass("active");
	}
});

$(".logout").click(function() {
	$.ajax({
		url: "http://localhost:8080/logout",
		method: "POST",

		success: function() {
			window.location.href = "/admin/login.html";
		},

		error: function(xhr) {
			handleShowToastError(xhr, "Logout failure")
		},
	});
});


function validateItem(image, name, description, price, modalId) {
	let valid = true;

	if (!image && !$(`${modalId} .image-show`).children().hasClass("image-add")) {
		valid = false;
		$(`${modalId} .info-button .note`).text("Please upload image of item");
	} else {
		$(`${modalId} .info-button .note`).text("");
	}

	if (name.length === 0) {
		valid = false;
		$(`${modalId} #name`).next().text("Please input item name");
	} else if (name.length > 100) {
		valid = false;
		$(`${modalId} #name`)
			.next()
			.text("Name length must be less than or equal 100");
	} else {
		$(`${modalId} #name`).next().text("");
	}

	if (description.length > 500) {
		valid = false;
		$(`${modalId} #description`)
			.next()
			.text("Description length must be less than or equal 500");
	} else {
		$(`${modalId} #description`).next().text("");
	}

	if (price === "") {
		valid = false;
		$(`${modalId} #price`).next().text("Please input item price");
	} else if (parseInt(price) < 0 || price === "") {
		valid = false;
		$(`${modalId} #price`)
			.next()
			.text("Price must be greater than or equal 0");
	} else if (price.toString().length > 10) {
		valid = false;
		$(`${modalId} #price`)
			.next()
			.text("Price length number must be less than or equal 10");
	} else {
		$(`${modalId} #price`).next().text("");
	}

	return valid;
}

$("#add-item").click(function() {
	const image = $("#modal-add-item .choose-file").prop("files")[0];
	const name = $("#modal-add-item #name").val().trim();
	const description = $("#modal-add-item #description").val().trim();
	let price = $("#modal-add-item #price").val();
	const type = $("#modal-add-item")
		.find(".active")
		.text()
		.trim()
		.toUpperCase();

	console.log(type);

	if (!validateItem(image, name, description, price, "#modal-add-item")) {
		return;
	}

	price = parseInt(price);

	let dataItem = new FormData();
	dataItem.append("name", name);
	dataItem.append("description", description);
	dataItem.append("image", image);
	dataItem.append("price", price);
	dataItem.append("type", type);

	$.ajax({
		url: "http://localhost:8080/api/items",
		type: "POST",
		processData: false,
		contentType: false,
		data: dataItem,

		success: function(data) {
			console.log(data);
			showToast(data.status, "Add item", data.message);
			clearModal("modal-add-item");
			$("#modal-add-item").hide();
			$("body").removeClass("modal-open");
			renderAllItems();
		},

		error: function(xhr) {
			handleShowToastError(xhr, "Add item")
		},
	});
});


// Handle render item and event
let size = 10;
let currentPage = 1;
let maxPage = 1;

function renderPagesItem(numPage, totalItem) {
	$("#management-item .total-items").empty();
	$("#management-item .total-items").text(`Total ${totalItem} items`);
	$("#management-item .list-page").empty();
	for (let i = 1; i <= numPage; i++) {
		$("#management-item .list-page").append(`<a id="page-${i}">${i}</a>`);
	}
}

function renderItemsPerPage(data) {
	const tbody = $(".item-table tbody");
	tbody.empty();

	if (data.length == 0) {
		tbody.append(`
			<tr><td></td><td></td><td><p style = "text-align: center;">Items not found</p></td><td></td><td></td><td></td></tr>`)
	}

	data.forEach(function(item) {

		const row = `
				<tr>
		                                    <td><p>${item.id}</p></td>
		                                    <td>
		                                        <div class="item-name-table">
		                                            <img
		                                                src="/uploads/${item.image}"
		                                                alt=""
		                                            />
		                                            <p>
		                                                ${item.name}
		                                            </p>
		                                        </div>
		                                    </td>
		                                    <td>
		                                        <p>
		                                            ${item.description}
		                                        </p>
		                                    </td>
		                                    <td><p>${item.price} đ</p></td>
		                                    <td>
		                                        <label class="switch ${item.status ? "active" : ""}">
		                                            <input type="checkbox" />
		                                            <span class="slider round"></span>
		                                        </label>
		                                    </td>
		                                    <td>
		                                        <div id = ${item.id} class="item-action-table">
		                                            <p class="delete">Delete</p>
		                                            <p class="edit">Edit</p>
		                                        </div>
		                                    </td>
		                                </tr>
		      `;
		tbody.append(row);
	});

	let activePage = `#management-item #page-${currentPage}`;

	$("#management-item .list-page a").removeClass("active");
	$(activePage).addClass("active");
}

$("#management-item .left").on("click", function() {
	if (currentPage == 1) {
		return;
	}
	currentPage = currentPage - 1;
	renderAllItems();
});

$("#management-item .right").on("click", function() {
	if (currentPage == maxPage) {
		return;
	}
	currentPage = currentPage + 1;
	renderAllItems();
});

$("#management-item .list-page").on("click", "a", function() {
	let pageClick = parseInt($(this).text());
	if (currentPage == pageClick) {
		return;
	}
	currentPage = pageClick;
	renderAllItems();
});

let currentItems;

function renderAllItems() {
	const nameSearch = $("#search").val();
	const type = $("#management-item .filter-item-block")
		.find(".active")
		.text()
		.trim()
		.toUpperCase();

	let dataFilter = new FormData();
	dataFilter.append("name", nameSearch);
	dataFilter.append("type", type);
	dataFilter.append("page", currentPage);
	dataFilter.append("size", size);

	$.ajax({
		url: `http://localhost:8080/api/items?name=${encodeURIComponent(nameSearch)}&type=${encodeURIComponent(type)}&page=${encodeURIComponent(currentPage)}&size=${encodeURIComponent(size)}`,
		method: "GET",

		success: function(data) {
			renderPagesItem(data.data.totalPages, data.data.totalElements);
			renderItemsPerPage(data.data.data);
			maxPage = data.data.totalPages;
			currentItems = data.data.data
			setupModal(".edit", "modal-edit-item");
			setupModal(".delete", "dialog-confirm-delete");
		},

		error: function(xhr) {
			handleShowToastError(xhr, "Load item")
		},
	});
}

$("#num-per-page").change(function() {
	size = parseInt($(this).val());
	renderAllItems();
})

$("#search").on('keypress', function(e) {
	const page = $(".page-name").text();
	if (e.which == 13) {
		if (page == "Management Item") {
			renderAllItems();
		}
		else if (page == "Dash board") {
			renderAllOrders()
		}
	}
});

// Handle render click edit
$(".item-table").on("click", ".edit", function() {
	const thisId = parseInt($(this).parent().attr("id"));

	let thisItem = currentItems.filter(item => item.id == thisId);
	thisItem = thisItem[0];

	const html = `
	        <div class="image-add">
	                                    <img
	                                        class="curr-image"
	                                        src="/uploads/${thisItem.image}"
	                                        alt=""
	                                    />
	                                    <div class="cancel-image">
	                                        <img
	                                            src="../images/icon/cancel.svg"
	                                            alt=""
	                                        />
	                                    </div>
	                                </div>
	        `;
	$("#modal-edit-item .image-show").empty();
	$("#modal-edit-item .image-show").append(html);

	$("#modal-edit-item #name").val(thisItem.name);
	$("#modal-edit-item #description").val(thisItem.description);
	$("#modal-edit-item #price").val(thisItem.price);

	$("#modal-edit-item .button-filter").removeClass("active");
	if (thisItem.type === "FOOD") {
		$("#modal-edit-item .type-food").addClass("active");
	}
	else if (thisItem.type === "DRINK") {
		$("#modal-edit-item .type-drink").addClass("active");
	}
})

$("#confirm-edit").click(function() {
	console.log("haha")
	const image = $("#modal-edit-item .choose-file").prop("files")[0];
	const name = $("#modal-edit-item #name").val().trim();
	const description = $("#modal-edit-item #description").val().trim();
	let price = $("#modal-edit-item #price").val();
	const type = $("#modal-edit-item")
		.find(".active")
		.text()
		.trim()
		.toUpperCase();

	if (!validateItem(image, name, description, price, "#modal-edit-item")) {
		return;
	}

	price = parseInt(price);

	let dataItem = new FormData();
	dataItem.append("name", name);
	dataItem.append("description", description);
	if (image) {
		dataItem.append("image", image);
	}
	dataItem.append("price", price);
	dataItem.append("type", type);

	$.ajax({
		url: `http://localhost:8080/api/items?id=${encodeURIComponent(currentItemId)}`,
		type: "PUT",
		processData: false,
		contentType: false,
		data: dataItem,

		success: function(data) {
			console.log(data);
			showToast(data.status, "Update item", data.message);
			clearModal("modal-edit-item");
			$("#modal-edit-item").hide();
			$("body").removeClass("modal-open");
			renderAllItems();
		},

		error: function(xhr) {
			handleShowToastError(xhr, "Update item")
		},
	});
});

// Handle delete item
$("#confirm-delete").on("click", function() {
	console.log(currentItemId);
	$.ajax({
		url: `http://localhost:8080/api/items?id=${encodeURIComponent(currentItemId)}`,
		method: "DELETE",

		success: function(data) {
			showToast(data.status, "Delete item", data.message);
			$("#dialog-confirm-delete").hide();
			renderAllItems();
		},

		error: function(xhr) {
			handleShowToastError(xhr, "Delete item")
		}
	})
})


function validate() {
	const newPassword = $("#new-password").val();
	const password = $("#password").val();
	let valid = true;
	if (newPassword === "") {
		$("#new-password").next().text("Please input new password");
		valid = false;
	} else {
		$("#new-password").next().text("");
	}
	if (password === "") {
		$("#password").next().text("Please input password");
		valid = false;
	} else {
		$("#password").next().text("");
	}
	if (newPassword == password) {
		$("#new-password").next().text("The new password must be different from the old password.");
		valid = false;
	} else {
		$("#new-password").next().text("");
	}

	return valid;
}

$("#change-password-form").submit(function(e) {
	e.preventDefault();

	if (!validate()) {
		return;
	}

	$(".note").empty();
	const newPassword = $("#new-password").val();
	const password = $("#password").val();

	$.ajax({
		url: "http://localhost:8080/api/auth/change_password",
		method: "POST",
		contentType: "application/json",
		data: JSON.stringify({
			newPassword: newPassword,
			oldPassword: password
		}),

		success: function(data) {
			showToast("success", "Change password", data.message)
		},
		error: function(xhr) {
			handleShowToastError(xhr, "Change password failed")

		}
	})
});

function loadInfoDashboard() {
	$.ajax({
		url: "http://localhost:8080/api/orders/info",
		method: "GET",

		success: function(data) {
			const info = data.data;
			$(".today-sale .total-statistics").text(info.todaySale + " đ")
			$(".total .total-statistics").text(info.totalOrder)
			$(".shipping .total-statistics").text(info.numShipping)
			$(".complete .total-statistics").text(info.numCompleted)
			$(".cancel .total-statistics").text(info.numCanceled)
		},
		error: function(xhr) {
			handleShowToastError(xhr, "Change password failed")
		}
	})
}

// Handle render item and event
let sizeOrder = 12;
let currentPageOrder = 1;
let maxPageOrder = 1;

function renderPagesOrder(numPage, totalItem) {
	$("#dash-board .total-items").empty();
	$("#dash-board .total-items").text(`Total ${totalItem} orders`);
	$("#dash-board .list-page").empty();
	for (let i = 1; i <= numPage; i++) {
		$("#dash-board .list-page").append(`<a id="page-${i}">${i}</a>`);
	}
}

function renderOrdersPerPage(data) {
	const listOrder = $(".list-order");
	listOrder.empty();

	if (data.length == 0) {
		listOrder.append("<p style = 'text-align: center;width: 100%; margin-top: 30px;'>Order not found</p>")
	}

	data.forEach(function(order) {

		const row = `
								<div id="order-${order.id}" class="item-order">
		                                <div class="num-date">
		                                    <p class="num">Order# ${order.id}</p>
		                                    <p class="date">${order.createAt}</p>
		                                </div>
		                                <div class="item-info">
		                                    <img
		                                        src="/uploads/${order.item.image}"
		                                        alt=""
		                                    />
		                                    <div class="info">
		                                        <p class="name">
		                                            ${order.item.name}
		                                        </p>
		                                        <div>
		                                            <p class="description">
		                                                ${order.item.description}
		                                            </p>
		                                            <div class="price-quantity">
		                                                <p class="price">${order.item.price} đ</p>
		                                                <p class="quantity">
		                                                    Quantity: ${order.item.quantity}
		                                                </p>
		                                            </div>
		                                        </div>
		                                    </div>
		                                </div>
		                                <div class="price-quantity-total">
		                                    <p class="quantity-total">${order.itemCount} items</p>
		                                    <div class="price-status">
		                                        <p class="price">${order.totalPrice} đ</p>
		                                        <div class="status-order ${order.status.toLowerCase()}">
		                                            <img
		                                                src="../images/icon/status-${order.status.toLowerCase()}.svg"
		                                                alt=""
		                                            />
		                                            <p>${order.status}</p>
		                                        </div>
		                                    </div>
		                                </div>
		                            </div>
		      `;
		listOrder.append(row);
	});

	let activePage = `#dash-board #page-${currentPageOrder}`;

	$("#dash-board .list-page a").removeClass("active");
	$(activePage).addClass("active");
}

$("#dash-board .left").on("click", function() {
	if (currentPageOrder == 1) {
		return;
	}
	currentPageOrder = currentPageOrder - 1;
	renderAllOrders();
});

$("#dash-board .right").on("click", function() {
	if (currentPageOrder == maxPageOrder) {
		return;
	}
	currentPageOrder = currentPageOrder + 1;
	renderAllOrders();
});

$("#dash-board .list-page").on("click", "a", function() {
	let pageClick = parseInt($(this).text());
	if (currentPageOrder == pageClick) {
		return;
	}
	currentPageOrder = pageClick;
	renderAllOrders();
});

let currentOrders;
let timeFilterOrder = "all";

$(".list-filter-time").on("click", ".item", function() {
	timeFilterOrder = $(this).text().replace(/\s/g, '').toLowerCase();
	console.log(timeFilterOrder)
	renderAllOrders();
})

function renderAllOrders() {
	const nameSearch = $("#search").val();
	console.log(nameSearch);
	let listStatus = [];
	arrayFilterStatus.forEach((status) => {
		listStatus.push(status.toUpperCase());
	})

	$.ajax({
		url: `http://localhost:8080/api/orders?nameSearch=${encodeURIComponent(nameSearch)}&listStatus=${encodeURIComponent(listStatus)}&time=${encodeURIComponent(timeFilterOrder)}&page=${encodeURIComponent(currentPageOrder)}&size=${encodeURIComponent(sizeOrder)}`,
		method: "GET",

		success: function(data) {
			console.log(data.data);
			renderPagesOrder(data.data.totalPages, data.data.totalElements);
			renderOrdersPerPage(data.data.data);
			maxPageOrder = data.data.totalPages;
			currentOrders = data.data.data

			setupModal(".item-order", "modal-detail-order");
		},

		error: function(xhr) {
			handleShowToastError(xhr, "Load order")
		},
	});
}

function renderListItemDetailOrder(items) {
	const body = $("#modal-detail-order .list-order-detail");
	body.empty();
	let totalItem = 0;
	items.forEach(function(item) {
		totalItem += item.quantity;
		let html = `
							<div class="item-order">
		                            <div class="item-info">
		                                <img
		                                    src="/uploads/${item.image}"
		                                    alt=""
		                                />
		                                <div class="info">
		                                    <p class="name">
		                                        ${item.name}
		                                    </p>
		                                    <div>
		                                        <p class="description">
		                                            ${item.description}
		                                        </p>
		                                        <div class="price-quantity">
		                                            <p class="price">${item.price} đ</p>
		                                            <p class="quantity">Quantity: ${item.quantity}</p>
		                                        </div>
		                                    </div>
		                                </div>
		                            </div>
		                        </div>
		
		`;
		body.append(html);
	});

	$("#modal-detail-order .num-date-quantity .quantity").text(totalItem + " items");
}

function renderListStatusUpdate(status) {
	$("#modal-detail-order .list-status-order").empty();
	switch (status) {
		case "New": {
			$("#modal-detail-order .list-status-order").append("<p class='item'>Canceled</p>");
			$("#modal-detail-order .list-status-order").append("<p class='item'>Shipping</p>");
			break;
		}
		case "Shipping": {
			$("#modal-detail-order .list-status-order").append("<p class='item'>Canceled</p>");
			$("#modal-detail-order .list-status-order").append("<p class='item'>Completed</p>");
			break;
		}
		default: {
			break;
		}
	}

	setupModal(".list-status-order .item", "dialog-confirm-update");
}

function capitalizeFirstLetter(val) {
	val = val.toLowerCase();
	return String(val).charAt(0).toUpperCase() + String(val).slice(1);
}

function renderStatusBlock(status) {
	const $statusDetail = $("#modal-detail-order .status-dropdown-order");
	$statusDetail.empty();
	$statusDetail.removeClass("new");
	$statusDetail.removeClass("completed");
	$statusDetail.removeClass("canceled");
	$statusDetail.removeClass("shipping");
	$statusDetail.addClass(status.toLowerCase());
	$statusDetail.append(`
											<img
			                                    src="../images/icon/status-${status.toLowerCase()}.svg"
			                                    alt=""
			                                />
			                                <p class="curr-status">${capitalizeFirstLetter(status)}</p>
			                                <img
			                                    src="../images/icon/icon-dropdown.svg"
			                                    alt=""
			                                />
			`);
}

function renderDetailOrder(order) {
	$("#modal-detail-order .num-date-quantity .num").text("Order# " + order.id);
	$("#modal-detail-order .num-date-quantity .date").text(order.createAt);

	const items = order.items;
	$("#modal-detail-order .address-info .name").text(order.name);
	$("#modal-detail-order .address-info .phone").text(order.phone);
	$("#modal-detail-order .address-info .address").text(order.address);
	$("#modal-detail-order .address-info .message").text(order.message);
	console.log(order)

	$("#modal-detail-order .total-price-item .price").text(order.totalPrice - order.feeShip);
	$("#modal-detail-order .fee-shipping .price").text(order.feeShip);
	$("#modal-detail-order .total-price-order .price").text(order.totalPrice + " đ");

	renderListItemDetailOrder(items);

	renderStatusBlock(order.status);

	renderListStatusUpdate(order.status);
}

let currentOrderId;
let statusUpdate;
$("#dash-board").on("click", ".item-order", function() {
	let id = $(this).attr("id").replace("order-", "");
	currentOrderId = id;
	$.ajax({
		url: `http://localhost:8080/api/orders/${encodeURIComponent(id)}`,
		method: "GET",

		success: function(data) {
			const order = data.data;
			renderDetailOrder(order);
		},

		error: function(xhr) {
			handleShowToastError(xhr, "Get order")
		}
	})
});

$(".list-status-order").on("click", ".item", function() {
	statusUpdate = $(this).text().toUpperCase();
});

$("#confirm-update").on("click", function() {
	$.ajax({
		url: `http://localhost:8080/api/orders?orderId=${encodeURIComponent(currentOrderId)}&status=${encodeURIComponent(statusUpdate)}`,
		method: "PUT",

		success: function() {
			renderStatusBlock(statusUpdate);
			showToast("success", "Update status", "Update successfully");
			$("#dialog-confirm-update").hide();
			renderAllOrders();
		},

		error: function(xhr) {
			handleShowToastError(xhr, "Get order")
		}
	})
})

function loadChartStats(timeStats) {
	$.ajax({
		url: `http://localhost:8080/api/orders/stats?time=${encodeURIComponent(timeStats)}`,
		method: "GET",

		success: function(data) {
			const stats = data.data;
			if (chart) {
				const label = [];
				const data = [];

				stats.forEach(function(item) {
					label.push(item.label);
					data.push(item.total);
				})

				chart.updateOptions({
					xaxis: {
						categories: label
					},
					series: [
						{
							name: "Revenue",
							data: data
						}
					],
				})
			} else {
				loadChart(stats)
			}
		},

		error: function(xhr) {
			handleShowToastError(xhr, "Get stats chart")
		}
	})
}

var chart;
function loadChart(stats) {
	const label = [];
	const data = [];

	stats.forEach(function(item) {
		label.push(item.label);
		data.push(item.total);
	})

	var options = {
		chart: {
			height: 280,
			type: "area",
			toolbar: {
				show: false
			}
		},
		dataLabels: {
			enabled: false
		},
		series: [
			{
				name: "Revenue",
				data: data
			}
		],
		fill: {
			type: "gradient",
			gradient: {
				shade: 'light',
				type: "vertical",
				shadeIntensity: 1,
				gradientToColors: ["#FFFFFF"],
				inverseColors: false,
				opacityFrom: 0.9,
				opacityTo: 0,
				stops: [0, 100]
			},
			colors: ["#ff6b00"]
		},
		xaxis: {
			categories: label
		},
		yaxis: {
			opposite: true
		}
	};

	chart = new ApexCharts(document.querySelector("#chart-dashboard"), options);

	chart.render();
}

$("#filter-time-chart").on("click", ".button-filter", function() {
	if ($(this).hasClass("active")) {
		return;
	}

	if ($(this).hasClass("day")) {
		loadChartStats("day")
		$("#filter-time-chart .button-filter").removeClass("active");
		$(this).addClass("active");
	} else if ($(this).hasClass("week")) {
		loadChartStats("week")
		$("#filter-time-chart .button-filter").removeClass("active");
		$(this).addClass("active");
	} else if ($(this).hasClass("month")) {
		loadChartStats("month")
		$("#filter-time-chart .button-filter").removeClass("active");
		$(this).addClass("active");
	}
});

function checkLogin() {
	let isLogin = sessionStorage.getItem("login") || 0;
	if (isLogin == 0) {
		return;
	}

	showToast("success", "Login by account", "Successfully");
	sessionStorage.removeItem("login");
}

//Itinial load
$(document).ready(function() {
	$(".content-section").hide();
	const firstTarget = $(".sidebar a").first().data("target");
	$(firstTarget).show();
	$(".sidebar a").first().addClass("active");

	loadInfoDashboard();
	renderAllOrders();
	checkLogin();
	loadChartStats("day");

	$(".sidebar a").click(function(e) {
		e.preventDefault();

		const target = $(this).data("target");

		if (!target) return;

		if (target === "#dash-board") {
			$("#search").val("");
			renderAllOrders();
			loadInfoDashboard();
			$(".page-name").text("Dashboard");
		}

		if (target === "#management-item") {
			$("#search").val("");
			$(".page-name").text("Management Item");
			currentPage = 1;
			renderAllItems();
		}

		if (target === "#change-password") {
			$("#search").val("");
			$(".note").empty();
			$(".page-name").text("Change password");
		}

		$(".content-section").hide();
		$(target).show();

		$(".sidebar a").removeClass("active");
		$(this).addClass("active");
	});
});