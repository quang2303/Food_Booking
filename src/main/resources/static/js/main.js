const formater = new Intl.NumberFormat("vi-VN");

function handleShowToastError(xhr, title) {
	const response = xhr.responseJSON;
	const status = xhr.status;
	if(!window.navigator.onLine) {
		showToast("other", "No network connection", "No network connection. Please check your internet and try again");
	}
	else if(status >= 400 && status < 500) {
		console.log(11111)
		let message = "";
		if(Array.isArray(response.message)) {
			for (let err in response.message) {
				message += response.message[err] + "\n";
			}
		} else {
			message = xhr.responseJSON.message;
		}
		showToast(response.status, title, message);
	}
	else {
		showToast("other", "System error", "The system is under maintenance. Please try again later");
	}
}

function showToast(typeClass, title, message) {
    const toast = $(`<div class="toast">
                <div class="title-toast ${typeClass}">
                    <div class="name-toast">
                        <img src="../images/icon/icon-${typeClass}.svg" alt="" />
                        <p>${title}</p>
                    </div>
                    <img
                        class="close-toast"
                        src="../images/icon/cancel.svg"
                        alt=""
                    />
                </div>
                <div class="content-toast ${typeClass}">${message}</div>
            </div>`);
    $("#toast-container").append(toast);
    $(".close-toast").click(function (e) {
        e.preventDefault();
        $(this).parent().parent().remove();
    });
    setTimeout(() => {
        toast.fadeOut(300, () => toast.remove());
    }, 3000);
}